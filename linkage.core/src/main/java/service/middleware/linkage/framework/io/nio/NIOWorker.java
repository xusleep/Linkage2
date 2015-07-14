package service.middleware.linkage.framework.io.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.Worker;
import service.middleware.linkage.framework.io.WorkingChannelContext;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this is default worker, will be used when there is connection established
 * it will deal with the message send&receive between the client and service
 *
 * @author zhonxu
 */
public class NIOWorker implements Worker {
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();
    protected final AtomicBoolean wakenUp = new AtomicBoolean();
    private final Selector selector;
    private volatile boolean isShutdown = false;
    private static Logger logger = LoggerFactory.getLogger(NIOWorker.class);

    public NIOWorker() throws Exception {
        selector = Selector.open();
    }

    public void run() {
        logger.debug("start worker hashCode = " + this.hashCode());
        // Listening
        while (true) {
            try {
                wakenUp.set(false);
                int num = 0;
                num = selector.select();
                // If we shutdown the loop, then beak from the loop
                if (isShutdown) {
                    logger.debug("shutdown, break loop after select.");
                    break;
                }
                processTaskQueue();
                if (num > 0) {
                    Set selectedKeys = selector.selectedKeys();
                    Iterator it = selectedKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        it.remove();
                        if (key.isReadable()) {
                            if (!read(key)) {
                                this.closeChannel(key);
                            }
                        } else if (key.isWritable()) {
                            writeFromSelectorLoop(key);
                        }
                    }
                }
            } catch (IOException e) {
                if (isShutdown) {
                    logger.debug(e.getMessage(), e);
                    break;
                }
                logger.error(e.getMessage(), e);
                continue;
            }
        }
    }

    /**
     * shutdown
     */
    public void shutdown() {
        logger.debug("shutdown worker.");
        isShutdown = true;
        if (selector != null) {
            if (wakenUp.compareAndSet(false, true)) {
                selector.wakeup();
            }
        }
    }

    /**
     * shutdown
     */
    public void shutdownImediate() {
        logger.debug("shutdown worker imediately.");
        isShutdown = true;
        if (selector != null) {
            if (wakenUp.compareAndSet(false, true)) {
                selector.wakeup();
            }
        }
    }

    /**
     * deal with the task queue
     */
    private void processTaskQueue() {
        for (; ; ) {
            final Runnable task = taskQueue.poll();
            if (task == null) {
                break;
            }
            task.run();
        }
    }

    /**
     * write when the select event happens
     *
     * @param key
     */
    private void writeFromSelectorLoop(final SelectionKey key) {
        WorkingChannelContext workingChannel = (WorkingChannelContext) key.attachment();
        workingChannel.writeChannel();
    }


    /**
     * close the channel
     *
     * @throws IOException
     */
    private void closeChannel(SelectionKey key) throws IOException {
        WorkingChannelContext channel = (WorkingChannelContext) key.attachment();
        if (channel != null) {
            channel.closeWorkingChannel();
        }
    }

    /**
     * @param key
     * @return
     */
    private boolean read(SelectionKey key) {
        final NIOWorkingChannelContext workingChannel = (NIOWorkingChannelContext) key.attachment();
        return workingChannel.readChannel().isSuccess();
    }

    /**
     * register a channel to the worker
     */
    public WorkingChannelContext submitOpeRegister(WorkingChannelContext objWorkingChannel) {
        registerTask(new RegisterTask(objWorkingChannel));
        return objWorkingChannel;
    }

    /**
     * submit a register task to the task queue
     *
     * @param task
     */
    protected final void registerTask(Runnable task) {
        taskQueue.add(task);
        Selector selector = this.selector;
        if (selector != null) {
            if (wakenUp.compareAndSet(false, true)) {
                selector.wakeup();
            }
        } else {
            if (taskQueue.remove(task)) {
                // the selector was null this means the Worker has already been shutdown.
                throw new RejectedExecutionException("Worker has already been shutdown");
            }
        }
    }

    /**
     * this class is used to register the task to the worker
     *
     * @author zhonxu
     */
    private final class RegisterTask implements Runnable {

        private NIOWorkingChannelContext objWorkingChannel;

        public RegisterTask(WorkingChannelContext workingChannel) {
            this.objWorkingChannel = (NIOWorkingChannelContext) workingChannel;
        }

        @Override
        public void run() {
            SocketChannel schannel = objWorkingChannel.getLinkageSocketChannel().getSocketChannel();
            try {
                schannel.configureBlocking(false);
                SelectionKey key = schannel.register(selector, SelectionKey.OP_READ, objWorkingChannel);
                objWorkingChannel.setKey(key);
            } catch (Exception e) {
                try {
                    schannel.finishConnect();
                    schannel.close();
                    schannel.socket().close();
                } catch (Exception e1) {
                }
            }
        }

    }
}
