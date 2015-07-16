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
    private String requestID;
    private String group;
    private List<ServiceParameter> serviceParameters;

    public ServiceRequest() {

    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
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

    public List<ServiceParameter> getServiceParameters() {
        return serviceParameters;
    }

    public void setServiceParameters(List<ServiceParameter> serviceParameters) {
        this.serviceParameters = serviceParameters;
    }
}
