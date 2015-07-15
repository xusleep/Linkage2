package service.middleware.linkage.framework.access.domain;

import linkage.common.CommonUtils;

import java.util.List;

/**
 * this domain class stores the request information when request
 *
 * @author zhonxu
 */
public class ServiceRequest {
    private String serviceName;
    private String methodName;
    private String version;
    private final String requestID;
    private List<ServiceParameter> parameters;
    private String group;

    public ServiceRequest() {
        this.requestID = CommonUtils.generateId();
    }

    public List<ServiceParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ServiceParameter> parameters) {
        this.parameters = parameters;
    }

    public String getRequestID() {
        return requestID;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
