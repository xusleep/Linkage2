package service.middleware.linkage.center;

import java.util.LinkedList;
import java.util.List;

import service.middleware.linkage.center.client.ServiceCenterClientUtils;
import service.middleware.linkage.center.common.ServiceCenterUtils;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.exception.ServiceException;

/**
 * �ṩ������Service��������ע��ʹ��
 * @author Smile
 *
 */
public class ServiceCenterImpl implements ServiceCenter {

	@Override
	public Boolean register(String serviceInfor) {
		// TODO Auto-generated method stub
		List<ServiceInformation> objServiceInformation = ServiceCenterUtils.deserializeServiceInformationList(serviceInfor);
		synchronized(ServiceCenter.serviceInformationList)
		{
			ServiceCenter.serviceInformationList.addAll(objServiceInformation);
		}
		for(ServiceInformation clientServiceInformation : ServiceCenter.serviceClientList)
		{
			try {
				ServiceCenterClientUtils.notifyClientServiceAdd(ServiceCenterClientUtils.defaultRouteConsume, clientServiceInformation, objServiceInformation);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("register service count = " + ServiceCenter.serviceInformationList.size());
		return true;
	}
	
	@Override
	public Boolean registerClientList(String serviceInfor) {
		ServiceInformation objServiceInformation = ServiceCenterUtils.deserializeServiceInformation(serviceInfor);
		ServiceCenter.serviceClientList.add(objServiceInformation);
		System.out.println("serviceInfor = " + serviceInfor);
		System.out.println("service client count = " + ServiceCenter.serviceInformationList.size());
		return true;
	}

	@Override
	public List<ServiceInformation> getServiceList(String serviceName) {
		List<ServiceInformation> resultList = new LinkedList<ServiceInformation>();
		for(ServiceInformation objServiceInformation : ServiceCenter.serviceInformationList){
			if(objServiceInformation.getServiceName().equals(serviceName))
			{
				resultList.add(objServiceInformation);
			}
		}
		return resultList;
	}
	/**
	 * ɾ�������б�
	 */
	public Boolean removeServiceList(String serviceName){
		List<ServiceInformation> resultList = new LinkedList<ServiceInformation>();
		synchronized(ServiceCenter.serviceInformationList)
		{
			for(ServiceInformation objServiceInformation : ServiceCenter.serviceInformationList){
				if(objServiceInformation.getServiceName().equals(serviceName))
				{
					resultList.add(objServiceInformation);
				}
			}
			ServiceCenter.serviceInformationList.removeAll(resultList);
		}
		return true;
	}

}
