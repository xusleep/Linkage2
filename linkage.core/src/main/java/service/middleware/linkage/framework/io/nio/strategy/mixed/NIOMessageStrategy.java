package service.middleware.linkage.framework.io.nio.strategy.mixed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.handlers.EventDistributionMaster;
import service.middleware.linkage.framework.io.IOProtocol;
import service.middleware.linkage.framework.io.WorkingChannelOperationResult;
import service.middleware.linkage.framework.io.WorkingChannelStrategy;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataReceivedEvent;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataWriteEvent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this strategy is only used for the mixed mode only
 * the message and file will stored in the packet
 *
 * @author zhonxu
 */
public class NIOMessageStrategy extends WorkingChannelStrategy {
    // event distribution master
    private final EventDistributionMaster eventDistributionHandler;
    // read buffer
    private StringBuffer readBuffer = new StringBuffer(10240);
    // write message event queue
    private Queue<ServiceOnMessageDataWriteEvent> writeMessageQueue = new ConcurrentLinkedQueue<ServiceOnMessageDataWriteEvent>();
    private static Logger logger = LoggerFactory.getLogger(NIOMessageStrategy.class);
    private final ExecutorService objExecutorService;

    public NIOMessageStrategy(NIOWorkingChannelContext nioWorkingChannelContext,
                              EventDistributionMaster eventDistributionHandler) {
        super(nioWorkingChannelContext);
        this.eventDistributionHandler = eventDistributionHandler;
        this.objExecutorService = Executors.newFixedThreadPool(10);
    }

    /**
     * offer writer message event to the write queue
     *
     * @param serviceOnMessageDataWriteEvent
     */
    public void offerMessageWriteQueue(ServiceOnMessageDataWriteEvent serviceOnMessageDataWriteEvent) {
        this.writeMessageQueue.offer(serviceOnMessageDataWriteEvent);
    }

    private void readToBuffer(List<String> extractMessages) {
        synchronized (this.getWorkingChannelContext().getLinkageSocketChannel().getReadLock()) {
            try {
                SocketChannel sc = this.getWorkingChannelContext().getLinkageSocketChannel().getSocketChannel();
                int readBytes = 0;
                int ret = 0;
                ByteBuffer bb = ByteBuffer.allocate(IOProtocol.BUFFER_SIZE);
                while ((ret = sc.read(bb)) > 0) {
                    readBytes = readBytes + ret;
                    if (ret < 0) {
                        this.getWorkingChannelContext().closeWorkingChannel();

                    }
                    if (!bb.hasRemaining()) {
                        break;
                    }
                }
                if (ret < 0) {
                    this.getWorkingChannelContext().closeWorkingChannel();
                }
                if (readBytes > 0) {
                    bb.flip();
                }
                byte[] message = new byte[readBytes];
                System.arraycopy(bb.array(), 0, message, 0, readBytes);
                String receiveString = new String(message, IOProtocol.FRAMEWORK_IO_ENCODING);
                this.readBuffer.append(receiveString);
                String unwrappedMessage = "";
                try {
                    while ((unwrappedMessage = IOProtocol.extractMessage(this.readBuffer)) != "") {
                        extractMessages.add(unwrappedMessage);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                this.getWorkingChannelContext().closeWorkingChannel();
            }
        }
    }

    @Override
    public WorkingChannelOperationResult readChannel() {
        List<String> messages = new LinkedList<>();
        readToBuffer(messages);
        for (String message : messages) {
            final String sendMessage = message;
            objExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    ServiceOnMessageDataReceivedEvent event = new ServiceOnMessageDataReceivedEvent(NIOMessageStrategy.this.getWorkingChannelContext(), sendMessage);
                    eventDistributionHandler.submitServiceEvent(event);
                }

            });
        }
        return new WorkingChannelOperationResult(true);
    }

    @Override
    public WorkingChannelOperationResult writeChannel() {
        ServiceOnMessageDataWriteEvent messageEvent;
        for (; ; ) {
            synchronized (this.getWorkingChannelContext().getLinkageSocketChannel().getWriteLock()) {
                if ((messageEvent = writeMessageQueue.poll()) == null) {
                    break;
                }
                if (this.getWorkingChannelContext().getLinkageSocketChannel().isOpen()) {
                    //logger.debug("write message:" + messageEvent.getMessageData());
                    SocketChannel sc = this.getWorkingChannelContext().getLinkageSocketChannel().getSocketChannel();
                    byte[] data = null;
                    try {
                        data = IOProtocol.wrapMessage(messageEvent.getMessageData())
                                .getBytes(IOProtocol.FRAMEWORK_IO_ENCODING);
                    } catch (UnsupportedEncodingException e2) {
                        logger.error(e2.getMessage(), e2);
                    }
                    ByteBuffer buffer = ByteBuffer
                            .allocate(data.length);
                    buffer.put(data, 0, data.length);
                    buffer.flip();
                    if (buffer.hasRemaining()) {
                        try {
                            while (buffer.hasRemaining()) {
                                sc.write(buffer);
                            }
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                            this.getWorkingChannelContext().closeWorkingChannel();
                        }
                    }
                } else {
                    logger.error("the channel is closed, drop writting message:" + messageEvent.getMessageData());
                }
            }
        }
        return new WorkingChannelOperationResult(true);
    }

    /**
     * get event distribution handler
     *
     * @return
     */
    public EventDistributionMaster getEventDistributionHandler() {
        return eventDistributionHandler;
    }

    @Override
    public void closeStrategy() {
        if(writeMessageQueue != null) {
            this.writeMessageQueue.clear();
        }
        this.writeMessageQueue = null;
        this.readBuffer = null;
    }
}
