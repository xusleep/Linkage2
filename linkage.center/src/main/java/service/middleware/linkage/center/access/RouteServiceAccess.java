package service.middleware.linkage.center.access;

import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.domain.ServiceParameter;

import java.util.List;

public interface RouteServiceAccess {
	/**
	 * request directly using the service information domain
	 * @param clientID
	 * @param parameters
	 * @param requestCallback
	 */
	public void requestService(String clientID, List<ServiceParameter> parameters,
							   RequestCallback requestCallback);
}
