package service.middleware.linkage.center.route.filters;

import java.util.List;

import service.middleware.linkage.framework.access.domain.ServiceInformation;

public interface RouteFilter {
	public List<ServiceInformation> filter(List<ServiceInformation> serviceList);
}
