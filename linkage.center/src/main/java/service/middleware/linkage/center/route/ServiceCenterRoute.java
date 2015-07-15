package service.middleware.linkage.center.route;

import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;

/**
 * Created by Smile on 2015/7/15.
 */
public interface ServiceCenterRoute {
    public ServiceRegisterEntry chooseRouteByServiceName(String serviceName);
}
