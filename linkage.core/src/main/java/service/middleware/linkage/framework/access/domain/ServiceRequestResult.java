package service.middleware.linkage.framework.access.domain;

/**
 * this class stored the request result
 *
 * @author zhonxu
 */
public class ServiceRequestResult {
    private final String requestID;
    private ServiceResponse responseEntity;

    public ServiceRequestResult(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestID() {
        return requestID;
    }

    public ServiceResponse getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ServiceResponse responseEntity) {
        this.responseEntity = responseEntity;
    }
}
