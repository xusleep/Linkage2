//package service.middleware.linkage.center.serviceaccess;
//
//import java.util.List;
//
//import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
//
//public interface RouteServiceAccess {
//	/**
//	 * request directly using the service information domain
//	 * @param clientID
//	 * @param args
//	 * @return
//	 */
//	public ServiceRequestResult requestService(String clientID, List<Object> args, List<Class<?>> argTypes);
//
//	/**
//	 * request directly using the service information domain
//	 * @param clientID
//	 * @param args
//	 * @param channelFromCached
//	 * @return
//	 */
//	public ServiceRequestResult requestService(String clientID,
//											   List<Object> args, List<Class<?>> argTypes, boolean channelFromCached);
//
//	/**
//	 * send the request from the client to request a service
//	 * this request will not reuse the repository connect
//	 * use this method with closeChannelByRequestResult
//	 * @param clientID the id set in property
//	 * @param args  the arguments for the service
//	 * @return
//	 * @throws Exception
//	 */
//	public ServiceRequestResult requestServicePerConnect(String clientID, List<Object> args, List<Class<?>> argTypes);
//
//	/**
//	 * send the request from the client to request a service synchronized
//	 * this request will not reuse the channel
//	 * this method is a synchronized method, the result will return once the method return
//	 * @param clientID the id set in property
//	 * @param args  the arguments for the service
//	 * @return
//	 * @throws Exception
//	 */
//	public ServiceRequestResult requestServicePerConnectSync(String clientID, List<Object> args, List<Class<?>> argTypes);
//
//}
