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
	 * @param argType
	 * @param requestCallback
	 */
	public void requestService(String address, int port, String clientID, List<Object> args, List<Class<?>> argType,
							   RequestCallback requestCallback);
}
