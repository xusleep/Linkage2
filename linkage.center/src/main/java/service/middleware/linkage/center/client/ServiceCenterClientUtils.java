package service.middleware.linkage.center.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.center.common.ServiceCenterUtils;
import service.middleware.linkage.center.serviceaccess.NIORouteServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.setting.ServiceSettingEntity;
import service.middleware.linkage.framework.setting.reader.ServiceSettingReader;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * this class is used for call from the client side
 * from the service center side, it will call method to request the service of client
 * from the client side, it will call method to request the service of service center
 * @author Smile
 *
 */
public final class ServiceCenterClientUtils {
	private static Logger logger = LoggerFactory.getLogger(ServiceCenterClientUtils.class);

	public static NIORouteServiceAccess defaultRouteConsume = null;
	
	// the service center side
	public static final String SERVICE_CENTER_SERVICE_NAME   				= "serviceCenter";
	public static final String SERVICE_CENTER_REGISTER_ID    				= "serviceCenterRegister";
	public static final String SERVICE_CENTER_UNREGISTER_ID 				= "serviceCenterUnregister";
	public static final String SERVICE_CENTER_GET_SERVICE_ID 				= "serviceCenterGetServiceList";
	public static final String SERVICE_CENTER_REGISTER_CLIENT_ID    		= "serviceCenterRegisterClient";
	
	// the service center client side
	public static final String SERVICE_CENTER_CLIENT_SERVICE_NAME			= "serviceCenterClientService";
	public static final String SERVICE_CENTER_CLIENT_SERVICE_REMOVE 		= "serviceCenterClientServiceRemove";
	public static final String SERVICE_CENTER_CLIENT_SERVICE_ADD 			= "serviceCenterClientServiceAdd";
	
	/**
	 * invoke the client service method
	 * @param clientID
	 * @param serviceInformationList
	 * @throws ServiceException 
	 */
	public static boolean notifyClientServiceAdd(ServiceAccess consume, ServiceInformation clientServiceInformation, List<ServiceInformation> serviceInformationList ) throws ServiceException{
		String strServiceInformation = ServiceCenterUtils.serializeServiceInformationList(serviceInformationList);
		List<String> args = new LinkedList<String>();
		args.add(strServiceInformation);
		ServiceRequestResult result = consume.requestServicePerConnectSync(SERVICE_CENTER_CLIENT_SERVICE_ADD, args, clientServiceInformation);
		if(result.isException()){
			result.getException().printStackTrace();
			throw result.getException();
		}
		return true;
	}
	
	/**
	 * call the service of the service center, 
	 * register the client information to service center,
	 * when the center need to notify the client about the service change,
	 * the center could use the client list which is register from here
	 * @param clientServiceInformation
	 * @return
	 * @throws ServiceException
	 */
	public static boolean registerClientInformation(ServiceAccess consume, ServiceInformation centerServiceInformation,
			ServiceInformation clientServiceInformation) throws ServiceException{
		String strServiceInformation = ServiceCenterUtils.serializeServiceInformation(clientServiceInformation);
		List<String> args = new LinkedList<String>();
		args.add(strServiceInformation);
		ServiceRequestResult result = consume.requestServicePerConnectSync(SERVICE_CENTER_REGISTER_CLIENT_ID, args, centerServiceInformation);
		if(result.isException()){
			result.getException().printStackTrace();
			throw result.getException();
		}
		return true;
	}
	
	/**
	 * this method is used to register the service to service center
	 * @param consumerBean
	 * @param workingServicePropertyEntity
	 * @return
	 * @throws ServiceException
	 */
	public static boolean registerServiceList(ServiceAccess consume, ServiceInformation centerServiceInformation, ServiceSettingReader workingServicePropertyEntity) throws ServiceException{
		List<ServiceInformation> serviceInformationList = new LinkedList<ServiceInformation>();
		
		//��ȡ���з��񣬽�����ע�ᵽע������
		List<ServiceSettingEntity> serviceList = workingServicePropertyEntity.getServiceList();
		for(ServiceSettingEntity serviceEntity: serviceList)
		{
			String interfaceName = serviceEntity.getServiceInterface();
			try {
				Class interfaceclass = Class.forName(interfaceName);
				Method[] methods = interfaceclass.getMethods();
				for(int i = 0; i < methods.length; i++){
					ServiceInformation subServiceInformation = new ServiceInformation();
					subServiceInformation.setAddress(workingServicePropertyEntity.getServiceAddress());
					subServiceInformation.setPort(workingServicePropertyEntity.getServicePort());
					subServiceInformation.setServiceMethod(methods[i].getName());
					subServiceInformation.setServiceName(serviceEntity.getServiceName());
					subServiceInformation.setServiceVersion(serviceEntity.getServiceVersion());
					serviceInformationList.add(subServiceInformation);
					logger.debug("service name : " + serviceEntity.getServiceName() + " method name : " + methods[i].getName());
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		String strServiceInformation = ServiceCenterUtils.serializeServiceInformationList(serviceInformationList);
		List<String> args = new LinkedList<String>();
		args.add(strServiceInformation);
		ServiceRequestResult result = consume.requestServicePerConnectSync(ServiceCenterClientUtils.SERVICE_CENTER_REGISTER_ID, args, centerServiceInformation);
		if(result.isException()){
			result.getException().printStackTrace();
			throw result.getException();
		}
		return true;
	}
	
	
}
