package service.middleware.linkage.framework.io.nio.strategy.mixed.events;

import service.middleware.linkage.framework.handlers.ServiceEvent;
import service.middleware.linkage.framework.io.WorkingChannelContext;

public class ServiceOnMessageDataReceivedEvent implements ServiceEvent {
	private String messageData;
	private WorkingChannelContext workingChannel;
	
	public ServiceOnMessageDataReceivedEvent(WorkingChannelContext workingChannel, String messageData){
		this.workingChannel = workingChannel;
		this.messageData = messageData;
	} 

	public String getMessageData() {
		return messageData;
	}

	public void setMessageData(String data) {
		this.messageData = data;
	}

	public WorkingChannelContext getWorkingChannel() {
		return workingChannel;
	}

	public void setWorkingChannel(WorkingChannelContext workingChannel) {
		this.workingChannel = workingChannel;
	}
	
	
}
