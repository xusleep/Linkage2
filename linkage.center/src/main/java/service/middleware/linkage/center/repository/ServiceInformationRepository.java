package service.middleware.linkage.center.repository;

import java.util.LinkedList;
import java.util.List;

import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.utils.StringUtils;

/**
 * client side service list repository
 * @author Smile
 *
 */
public class ServiceInformationRepository {
	private static final List<ServiceInformation> serviceListCache = new LinkedList<ServiceInformation>();
	
	/**
	 * add the service information domain to the repository
	 * @param objServiceInformation
	 */
	public static synchronized void addServiceInformation(ServiceInformation objServiceInformation){
		if(objServiceInformation != null)
			serviceListCache.add(objServiceInformation);
	}
	
	/**
	 * add the service information domain to the repository
	 * @param objServiceInformationList
	 */
	public static synchronized void addServiceInformationList(List<ServiceInformation> objServiceInformationList){
		if(objServiceInformationList != null)
			serviceListCache.addAll(objServiceInformationList);
	}
	
	/**
	 * remove the service information domain from the repository
	 * @param objServiceInformation
	 */
	public static synchronized void removeServiceInformation(ServiceInformation objServiceInformation){
		if(objServiceInformation != null)
			serviceListCache.remove(objServiceInformation);
	}
	
	/**
	 * remove the service information domain list from the repository
	 * @param objServiceInformationList
	 */
	public static synchronized void removeServiceInformationList(List<ServiceInformation> objServiceInformationList){
		if(objServiceInformationList != null)
			serviceListCache.removeAll(objServiceInformationList);
	}
	
	
	/**
	 * clear the service information domain
	 */
	public static synchronized void clear(){
		serviceListCache.clear();
	}
	
	/**
	 * get the service list from the repository by service name
	 * @param serviceName
	 * @return
	 */
	public static synchronized List<ServiceInformation> getServiceInformationList(String serviceName){
		List<ServiceInformation> resultList = new LinkedList<ServiceInformation>();
		if(StringUtils.isEmpty(serviceName))
			return resultList;
		if(serviceListCache == null || serviceListCache.size() == 0)
			return resultList;
		for(ServiceInformation objServiceInformation :serviceListCache)
		{
			if(serviceName.equals(objServiceInformation.getServiceName()))
			{
				resultList.add(objServiceInformation);
			}
		}
		return resultList;
	}
}
