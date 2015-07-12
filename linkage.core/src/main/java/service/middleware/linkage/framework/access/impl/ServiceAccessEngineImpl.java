package service.middleware.linkage.framework.access.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.ServiceAccessEngine;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.connection.pool.NIOConnectionPoolManager;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.io.WorkingChannelContext;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.io.nio.strategy.mixed.NIOMixedStrategy;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnMessageDataWriteEvent;
import service.middleware.linkage.framework.serialization.SerializationUtils;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceResponse;
import service.middleware.linkage.framework.setting.ClientSettingEntity;
import service.middleware.linkage.framework.setting.reader.ClientSettingReader;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * this an engine class of consume
 *
 * @author zhonxu
 */
public class ServiceAccessEngineImpl implements ServiceAccessEngine {
    /**
     * used to cached the {@link WorkingChannelContext} object
     */
    private final HashMap<String, WorkingChannelContext> workingChannelCacheList = new HashMap<String, WorkingChannelContext>(16);
    private AtomicLong idGenerator = new AtomicLong(0);
    private final WorkerPool workerPool;
    private static Logger logger = LoggerFactory.getLogger(ServiceAccessEngineImpl.class);
    private final ClientSettingReader workingClientPropertyEntity;
    // use the concurrent hash map to store the request result list {@link ServiceRequestResult}
    private final ConcurrentHashMap<String, ServiceRequestResult> resultList = new ConcurrentHashMap<String, ServiceRequestResult>(2048);

    public ServiceAccessEngineImpl(ClientSettingReader workingClientPropertyEntity, WorkerPool workerPool) {
        this.workingClientPropertyEntity = workingClientPropertyEntity;
        this.workerPool = workerPool;
    }

    /**
     * put the request result in the result list
     *
     * @param serviceRequestResult
     */
    public void offerRequestResult(ServiceRequestResult serviceRequestResult) {
        resultList.put(serviceRequestResult.getRequestID(), serviceRequestResult);
    }

    /**
     * clear the result
     */
    public void clearAllResult(ServiceException exception) {
        Enumeration<String> keyEnumeration = resultList.keys();
        while (keyEnumeration.hasMoreElements()) {
            String requestID = keyEnumeration.nextElement();
            ServiceRequestResult serviceRequestResult = resultList.get(requestID);
            setExceptionToRuquestResult(serviceRequestResult, exception);
        }
        resultList.clear();
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
        NIOWorkingChannelContext newWorkingChannel = null;
        NIOMixedStrategy strategy = null;
        try {
            newWorkingChannel = (NIOWorkingChannelContext) getWorkingChannnel(channelFromCached, serviceInformation);
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
     * get a working channel from cache,
     * if not existed, create a new one
     *
     * @param fromCached get it from the cache or not
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    private WorkingChannelContext getWorkingChannnel(boolean fromCached, ServiceInformation service) throws IOException, InterruptedException, ExecutionException, Exception {
        if (service == null)
            return null;
        String cacheID = service.toString();
        NIOWorkingChannelContext objWorkingChannel;
        // if not get it from the cache, create it directly
        if (!fromCached) {
            objWorkingChannel = createWorkingChannel(service, WorkingChannelMode.MIXED);
            objWorkingChannel.setWorkingChannelCacheID(cacheID);
            return objWorkingChannel;
        }
        objWorkingChannel = (NIOWorkingChannelContext) workingChannelCacheList.get(cacheID);
        if (objWorkingChannel == null) {
            synchronized (workingChannelCacheList) {
                // get the working channel again
                // in case we set after we get from the cache
                objWorkingChannel = (NIOWorkingChannelContext) workingChannelCacheList.get(service.toString());
                if (objWorkingChannel == null) {
                    objWorkingChannel = createWorkingChannel(service, WorkingChannelMode.MIXED);
                    objWorkingChannel.setWorkingChannelCacheID(cacheID);
                    workingChannelCacheList.put(cacheID, objWorkingChannel);
                }
            }
        }
        return objWorkingChannel;
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
    public ServiceRequest createRequestEntity(String clientID, List<String> args) {
        UUID uuid = UUID.randomUUID();
        final ServiceRequest objRequestEntity = new ServiceRequest();
        ClientSettingEntity objServiceClientEntity = searchServiceClientEntity(clientID);
        objRequestEntity.setMethodName(objServiceClientEntity.getServiceMethod());
        objRequestEntity.setGroup(objServiceClientEntity.getServiceGroup());
        objRequestEntity.setServiceName(objServiceClientEntity.getServiceName());
        objRequestEntity.setArgs(args);
        objRequestEntity.setRequestID(uuid.toString());
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
        SocketChannel channel = NIOConnectionPoolManager.getIOConnection(service.getAddress() + ":" + service.getPort());
        NIOWorkingChannelContext objWorkingChannel = (NIOWorkingChannelContext) this.workerPool.register(channel, workingChannelMode);
        return objWorkingChannel;
    }

    /**
     * remove the channel from the cache
     */
    public void removeCachedChannel(WorkingChannelContext objWorkingChannel) {
        if (objWorkingChannel != null) {
            synchronized (workingChannelCacheList) {
                this.workingChannelCacheList.remove(((NIOWorkingChannelContext) objWorkingChannel).getWoringChannelCacheID());
            }
        }
    }

    /**
     * close the channel by request result
     * the client could determine close the channel or not
     * this method is used along with method prcessRequestPerConnect
     * don't use it along with method prcessRequest
     *
     * @param objServiceRequestResult
     */
    public void closeChannelByRequestResult(ServiceRequestResult objServiceRequestResult) {
        removeCachedChannel(objServiceRequestResult.getWorkingChannel());
        if (objServiceRequestResult.getWorkingChannel() != null) {
            objServiceRequestResult.getWorkingChannel().closeWorkingChannel();
        }
    }
}
