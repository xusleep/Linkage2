package service.middleware.linkage.framework.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.domain.ServiceResponse;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnFileDataReceivedEvent;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataReceivedEvent;
import service.middleware.linkage.framework.repository.WorkingChannelRepository;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.serialization.ServiceJsonUtils;

import java.io.IOException;

/**
 * the default handler for the client message received event
 *
 * @author zhonxu
 */
public class AccessClientHandler extends Handler {
    private static Logger logger = LoggerFactory.getLogger(AccessClientHandler.class);

    public AccessClientHandler() {
    }

    @Override
    public void handleRequest(ServiceEvent event) throws IOException {
        if (event instanceof ServiceOnMessageDataReceivedEvent) {
            ServiceOnMessageDataReceivedEvent objServiceOnMessageDataReceivedEvent = (ServiceOnMessageDataReceivedEvent) event;
            String receiveData = objServiceOnMessageDataReceivedEvent.getMessageData();
            logger.debug("ServiceOnMessageDataReceivedEvent receive message : " + receiveData);
            ServiceResponse objResponseEntity = ServiceJsonUtils.deserializeResponse(receiveData);
            WorkingChannelStoreBean workingChannelStoreBean = WorkingChannelRepository.getWorkingChannelStoreBeanByChannelId(objServiceOnMessageDataReceivedEvent.getWorkingChannel().getId());
            RequestCallback requestCallback = workingChannelStoreBean.getRequestCallback(objResponseEntity.getRequestID());
            requestCallback.runCallback(objResponseEntity.getJsonResult());
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
