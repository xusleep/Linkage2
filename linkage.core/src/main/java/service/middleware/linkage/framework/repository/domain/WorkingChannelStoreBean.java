package service.middleware.linkage.framework.repository.domain;

import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.io.WorkingChannelContext;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class WorkingChannelStoreBean {
    private final WorkingChannelContext workingChannelContext;
    private final ConcurrentHashMap<String, ServiceRequestResult> serviceRequestResults;

    public WorkingChannelStoreBean(WorkingChannelContext workingChannelContext){
        this.workingChannelContext = workingChannelContext;
        this.serviceRequestResults = new ConcurrentHashMap<>();
    }

    public WorkingChannelContext getWorkingChannelContext() {
        return workingChannelContext;
    }

    /**
     * put the request result in the result list
     *
     * @param serviceRequestResult
     */
    public void offerRequestResult(ServiceRequestResult serviceRequestResult) {
        serviceRequestResults.put(serviceRequestResult.getRequestID(), serviceRequestResult);
    }
}
