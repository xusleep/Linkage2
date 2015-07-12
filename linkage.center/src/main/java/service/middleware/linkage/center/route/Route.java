package service.middleware.linkage.center.route;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import service.middleware.linkage.center.clean.Cleaner;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
/**
 *  this is interface for the route,
 *  the client will call a route to get the service list and choose the service
 * @author zhonxu
 *
 */
public interface Route extends Cleaner{
	public ServiceInformation chooseRoute(ServiceRequest requestEntity) throws IOException, InterruptedException, ExecutionException, Exception;
}
