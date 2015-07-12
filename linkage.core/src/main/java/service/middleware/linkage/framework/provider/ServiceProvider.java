package service.middleware.linkage.framework.provider;

import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceResponse;

/**
 * This class provide a service access point 
 * for all of the service
 * @author zhonxu
 *
 */
public interface ServiceProvider {
	/**
	 * process the request from the client
	 * @param request
	 * @return
	 */
	public ServiceResponse acceptServiceRequest(ServiceRequest request);
}
