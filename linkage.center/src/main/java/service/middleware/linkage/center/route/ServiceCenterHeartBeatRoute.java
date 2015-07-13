package service.middleware.linkage.center.route;
//package management.service.center.route;
//
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import management.service.center.ServiceCenter;
//import service.framework.common.domain.ServiceRequest;
//import service.framework.common.domain.ServiceRequestResult;
//import service.framework.common.domain.ServiceInformation;
//import service.framework.comsume.ConsumerBean;
//import service.framework.exception.NoServiceRegisteredException;
//import service.framework.exception.ServiceException;
//import service.framework.route.AbstractRoute;
//import service.framework.route.filters.RouteFilter;
///**
// * This route is used for the service center
// * If will first access the service center, get the service list
// * then choose one service from the list.
// * If the service list exist in the repository, it will use it directly without access it again
// * @author zhonxu
// *
// */
//public class ServiceCenterHeartBeatRoute extends AbstractRoute {
//	private final ConcurrentHashMap<String, List<ServiceInformation>> serviceListCache = new ConcurrentHashMap<String, List<ServiceInformation>>(16);
//	private final AtomicInteger nextCounter = new AtomicInteger(0);
//	
//	public ServiceCenterHeartBeatRoute(){
//		
//	}
//	
//
//	@Override
//	public ServiceInformation chooseRoute(ServiceRequest requestEntity, ConsumerBean serviceCenterConsumerBean) throws ServiceException {
//		return getNextServiceFromServiceList();
//	}
//	
//	/**
//	 * 
//	 * @return
//	 * @throws ServiceException 
//	 */
//	private ServiceInformation getNextServiceFromServiceList() throws ServiceException{
//		if(ServiceCenter.serviceInformationList.size() == 0){
//			throw new NoServiceRegisteredException(new Exception("no service register to the service center"), "no service register to the service center");
//		}
//		return ServiceCenter.serviceInformationList.get(nextCounter.incrementAndGet() % ServiceCenter.serviceInformationList.size());
//	}
//	
//	private List<RouteFilter> filters;
//	
//	public List<RouteFilter> getFilters() {
//		return filters;
//	}
//
//	public void setFilters(List<RouteFilter> filters) {
//		this.filters = filters;
//	}
//
//
//	@Override
//	public void clean(ServiceRequestResult objRequestResultEntity) {
//		// TODO Auto-generated method stub
//		
//	}
//}
