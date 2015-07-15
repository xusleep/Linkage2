package service.middleware.linkage.framework.access;

import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;

import java.util.List;

public interface ServiceAccess {


    /**
     * request
     *
     * @param address
     * @param port
     * @param clientID
     * @param parameters
     * @param requestCallback
     */
    public void requestService(String address, int port, String clientID, List<ServiceParameter> parameters,
                               RequestCallback requestCallback);

    /**
     *
     * @param clientID
     * @param parameters
     * @return
     */
    public ServiceRequest createRequestEntity(String clientID, List<ServiceParameter> parameters);
}
