package service.middleware.linkage.framework.access.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.mixed.NIOMessageStrategy;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataWriteEvent;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.route.MultiConnectionRoute;
import service.middleware.linkage.framework.route.impl.DefaultMultiConnectionRoute;
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
    private final MultiConnectionRoute multiConnectionRoute;

    public ServiceAccessImpl(ClientSettingReader workingClientPropertyEntity) {
        this.workingClientPropertyEntity = workingClientPropertyEntity;
        this.multiConnectionRoute = new DefaultMultiConnectionRoute();
    }

    public ServiceAccessImpl(ClientSettingReader workingClientPropertyEntity, MultiConnectionRoute multiConnectionRoute) {
        this.workingClientPropertyEntity = workingClientPropertyEntity;
        this.multiConnectionRoute = multiConnectionRoute;
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
            workingChannelStoreBean = multiConnectionRoute.chooseRoute(address, port);
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
     * @param parameters
     * @return
     */
    @Override
    public ServiceRequest createRequestEntity(String clientID, List<ServiceParameter> parameters) {
        final ServiceRequest objRequestEntity = new ServiceRequest();
        ClientSettingEntity objServiceClientEntity = searchServiceClientEntity(clientID);
        objRequestEntity.setMethodName(objServiceClientEntity.getServiceMethod());
        objRequestEntity.setGroup(objServiceClientEntity.getServiceGroup());
        objRequestEntity.setServiceName(objServiceClientEntity.getServiceName());
        objRequestEntity.setParameters(parameters);
        objRequestEntity.setVersion(objServiceClientEntity.getServiceVersion());
        return objRequestEntity;
    }
}
