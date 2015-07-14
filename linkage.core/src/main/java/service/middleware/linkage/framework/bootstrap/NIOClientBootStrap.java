package service.middleware.linkage.framework.bootstrap;

import java.io.IOException;

import service.middleware.linkage.framework.handlers.AccessClientHandler;
import service.middleware.linkage.framework.handlers.DefaultEventDistributionMaster;
import service.middleware.linkage.framework.io.Client;
import service.middleware.linkage.framework.io.nio.NIOClient;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.impl.ServiceAccessImpl;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.connection.NIOConnectionManager;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.repository.WorkingChannelRepository;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.setting.reader.ClientSettingPropertyReader;
import service.middleware.linkage.framework.setting.reader.ClientSettingReader;

/**
 * client side boot strap
 * it will init a worker pool and a distribution master
 * @author zhonxu
 *
 */
public class NIOClientBootStrap extends AbstractBootStrap implements Runnable {
	private final Client client;
	private final ServiceAccess serviceAccess;
	
	/**
	 * 
	 * @param propertyPath the property configured for the client
	 * @param clientTaskThreadPootSize the client 
	 */
	public NIOClientBootStrap(String serviceAddress, int servicePort, String propertyPath, int clientTaskThreadPootSize){
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
		this.serviceAccess = new ServiceAccessImpl(objServicePropertyEntity);
		this.getWorkerPool().getEventDistributionHandler().addHandler(new AccessClientHandler());

		try {
			NIOWorkingChannelContext nioWorkingChannelContext = NIOConnectionManager.createNIOWorkingChannel(serviceAddress, servicePort, WorkingChannelMode.MESSAGE, this.getWorkerPool());
			WorkingChannelRepository.addWorkingChannelStoreBean(new WorkingChannelStoreBean(nioWorkingChannelContext));
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * the user can use this cosumer bean to request service
	 * from the client
	 * @return
	 */
	public ServiceAccess getServiceAccess() {
		return serviceAccess;
	}

	@Override
	public void run() {
		new Thread(client).start();
	}
	
	/**
	 * shutdown 
	 */
	public void shutdown()
	{
		client.shutdown();
	}
}
