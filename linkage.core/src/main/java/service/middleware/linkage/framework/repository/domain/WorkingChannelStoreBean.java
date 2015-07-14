package service.middleware.linkage.framework.repository.domain;

import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.io.WorkingChannelContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class WorkingChannelStoreBean {
    private final WorkingChannelContext workingChannelContext;
    private final ConcurrentHashMap<String, RequestCallback> serviceRequestCallbacks;

    public WorkingChannelStoreBean(WorkingChannelContext workingChannelContext){
        this.workingChannelContext = workingChannelContext;
        this.serviceRequestCallbacks = new ConcurrentHashMap<>();
    }

    public WorkingChannelContext getWorkingChannelContext() {
        return workingChannelContext;
    }

    /**
     * put the request result in the result list
     *
     * @param requestCallback
     */
    public void offerRequestResult(String requestId, RequestCallback requestCallback) {
        serviceRequestCallbacks.put(requestId, requestCallback);
    }

    /**
     * »ñµÃServiceRequestResult
     * @param requestId
     * @return
     */
    public RequestCallback getRequestCallback(String requestId){
        return serviceRequestCallbacks.get(requestId);
    }

    /**
     * É¾³ýServiceRequestResult
     * @param requestId
     * @return
     */
    public RequestCallback removeRequestCallback(String requestId){
        return serviceRequestCallbacks.remove(requestId);
    }
}
