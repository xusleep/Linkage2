package service.middleware.linkage.framework.route;

import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;

/**
 *  this is interface for the route,
 *  the client will call a route to get the service list and choose the service
 * @author zhonxu
 *
 */
public interface MultiConnectionRoute {
	public WorkingChannelStoreBean chooseRoute(String address, int port);
}
