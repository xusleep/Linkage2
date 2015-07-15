package service.middleware.linkage.framework.access.domain;

/**
 * this class stores the information which response from the server
 * @author zhonxu
 *
 */
public class ServiceResponse {
	private String requestID;
	private String jsonResult;

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	
}
