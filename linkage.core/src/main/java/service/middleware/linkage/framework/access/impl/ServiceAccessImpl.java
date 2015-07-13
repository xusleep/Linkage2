package service.middleware.linkage.framework.access.impl;

import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.ServiceAccessEngine;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.access.domain.ServiceResponse;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.setting.reader.ClientSettingReader;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * the default consume which wrapped the method request
 *
 * @author zhonxu
 */
public class ServiceAccessImpl implements ServiceAccess {

    protected ServiceAccessEngine serviceEngine;
    // use the concurrent hash map to store the request result list {@link ServiceRequestResult}
    private final ConcurrentHashMap<String, ServiceRequestResult> resultList = new ConcurrentHashMap<String, ServiceRequestResult>(2048);

    public ServiceAccessImpl(ClientSettingReader workingClientPropertyEntity, WorkerPool workerPool) {
        serviceEngine = new ServiceAccessEngineImpl(workingClientPropertyEntity, workerPool);
    }

    @Override
    public ServiceRequestResult requestService(String clientID, List<Object> args, List<Class<?>> argTypes, ServiceInformation serviceInformation) {
        return requestService(clientID, args, argTypes, serviceInformation, true);
    }

    @Override
    public ServiceRequestResult requestServicePerConnect(String clientID,
                                                         List<Object> args, List<Class<?>> argTypes, ServiceInformation serviceInformation) {
        // TODO Auto-generated method stub
        return requestService(clientID, args, argTypes, serviceInformation, false);
    }

    @Override
    public ServiceRequestResult requestServicePerConnectSync(String clientID,
                                                             List<Object> args, List<Class<?>> argTypes, ServiceInformation serviceInformation) {
        ServiceRequestResult result = requestService(clientID, args, argTypes, serviceInformation, false);
        result.getResponseEntity();
        result.setServiceInformation(serviceInformation);
        this.closeChannelByRequestResult(result);
        return result;
    }

    @Override
    public ServiceRequestResult requestService(String clientID, List<Object> args, List<Class<?>> argTypes,
                                               ServiceInformation serviceInformation, boolean channelFromCached) {
        ServiceRequest objRequestEntity = serviceEngine.createRequestEntity(clientID, args, argTypes);
        ServiceRequestResult result = new ServiceRequestResult();
        result.setRequestID(objRequestEntity.getRequestID());
        return this.serviceEngine.basicProcessRequest(objRequestEntity, result, serviceInformation, channelFromCached);
    }

    @Override
    public void closeChannelByRequestResult(
            ServiceRequestResult objServiceRequestResult) {
        // TODO Auto-generated method stub
        serviceEngine.closeChannelByRequestResult(objServiceRequestResult);
    }

    @Override
    public ServiceAccessEngine getServiceAccessEngine() {
        // TODO Auto-generated method stub
        return serviceEngine;
    }

    /**
     * when the response comes, use this method to set it.
     *
     * @param objResponseEntity
     */
    public ServiceRequestResult setRequestResult(ServiceResponse objResponseEntity) {
        return serviceEngine.setRequestResult(objResponseEntity);
    }

    /**
     * clear the result
     */
    public void clearAllResult(ServiceException exception) {
        serviceEngine.clearAllResult(exception);
    }
}
