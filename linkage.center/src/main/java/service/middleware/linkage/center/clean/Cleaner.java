package service.middleware.linkage.center.clean;

import service.middleware.linkage.framework.access.domain.ServiceRequestResult;

/**
 * this interface will deal with the clean job 
 * @author Smile
 *
 */
public interface Cleaner {
	public void clean(ServiceRequestResult objServiceRequestResult);
}
