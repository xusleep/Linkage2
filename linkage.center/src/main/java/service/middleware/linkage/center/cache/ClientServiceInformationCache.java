package service.middleware.linkage.center.cache;

import java.util.LinkedList;
import java.util.List;

import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.utils.StringUtils;

/**
 * 
 * @author Smile
 *
 */
public class ClientServiceInformationCache {
	private static final List<ServiceInformation> serviceListCache = new LinkedList<ServiceInformation>();
	
	/**
	 * add the service information domain to the cache
	 * @param objServiceInformation
	 */
	public static synchronized void addServiceInformationEntity(ServiceInformation objServiceInformation){
		if(objServiceInformation != null)
			serviceListCache.add(objServiceInformation);
	}
	
	/**
	 * add the service information domain to the cache
	 * @param objServiceInformationEntity
	 */
	public static synchronized void addServiceInformationEntityList(List<ServiceInformation> objServiceInformationList){
		if(objServiceInformationList != null)
			serviceListCache.addAll(objServiceInformationList);
	}
	
	/**
	 * remove the service information domain from the cache
	 * @param objServiceInformation
	 */
	public static synchronized void removeServiceInformationEntity(ServiceInformation objServiceInformation){
		if(objServiceInformation != null)
			serviceListCache.remove(objServiceInformation);
	}
	
	/**
	 * remove the service information domain list from the cache
	 * @param objServiceInformationEntity
	 */
	public static synchronized void removeServiceInformationEntityList(List<ServiceInformation> objServiceInformationList){
		if(objServiceInformationList != null)
			serviceListCache.removeAll(objServiceInformationList);
	}
	
	
	/**
	 * clear the service information domain
	 */
	public static synchronized void clearServiceInformationEntity(){
		serviceListCache.clear();
	}
	
	/**
	 * get the service list from the cache by service name
	 * @param serviceName
	 * @return
	 */
	public static synchronized List<ServiceInformation> getServiceInformationEntityList(String serviceName){
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
