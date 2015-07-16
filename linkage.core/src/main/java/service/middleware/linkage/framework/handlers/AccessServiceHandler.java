package service.middleware.linkage.framework.handlers;

import linkage.common.EncodingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceResponse;
import service.middleware.linkage.framework.io.WorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.mixed.NIOMessageStrategy;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnFileDataReceivedEvent;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataReceivedEvent;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataWriteEvent;
import service.middleware.linkage.framework.provider.ServiceProvider;
import service.middleware.linkage.framework.serialization.ServiceJsonUtils;

import java.io.IOException;

/**
 * Service handler from the server side
 *
 * @author zhonxu
 */
public class AccessServiceHandler extends Handler {
    private static Logger logger = LoggerFactory.getLogger(AccessServiceHandler.class);
    private final ServiceProvider provider;

    public AccessServiceHandler(ServiceProvider provider) {
        this.provider = provider;
    }

    @Override
    public void handleRequest(ServiceEvent event) throws IOException {
        if (event instanceof ServiceOnMessageDataReceivedEvent) {
            ServiceOnMessageDataReceivedEvent objServiceOnMessageDataReceivedEvent = (ServiceOnMessageDataReceivedEvent) event;
            WorkingChannelContext channel = objServiceOnMessageDataReceivedEvent.getWorkingChannel();
            NIOMessageStrategy strategy = (NIOMessageStrategy) channel.getWorkingChannelStrategy();
            ServiceRequest objRequestEntity = ServiceJsonUtils.deserializeRequest(objServiceOnMessageDataReceivedEvent.getMessageData());
            logger.debug("ServiceOnMessageDataReceivedEvent receive message : " + objServiceOnMessageDataReceivedEvent.getMessageData());
            ServiceResponse objResponseEntity = this.provider.acceptServiceRequest(objRequestEntity);
            String responseStr = ServiceJsonUtils.serializeResponse(objResponseEntity);
            if (channel.getLinkageSocketChannel().isOpen()) {
                logger.debug("ServiceOnMessageDataWriteEvent write back message : " + responseStr);
                ServiceOnMessageDataWriteEvent objServiceOnMessageWriteEvent = new ServiceOnMessageDataWriteEvent(channel, new String(responseStr.getBytes("GBK"), EncodingUtils.FRAMEWORK_IO_ENCODING));
                strategy.offerMessageWriteQueue(objServiceOnMessageWriteEvent);
                strategy.writeChannel();
            }
            else{
                logger.debug("channel is closed, drop message writting:" + responseStr);
            }
        } else if (event instanceof ServiceOnFileDataReceivedEvent) {
            ServiceOnFileDataReceivedEvent objServerOnFileDataReceivedEvent = (ServiceOnFileDataReceivedEvent) event;
            logger.debug("ServerOnFileDataReceivedEvent receive message : " + objServerOnFileDataReceivedEvent.getFileID());
        }

        if (this.getNext() != null) {
            try {
                this.getNext().handleRequest(event);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
