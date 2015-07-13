package service.middleware.linkage.center.serviceaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.center.route.Route;
import service.middleware.linkage.center.route.ServiceCenterRoute;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.access.impl.ServiceAccessEngineImpl;
import service.middleware.linkage.framework.access.impl.ServiceAccessImpl;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.setting.reader.ClientSettingReader;
import service.middleware.linkage.framework.utils.StringUtils;

import java.util.List;

public class NIORouteServiceAccess extends ServiceAccessImpl implements RouteServiceAccess {
	private final Route route;
	private static Logger logger = LoggerFactory.getLogger(NIORouteServiceAccess.class);
	public NIORouteServiceAccess(ClientSettingReader workingClientPropertyEntity, WorkerPool workerPool, ServiceInformation centerServiceInformation) {
		super(workingClientPropertyEntity, workerPool);
		this.route = new ServiceCenterRoute(centerServiceInformation, this);
	}
	
	@Override
	public ServiceRequestResult requestService(String clientID, List<String> args) {
		return requestService(clientID, args, true);
	}

	@Override
	public ServiceRequestResult requestService(String clientID,
			List<String> args, boolean channelFromCached) {
		final ServiceRequest objRequestEntity = serviceEngine.createRequestEntity(clientID, args);
        ServiceRequestResult result = new ServiceRequestResult();
        result.setRequestID(objRequestEntity.getRequestID());
    	// Find the service information from the route, set the information into the result domain as well
		ServiceInformation serviceInformation = null;
		try {
			serviceInformation = route.chooseRoute(objRequestEntity);
			result.setServiceInformation(serviceInformation);
			if(serviceInformation == null)
			{
				ServiceAccessEngineImpl.setExceptionToRuquestResult(result, new ServiceException(new Exception("Can not find the service"), "Can not find the service"));
				return result;
			}
		} 
		catch(Exception ex)
		{
			logger.error(StringUtils.ExceptionStackTraceToString(ex));
			//logger.log(Level.WARNING, ex.getMessage());
			//System.out.println("ComsumerBean ... exception happend " + ex.getMessage());
			//ex.printStackTrace();
			ServiceAccessEngineImpl.setExceptionToRuquestResult(result, new ServiceException(ex, "ComsumerBean ... exception happend"));
        	route.clean(result);
        	return result;
        }
		return serviceEngine.basicProcessRequest(objRequestEntity, result, serviceInformation, channelFromCached);
	}

	@Override
	public ServiceRequestResult requestServicePerConnect(String clientID,
			List<String> args) {
		return requestService(clientID, args, false);
	}

	@Override
	public ServiceRequestResult requestServicePerConnectSync(String clientID,
			List<String> args) {
		ServiceRequestResult result = requestService(clientID, args, false);
		result.getResponseEntity();
		this.closeChannelByRequestResult(result);
		return result;
	}
}