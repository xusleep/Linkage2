package service.middleware.linkage.framework.access.impl;

import linkage.common.LocalIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.ServiceAccessEngine;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.access.domain.ServiceResponse;
import service.middleware.linkage.framework.connection.pool.NIOConnectionManager;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.io.nio.strategy.mixed.NIOMixedStrategy;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataWriteEvent;
import service.middleware.linkage.framework.repository.WorkingChannelRepository;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.serialization.SerializationUtils;
import service.middleware.linkage.framework.setting.ClientSettingEntity;
import service.middleware.linkage.framework.setting.reader.ClientSettingReader;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * this an engine class of consume
 *
 * @author zhonxu
 */
public class ServiceAccessEngineImpl implements ServiceAccessEngine {
    private final WorkerPool workerPool;
    private static Logger logger = LoggerFactory.getLogger(ServiceAccessEngineImpl.class);
    private final ClientSettingReader workingClientPropertyEntity;

    public ServiceAccessEngineImpl(ClientSettingReader workingClientPropertyEntity, WorkerPool workerPool) {
        this.workingClientPropertyEntity = workingClientPropertyEntity;
        this.workerPool = workerPool;
    }

    /**
     * set the request result
     *
     * @param requestID
     * @param strResult
     */
    public void setRuquestResult(String requestID, String strResult) {
        ServiceRequestResult result = this.resultList.remove(requestID);
        if (result != null) {
            ServiceResponse objResponseEntity = new ServiceResponse();
            objResponseEntity.setRequestID(requestID);
            objResponseEntity.setResult(strResult);
            result.setException(false);
            result.setResponseEntity(objResponseEntity);
        }
    }

    /**
     * @param requestID
     * @param serviceException
     */
    public void setExceptionRuquestResult(String requestID, ServiceException serviceException) {
        ServiceRequestResult result = this.resultList.remove(requestID);
        if (result != null) {
            ServiceResponse objResponseEntity = new ServiceResponse();
            objResponseEntity.setRequestID(requestID);
            objResponseEntity.setResult(serviceException.getMessage());
            result.setException(true);
            result.setException(serviceException);
            result.setResponseEntity(objResponseEntity);
        }
    }

    /**
     * when the response comes, use this method to set it.
     *
     * @param objResponseEntity
     */
    public ServiceRequestResult setRequestResult(ServiceResponse objResponseEntity) {
        ServiceRequestResult result = this.resultList.remove(objResponseEntity.getRequestID());
        if (result != null) {
            result.setException(false);
            result.setResponseEntity(objResponseEntity);
        }
        return result;
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
     * basic process request
     *
     * @param objRequestEntity
     * @param result
     * @param serviceInformation
     * @param channelFromCached
     * @return
     */
    public ServiceRequestResult basicProcessRequest(ServiceRequest objRequestEntity, ServiceRequestResult result,
                                                    ServiceInformation serviceInformation, boolean channelFromCached) {
        WorkingChannelStoreBean workingChannelStoreBean = null;
        NIOWorkingChannelContext newWorkingChannel = null;
        NIOMixedStrategy strategy = null;
        try {
            workingChannelStoreBean = getWorkingChannnelStoreBean(serviceInformation);
            newWorkingChannel = (NIOWorkingChannelContext) workingChannelStoreBean.getWorkingChannelContext();
            result.setWorkingChannel(newWorkingChannel);
            strategy = (NIOMixedStrategy) newWorkingChannel.getWorkingChannelStrategy();
        } catch (Exception ex) {
            setExceptionToRuquestResult(result, new ServiceException(ex, ex.getMessage()));
            return result;
        }
        // put the request result into the request result list
        this.offerRequestResult(result);

        String sendData = SerializationUtils.serializeRequest(objRequestEntity);
        ServiceOnMessageDataWriteEvent objServiceOnMessageWriteEvent;
        logger.debug("request: " + sendData);
        objServiceOnMessageWriteEvent = new ServiceOnMessageDataWriteEvent(newWorkingChannel, sendData);
        strategy.offerMessageWriteQueue(objServiceOnMessageWriteEvent);
        strategy.writeChannel();
        return result;
    }


    //public ServiceRequestResult uploadFile(String )

    /**
     * get a working channel from repository,
     * if not existed, create a new one
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    private WorkingChannelStoreBean getWorkingChannnelStoreBean(ServiceInformation service) throws IOException, InterruptedException, ExecutionException, Exception {
        if (service == null)
            return null;
        WorkingChannelStoreBean workingChannelStoreBean = WorkingChannelRepository.getWorkingChannelStoreBean(service.getId());
        if (workingChannelStoreBean == null) {
            synchronized (WorkingChannelRepository.class) {
                workingChannelStoreBean = WorkingChannelRepository.getWorkingChannelStoreBean(service.toLocalKey());
                if (workingChannelStoreBean == null) {
                    NIOWorkingChannelContext objWorkingChannel = createWorkingChannel(service, WorkingChannelMode.MIXED);
                    workingChannelStoreBean = new WorkingChannelStoreBean(objWorkingChannel, new LinkedList<ServiceRequestResult>());
                }
            }
        }
        return workingChannelStoreBean;
    }

    /**
     * set the request result
     */
    public static void setExceptionToRuquestResult(ServiceRequestResult result, ServiceException serviceException) {
        if (result != null) {
            ServiceResponse objResponseEntity = new ServiceResponse();
            objResponseEntity.setRequestID(result.getRequestID());
            objResponseEntity.setResult(serviceException.getMessage());
            result.setException(true);
            result.setException(serviceException);
            result.setResponseEntity(objResponseEntity);
        }
    }

    /**
     * create a request domain
     *
     * @param clientID
     * @param args
     * @return
     */
    @Override
    public ServiceRequest createRequestEntity(String clientID, List<Object> args, List<Class<?>> argTypes) {
        final ServiceRequest objRequestEntity = new ServiceRequest();
        ClientSettingEntity objServiceClientEntity = searchServiceClientEntity(clientID);
        objRequestEntity.setMethodName(objServiceClientEntity.getServiceMethod());
        objRequestEntity.setGroup(objServiceClientEntity.getServiceGroup());
        objRequestEntity.setServiceName(objServiceClientEntity.getServiceName());
        objRequestEntity.setArgs(args);
        objRequestEntity.setArgsTyps(argTypes);
        objRequestEntity.setRequestID(LocalIdGenerator.generateId());
        return objRequestEntity;
    }

    /**
     * connect to the service
     *
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    private NIOWorkingChannelContext createWorkingChannel(ServiceInformation service, WorkingChannelMode workingChannelMode) throws IOException, ServiceException {
        SocketChannel channel = NIOConnectionManager.createNIOConnection(service.getAddress(), service.getPort());
        NIOWorkingChannelContext objWorkingChannel = (NIOWorkingChannelContext) this.workerPool.register(channel, workingChannelMode);
        objWorkingChannel.setId(service.toLocalKey());
        return objWorkingChannel;
    }
}
