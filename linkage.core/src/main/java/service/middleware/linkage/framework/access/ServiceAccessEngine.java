package service.middleware.linkage.framework.access;

import java.util.List;
import java.util.Map;

import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkingChannelContext;
import service.middleware.linkage.framework.access.domain.ServiceResponse;

public interface ServiceAccessEngine {
	
	/**
	 * basic process request
	 * @param objRequestEntity
	 * @param result
	 * @param serviceInformation
	 * @param channelFromCached
	 * @return
	 */
	public ServiceRequestResult basicProcessRequest(ServiceRequest objRequestEntity, ServiceRequestResult result,
			ServiceInformation serviceInformation, boolean channelFromCached);
	
	/**
	 * create a request domain
	 * @param clientID
	 * @param args
	 * @return
	 */
	public ServiceRequest createRequestEntity(String clientID, List<Object> args, List<Class<?>> argTypes);
	
	/**
	 * close the channel by request result
	 * the client could determine close the channel or not
	 * this method is used along with method prcessRequestPerConnect
	 * don't use it along with method prcessRequest
	 * @param objServiceRequestResult
	 */
	public void closeChannelByRequestResult(ServiceRequestResult objServiceRequestResult);
	/**
	 * remove the channel from the repository
	 */
	public void removeCachedChannel(WorkingChannelContext objWorkingChannel);
	/**
	 * when the response comes, use this method to set it. 
	 * @param objResponseEntity
	 */
	public ServiceRequestResult setRequestResult(ServiceResponse objResponseEntity);
	
	/**
	 * clear the result
	 */
	public void clearAllResult(ServiceException exception);
}
