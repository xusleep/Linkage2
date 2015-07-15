//package service.middleware.linkage.center;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import service.middleware.linkage.center.client.ServiceCenterCommonRepository;
//import service.middleware.linkage.center.common.ServiceCenterUtils;
//import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;
//import service.middleware.linkage.framework.exception.ServiceException;
//
///**
// * �ṩ������Service��������ע��ʹ��
// * @author Smile
// *
// */
//public class ServiceCenterImpl implements ServiceCenter {
//
//	@Override
//	public Boolean register(String serviceInfor) {
//		// TODO Auto-generated method stub
//		List<ServiceRegisterEntry> objServiceInformation = ServiceCenterUtils.deserializeServiceInformationList(serviceInfor);
//		synchronized(ServiceCenter.SERVICE_REGISTER_ENTRY_LIST)
//		{
//			ServiceCenter.SERVICE_REGISTER_ENTRY_LIST.addAll(objServiceInformation);
//		}
//		for(ServiceRegisterEntry clientServiceInformation : ServiceCenter.serviceClientList)
//		{
//			try {
//				ServiceCenterCommonRepository.notifyClientServiceAdd(ServiceCenterCommonRepository.defaultRouteConsume, clientServiceInformation, objServiceInformation);
//			} catch (ServiceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println("register service count = " + ServiceCenter.SERVICE_REGISTER_ENTRY_LIST.size());
//		return true;
//	}
//
//	@Override
//	public Boolean registerClientList(String serviceInfor) {
//		ServiceRegisterEntry objServiceInformation = ServiceCenterUtils.deserializeServiceInformation(serviceInfor);
//		ServiceCenter.serviceClientList.add(objServiceInformation);
//		System.out.println("serviceInfor = " + serviceInfor);
//		System.out.println("service client count = " + ServiceCenter.SERVICE_REGISTER_ENTRY_LIST.size());
//		return true;
//	}
//
//	@Override
//	public List<ServiceRegisterEntry> getServiceList(String serviceName) {
//		List<ServiceRegisterEntry> resultList = new LinkedList<ServiceRegisterEntry>();
//		for(ServiceRegisterEntry objServiceInformation : ServiceCenter.SERVICE_REGISTER_ENTRY_LIST){
//			if(objServiceInformation.getServiceName().equals(serviceName))
//			{
//				resultList.add(objServiceInformation);
//			}
//		}
//		return resultList;
//	}
//	/**
//	 * ɾ�������б�
//	 */
//	public Boolean removeServiceList(String serviceName){
//		List<ServiceRegisterEntry> resultList = new LinkedList<ServiceRegisterEntry>();
//		synchronized(ServiceCenter.SERVICE_REGISTER_ENTRY_LIST)
//		{
//			for(ServiceRegisterEntry objServiceInformation : ServiceCenter.SERVICE_REGISTER_ENTRY_LIST){
//				if(objServiceInformation.getServiceName().equals(serviceName))
//				{
//					resultList.add(objServiceInformation);
//				}
//			}
//			ServiceCenter.SERVICE_REGISTER_ENTRY_LIST.removeAll(resultList);
//		}
//		return true;
//	}
//
//}
