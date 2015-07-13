package service.middleware.linkage.framework.access;

import java.util.List;
import java.util.Map;

import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.access.domain.ServiceInformation;

public interface ServiceAccess {
	
	/**
	 * request directly using the service information domain
	 * @param clientID
	 * @param args
	 * @param serviceInformation
	 * @return
	 */
	public ServiceRequestResult requestService(String clientID, List<Object> args, List<Class<?>> argTypes, ServiceInformation serviceInformation);
	
	/**
	 * request directly using the service information domain
	 * @param clientID
	 * @param args
	 * @param serviceInformation
	 * @param channelFromCached
	 * @return
	 */
	public ServiceRequestResult requestService(String clientID, List<Object> args, List<Class<?>> argTypes,
											   ServiceInformation serviceInformation, boolean channelFromCached);
	
	/**
	 * send the request from the client to request a service
	 * this request will not reuse the repository connect
	 * use this method with closeChannelByRequestResult together
	 * @param clientID the id set in property
	 * @param args  the arguments for the service
	 * @return
	 * @throws Exception
	 */
	public ServiceRequestResult requestServicePerConnect(String clientID, List<Object> args, List<Class<?>> argTypes, ServiceInformation serviceInformation);

	/**
	 * send the request from the client to request a service synchronized
	 * this request will not reuse the channel
	 * this method is a synchronized method, the result will return once the method return
	 * @param clientID the id set in property
	 * @param args  the arguments for the service
	 * @return
	 * @throws Exception
	 */
	public ServiceRequestResult requestServicePerConnectSync(String clientID, List<Object> args, List<Class<?>> argTypes, ServiceInformation serviceInformation);
	
	/**
	 * close the channel by request result
	 * the client could determine close the channel or not
	 * this method is used along with method requestServicePerConnectSync
	 * don't use it along with method prcessRequest
	 * @param objServiceRequestResult
	 */
	public void closeChannelByRequestResult(ServiceRequestResult objServiceRequestResult);
	
	/**
	 * get the engine
	 * @return
	 */
	public ServiceAccessEngine getServiceAccessEngine(); 
}
