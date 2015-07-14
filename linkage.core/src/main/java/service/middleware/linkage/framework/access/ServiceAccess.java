package service.middleware.linkage.framework.access;

import service.middleware.linkage.framework.access.domain.ServiceInformation;

import java.util.List;

public interface ServiceAccess {


	/**
	 * request
	 * @param address
	 * @param port
	 * @param clientID
	 * @param args
	 * @param requestCallback
	 */
	public void requestService(String address, int port, String clientID, List<Object> args,
							   RequestCallback requestCallback);
}
