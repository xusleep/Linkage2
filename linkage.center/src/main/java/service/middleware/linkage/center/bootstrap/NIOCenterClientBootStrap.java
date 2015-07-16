package service.middleware.linkage.center.bootstrap;

import service.middleware.linkage.center.access.RouteServiceAccess;
import service.middleware.linkage.center.access.impl.NIORouteServiceAccess;
import service.middleware.linkage.center.route.impl.DefaultServiceCenterRoute;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.impl.ServiceAccessImpl;
import service.middleware.linkage.framework.bootstrap.AbstractBootStrap;
import service.middleware.linkage.framework.handlers.AccessClientHandler;
import service.middleware.linkage.framework.handlers.DefaultEventDistributionMaster;
import service.middleware.linkage.framework.io.Client;
import service.middleware.linkage.framework.io.nio.NIOClient;
import service.middleware.linkage.framework.io.nio.connection.NIOConnectionManager;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.route.impl.RandomMultiConnectionRoute;
import service.middleware.linkage.framework.setting.reader.ClientSettingPropertyReader;
import service.middleware.linkage.framework.setting.reader.ClientSettingReader;

import java.io.IOException;

/**
 * client side boot strap
 * it will init a worker pool and a distribution master
 *
 * @author zhonxu
 */
public class NIOCenterClientBootStrap extends AbstractBootStrap implements Runnable {
    private final Client client;
    private final RouteServiceAccess serviceAccess;

    /**
     * @param propertyPath             the property configured for the client
     * @param clientTaskThreadPootSize the client
     */
    public NIOCenterClientBootStrap(String propertyPath, int clientTaskThreadPootSize, String centerAddress, int centerPort) {
        super(new DefaultEventDistributionMaster(clientTaskThreadPootSize));
        // read the configuration from the properties
        ClientSettingReader objServicePropertyEntity = null;
        try {
            objServicePropertyEntity = new ClientSettingPropertyReader(propertyPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // this is a client, in this client it will be a gather place where we will start the worker pool & task handler
        this.client = new NIOClient(this.getWorkerPool());
        NIOConnectionManager nioConnectionManager = new NIOConnectionManager(new RandomMultiConnectionRoute(), WorkingChannelMode.MESSAGE, this.getWorkerPool());
        ServiceAccess serviceAccess = new ServiceAccessImpl(objServicePropertyEntity, nioConnectionManager);
        this.serviceAccess = new NIORouteServiceAccess(serviceAccess, new DefaultServiceCenterRoute(serviceAccess, centerAddress, centerPort));
        this.getWorkerPool().getEventDistributionHandler().addHandler(new AccessClientHandler());
    }

    /**
     * the user can use this cosumer bean to request service
     * from the client
     *
     * @return
     */
    public RouteServiceAccess getServiceAccess() {
        return serviceAccess;
    }


    @Override
    public void run() {
        new Thread(client).start();
    }

    /**
     * shutdown
     */
    public void shutdown() {
        client.shutdown();
    }
}
