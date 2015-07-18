package service.middleware.linkage.center.access.impl;

import service.middleware.linkage.center.access.RouteServiceAccess;
import service.middleware.linkage.center.route.ServiceCenterRoute;
import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;
import service.middleware.linkage.framework.access.domain.ServiceRequest;

import java.util.List;

public class NIORouteServiceAccess implements RouteServiceAccess {
    private final ServiceAccess serviceAccess;
    private final ServiceCenterRoute serviceCenterRoute;

    public NIORouteServiceAccess(ServiceAccess serviceAccess, ServiceCenterRoute serviceCenterRoute) {
        this.serviceAccess = serviceAccess;
        this.serviceCenterRoute = serviceCenterRoute;
    }

    @Override
    public void requestService(String clientID, List<ServiceParameter> parameters, RequestCallback requestCallback) {
        ServiceRequest serviceRequest = serviceAccess.createRequestEntity(clientID, parameters);
        ServiceRegisterEntry serviceRegisterEntry = serviceCenterRoute.chooseRouteByServiceName(serviceRequest.getServiceName());
        if(serviceRegisterEntry == null)
        {
            requestCallback.runException(new Exception("cannot find the service from the service center."));
        }
        serviceAccess.requestService(serviceRegisterEntry.getAddress(), serviceRegisterEntry.getPort(), clientID, parameters, requestCallback);
    }
}
