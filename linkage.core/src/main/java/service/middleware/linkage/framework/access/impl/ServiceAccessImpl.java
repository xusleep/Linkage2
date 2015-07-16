package service.middleware.linkage.framework.access.impl;

import linkage.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.connection.NIOConnectionManager;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.io.nio.strategy.mixed.NIOMessageStrategy;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataWriteEvent;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.serialization.ServiceJsonUtils;
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
    private final NIOConnectionManager nioConnectionManager;

    public ServiceAccessImpl(ClientSettingReader workingClientPropertyEntity, NIOConnectionManager nioConnectionManager) {
        this.workingClientPropertyEntity = workingClientPropertyEntity;
        this.nioConnectionManager = nioConnectionManager;
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
     * @param parameters
     * @param requestCallback
     */
    @Override
    public void requestService(String address, int port, String clientID, List<ServiceParameter> parameters,
                               RequestCallback requestCallback) {
        ServiceRequest objRequestEntity = this.createRequestEntity(clientID, parameters);
        WorkingChannelStoreBean workingChannelStoreBean = null;
        NIOWorkingChannelContext workingChannel = null;
        NIOMessageStrategy strategy = null;
        try {
            workingChannelStoreBean = nioConnectionManager.connect(address, port);
            if(workingChannelStoreBean == null){
                requestCallback.runException(new Exception("can not create connection."));
                return;
            }
            workingChannel = (NIOWorkingChannelContext) workingChannelStoreBean.getWorkingChannelContext();
            workingChannelStoreBean.offerRequestResult(objRequestEntity.getRequestID(), requestCallback);
            strategy = (NIOMessageStrategy) workingChannel.getWorkingChannelStrategy();
        } catch (Exception ex) {
            requestCallback.runException(ex);
            logger.error(ex.getMessage(), ex);
        }
        String sendData = ServiceJsonUtils.serializeRequest(objRequestEntity);
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
     * @param parameters
     * @return
     */
    @Override
    public ServiceRequest createRequestEntity(String clientID, List<ServiceParameter> parameters) {
        ServiceRequest objRequestEntity = new ServiceRequest();
        ClientSettingEntity objServiceClientEntity = searchServiceClientEntity(clientID);
        objRequestEntity.setRequestID(CommonUtils.generateId());
        objRequestEntity.setMethodName(objServiceClientEntity.getServiceMethod());
        objRequestEntity.setGroup(objServiceClientEntity.getServiceGroup());
        objRequestEntity.setServiceName(objServiceClientEntity.getServiceName());
        objRequestEntity.setServiceParameters(parameters);
        objRequestEntity.setVersion(objServiceClientEntity.getServiceVersion());
        return objRequestEntity;
    }
}
