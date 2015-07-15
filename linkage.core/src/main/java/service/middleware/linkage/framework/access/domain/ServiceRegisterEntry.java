package service.middleware.linkage.framework.access.domain;

/**
 * this class use to hold the information of a service
 * @author zhonxu
 *
 */
public class ServiceRegisterEntry {
	private final String id;
	private final String serviceName;
	private final String serviceMethod;
	private final String address;
	private final String serviceVersion;
	private final int    port;
	
	public ServiceRegisterEntry(String id, String address, int port, String serviceName, String serviceMethod, String serviceVersion)
	{
		this.id = id;
		this.address = address;
		this.port = port;
		this.serviceName = serviceName;
		this.serviceMethod = serviceMethod;
		this.serviceVersion = serviceVersion;
	}

	public String getId() {
		return id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceMethod() {
		return serviceMethod;
	}

	public String getAddress() {
		return address;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public int getPort() {
		return port;
	}

	public String getNetKey(){
		return address + "_" + port;
	}
}
