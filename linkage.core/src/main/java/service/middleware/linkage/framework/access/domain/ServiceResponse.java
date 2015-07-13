package service.middleware.linkage.framework.access.domain;

/**
 * this class stores the information which response from the server
 * @author zhonxu
 *
 */
public class ServiceResponse {
	private String requestID;
	private Object result;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	
}
