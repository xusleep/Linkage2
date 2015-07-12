package service.middleware.linkage.framework.access.domain;

import java.util.concurrent.CountDownLatch;

import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkingChannelContext;

/**
 * this class stored the request result
 * @author zhonxu
 *
 */
public class ServiceRequestResult {
	private String requestID;
	private ServiceResponse responseEntity;
	private CountDownLatch signal = new CountDownLatch(1);
	private boolean isException;
	private ServiceException exception;
	private WorkingChannelContext workingChannel;
	private ServiceInformation serviceInformation;
	
	public ServiceRequestResult(){
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public ServiceResponse getResponseEntity() {
		try {
			signal.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseEntity;
	}

	public void setResponseEntity(ServiceResponse responseEntity) {
		this.responseEntity = responseEntity;
		signal.countDown();
	}

	public boolean isException() {
		return isException;
	}

	public void setException(boolean isException) {
		this.isException = isException;
	}

	public ServiceException getException() {
		return exception;
	}

	public void setException(ServiceException exception) {
		this.exception = exception;
	}

	public WorkingChannelContext getWorkingChannel() {
		return workingChannel;
	}

	public void setWorkingChannel(WorkingChannelContext workingChannel) {
		this.workingChannel = workingChannel;
	}

	public ServiceInformation getServiceInformation() {
		return serviceInformation;
	}

	public void setServiceInformation(
			ServiceInformation serviceInformation) {
		this.serviceInformation = serviceInformation;
	}
	
	
}
