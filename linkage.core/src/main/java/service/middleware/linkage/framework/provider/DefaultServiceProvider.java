package service.middleware.linkage.framework.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceResponse;
import service.middleware.linkage.framework.setting.ServiceSettingEntity;
import service.middleware.linkage.framework.setting.reader.ServiceSettingReader;
import service.middleware.linkage.framework.utils.StringUtils;

import java.lang.reflect.Method;

public class DefaultServiceProvider implements ServiceProvider{
	private final ServiceSettingReader servicePropertyEntity;
	private static Logger logger = LoggerFactory.getLogger(DefaultServiceProvider.class);
	
	public DefaultServiceProvider(ServiceSettingReader servicePropertyEntity){
		this.servicePropertyEntity = servicePropertyEntity;
	}
	
	/**
	 * search the service from the service list
	 * @param serviceName
	 * @return
	 */
	private ServiceSettingEntity searchService(String serviceName){
		for(ServiceSettingEntity entity : servicePropertyEntity.getServiceList()){
			if(entity.getServiceName().equals(serviceName)){
				return entity;
			}
		}
		return null;
	}
	
	/**
	 * process the request from the client
	 * @param request
	 * @return
	 */
	public ServiceResponse acceptServiceRequest(ServiceRequest request){
		ServiceSettingEntity entity = searchService(request.getServiceName());
		if(entity == null){
			ServiceResponse response = new ServiceResponse();
			response.setResult("Can not find the service name of " + request.getServiceName());
			response.setRequestID(request.getRequestID());
			return response;
		}
		try {
			Class clazz = Class.forName(entity.getServiceInterface());
			Method[] methods = clazz.getMethods();
			Method findMethod = null;
			for(Method method : methods){
				if(method.getName().equals(request.getMethodName())){
					findMethod = method;
					break;
				}
			}
			if(findMethod != null)
			{
				Object result = findMethod.invoke(entity.getServiceTargetObject(), request.getArgs().toArray());
				ServiceResponse response = new ServiceResponse();
				response.setResult(result.toString());
				response.setRequestID(request.getRequestID());
				return response;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("unexpected exception happened. exception detail:" + StringUtils.ExceptionStackTraceToString(e));
			ServiceResponse response = new ServiceResponse();
			response.setResult("no service found in the server.");
			response.setRequestID(request.getRequestID());
			return response;
		} 
		return new ServiceResponse();
	}
}
