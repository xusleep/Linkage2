package service.middleware.linkage.framework.access.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private List<Object> args;
    private List<Class<?>> argsTyps;
    private String group;

    public ServiceRequest() {
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

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public List<Class<?>> getArgsTyps() {
        return argsTyps;
    }

    public void setArgsTyps(List<Class<?>> argsTyps) {
        this.argsTyps = argsTyps;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
