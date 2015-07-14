package service.middleware.linkage.framework.access.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.mixed.NIOMessageStrategy;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataWriteEvent;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.route.Route;
import service.middleware.linkage.framework.route.impl.DefaultRoute;
import service.middleware.linkage.framework.serialization.SerializationUtils;
import service.middleware.linkage.framework.setting.ClientSettingEntity;
import service.middleware.linkage.framework.setting.reader.ClientSettingReader;

import java.util.List;

/**
 * this an engine class of consume
 *
 * @author zhonxu
 */
public class ServiceAccessImpl implements ServiceAccess {
    private static Logger logger = LoggerFactory.getLogger(ServiceAccessImpl.class);
    private final ClientSettingReader workingClientPropertyEntity;
    private final Route route;

    public ServiceAccessImpl(ClientSettingReader workingClientPropertyEntity) {
        this.workingClientPropertyEntity = workingClientPropertyEntity;
        this.route = new DefaultRoute();
    }

    public ServiceAccessImpl(ClientSettingReader workingClientPropertyEntity, Route route) {
        this.workingClientPropertyEntity = workingClientPropertyEntity;
        this.route = route;
    }

    /**
     * search the configuration check the configure for the service
     *
     * @param id
     * @return
     */
    protected ClientSettingEntity searchServiceClientEntity(String id) {
        for (ClientSettingEntity entity : this.workingClientPropertyEntity.getServiceClientList()) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }


    /**
     * request service
     *
     * @param address
     * @param port
     * @param clientID
     * @param args
     * @param requestCallback
     */
    @Override
    public void requestService(String address, int port, String clientID, List<Object> args,
                               RequestCallback requestCallback) {
        ServiceRequest objRequestEntity = this.createRequestEntity(clientID, args);
        WorkingChannelStoreBean workingChannelStoreBean = null;
        NIOWorkingChannelContext workingChannel = null;
        NIOMessageStrategy strategy = null;
        try {
            workingChannelStoreBean = route.chooseRoute(address, port);
            workingChannel = (NIOWorkingChannelContext) workingChannelStoreBean.getWorkingChannelContext();
            workingChannelStoreBean.offerRequestResult(objRequestEntity.getRequestID(), requestCallback);
            strategy = (NIOMessageStrategy) workingChannel.getWorkingChannelStrategy();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        String sendData = SerializationUtils.serializeRequest(objRequestEntity);
        ServiceOnMessageDataWriteEvent objServiceOnMessageWriteEvent;
        logger.debug("request: " + sendData);
        objServiceOnMessageWriteEvent = new ServiceOnMessageDataWriteEvent(workingChannel, sendData);
        strategy.offerMessageWriteQueue(objServiceOnMessageWriteEvent);
        strategy.writeChannel();
    }

    /**
     * create a request entity
     *
     * @param clientID
     * @param args
     * @return
     */
    public ServiceRequest createRequestEntity(String clientID, List<Object> args) {
        final ServiceRequest objRequestEntity = new ServiceRequest();
        ClientSettingEntity objServiceClientEntity = searchServiceClientEntity(clientID);
        objRequestEntity.setMethodName(objServiceClientEntity.getServiceMethod());
        objRequestEntity.setGroup(objServiceClientEntity.getServiceGroup());
        objRequestEntity.setServiceName(objServiceClientEntity.getServiceName());
        objRequestEntity.setArgs(args);
        objRequestEntity.setVersion(objServiceClientEntity.getServiceVersion());
        return objRequestEntity;
    }
}
