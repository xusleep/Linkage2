import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//package service.middleware.linkage.center.client;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import service.middleware.linkage.center.common.ServiceCenterUtils;
//import service.middleware.linkage.center.access.impl.NIORouteServiceAccess;
//import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
//import service.middleware.linkage.framework.exception.ServiceException;
//import service.middleware.linkage.framework.access.ServiceAccess;
//import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;
//import service.middleware.linkage.framework.setting.ServiceSettingEntity;
//import service.middleware.linkage.framework.setting.reader.ServiceSettingReader;
//
//import java.lang.reflect.Method;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * this class is used for call from the client side
// * from the service center side, it will call method to request the service of client
// * from the client side, it will call method to request the service of service center
// * @author Smile
// *
// */
//public final class ServiceCenterCommonRepository {
//    private static Logger logger = LoggerFactory.getLogger(ServiceCenterCommonRepository.class);
//
//
//    // the service center side
//    public static final String SERVICE_CENTER_SERVICE_NAME = "serviceCenter";
//    public static final String SERVICE_CENTER_REGISTER_ID = "serviceCenterRegister";
//    public static final String SERVICE_CENTER_UNREGISTER_ID = "serviceCenterUnregister";
//    public static final String SERVICE_CENTER_GET_SERVICE_ID = "serviceCenterGetServiceList";
//    public static final String SERVICE_CENTER_REGISTER_CLIENT_ID = "serviceCenterRegisterClient";
//
//    // the service center client side
//    public static final String SERVICE_CENTER_CLIENT_SERVICE_NAME = "serviceCenterClientService";
//    public static final String SERVICE_CENTER_CLIENT_SERVICE_REMOVE = "serviceCenterClientServiceRemove";
//    public static final String SERVICE_CENTER_CLIENT_SERVICE_ADD = "serviceCenterClientServiceAdd";
//}
//	/**
//	 * invoke the client service method
//	 * @param SERVICE_REGISTER_ENTRY_LIST
//	 * @throws ServiceException
//	 */
//	public static boolean notifyClientServiceAdd(ServiceAccess consume, ServiceRegisterEntry clientServiceInformation, List<ServiceRegisterEntry> SERVICE_REGISTER_ENTRY_LIST ) throws ServiceException{
//		String strServiceInformation = ServiceCenterUtils.serializeServiceInformationList(SERVICE_REGISTER_ENTRY_LIST);
//		List<Object> args = new LinkedList<Object>();
//		args.add(strServiceInformation);
//		List<Class<?>> argTypes = new LinkedList<>();
//		argTypes.add(String.class);
//		ServiceRequestResult result = consume.requestServicePerConnectSync(SERVICE_CENTER_CLIENT_SERVICE_ADD, args, argTypes, clientServiceInformation);
//		if(result.isException()){
//			result.getException().printStackTrace();
//			throw result.getException();
//		}
//		return true;
//	}
//
//	/**
//	 * call the service of the service center,
//	 * register the client information to service center,
//	 * when the center need to notify the client about the service change,
//	 * the center could use the client list which is register from here
//	 * @param clientServiceInformation
//	 * @return
//	 * @throws ServiceException
//	 */
//	public static boolean registerClientInformation(ServiceAccess consume, ServiceRegisterEntry centerServiceInformation,
//			ServiceRegisterEntry clientServiceInformation) throws ServiceException{
//		String strServiceInformation = ServiceCenterUtils.serializeServiceInformation(clientServiceInformation);
//		List<Object> args = new LinkedList<Object>();
//		args.add(strServiceInformation);
//		List<Class<?>> argTypes = new LinkedList<>();
//		argTypes.add(String.class);
//		ServiceRequestResult result = consume.requestServicePerConnectSync(SERVICE_CENTER_REGISTER_CLIENT_ID, args, argTypes, centerServiceInformation);
//		if(result.isException()){
//			result.getException().printStackTrace();
//			throw result.getException();
//		}
//		return true;
//	}
//
//	/**
//	 * this method is used to register the service to service center
//	 * @param workingServicePropertyEntity
//	 * @return
//	 * @throws ServiceException
//	 */
//	public static boolean registerServiceList(ServiceAccess consume, ServiceRegisterEntry centerServiceInformation, ServiceSettingReader workingServicePropertyEntity) throws ServiceException{
//		List<ServiceRegisterEntry> SERVICE_REGISTER_ENTRY_LIST = new LinkedList<ServiceRegisterEntry>();
//
//		//��ȡ���з��񣬽�����ע�ᵽע������
//		List<ServiceSettingEntity> serviceList = workingServicePropertyEntity.getServiceList();
//		for(ServiceSettingEntity serviceEntity: serviceList)
//		{
//			String interfaceName = serviceEntity.getServiceInterface();
//			try {
//				Class interfaceclass = Class.forName(interfaceName);
//				Method[] methods = interfaceclass.getMethods();
//				for(int i = 0; i < methods.length; i++){
//					ServiceRegisterEntry subServiceInformation = new ServiceRegisterEntry();
//					subServiceInformation.setAddress(workingServicePropertyEntity.getServiceAddress());
//					subServiceInformation.setPort(workingServicePropertyEntity.getServicePort());
//					subServiceInformation.setServiceMethod(methods[i].getParameterName());
//					subServiceInformation.setServiceName(serviceEntity.getServiceName());
//					subServiceInformation.setServiceVersion(serviceEntity.getServiceVersion());
//					SERVICE_REGISTER_ENTRY_LIST.add(subServiceInformation);
//					logger.debug("service name : " + serviceEntity.getServiceName() + " method name : " + methods[i].getParameterName());
//				}
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//
//		}
//		String strServiceInformation = ServiceCenterUtils.serializeServiceInformationList(SERVICE_REGISTER_ENTRY_LIST);
//		List<Object> args = new LinkedList<>();
//		args.add(strServiceInformation);
//		List<Class<?>> argTypes = new LinkedList<>();
//		argTypes.add(String.class);
//		ServiceRequestResult result = consume.requestServicePerConnectSync(ServiceCenterCommonRepository.SERVICE_CENTER_REGISTER_ID, args, argTypes, centerServiceInformation);
//		if(result.isException()){
//			result.getException().printStackTrace();
//			throw result.getException();
//		}
//		return true;
//	}
//
//
//}
