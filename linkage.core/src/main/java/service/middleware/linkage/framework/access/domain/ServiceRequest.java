package service.middleware.linkage.framework.access.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * this domain class stores the request information when request
 * @author zhonxu
 *
 */
public class ServiceRequest {
	private String serviceName;
	private String methodName;
	private String version;
	private String requestID;
	private List   args;
	private String group;
	
	public ServiceRequest(){
		args = new LinkedList();
	}
	
	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public List<String> getArgs() {
		return args;
	}
	
	public void setArgs(List<String> args) {
		this.args = args;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
}
