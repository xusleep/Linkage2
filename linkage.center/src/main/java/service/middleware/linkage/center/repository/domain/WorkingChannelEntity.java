package service.middleware.linkage.center.repository.domain;

import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.io.WorkingChannelContext;

import java.util.List;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class WorkingChannelEntity {
    private final WorkingChannelContext workingChannelContext;
    private final List<ServiceRequestResult> serviceRequestResults;

    public WorkingChannelEntity(WorkingChannelContext workingChannelContext, List<ServiceRequestResult> serviceRequestResults){
        this.workingChannelContext = workingChannelContext;
        this.serviceRequestResults = serviceRequestResults;
    }

    public WorkingChannelContext getWorkingChannelContext() {
        return workingChannelContext;
    }

    public List<ServiceRequestResult> getServiceRequestResults() {
        return serviceRequestResults;
    }
}
