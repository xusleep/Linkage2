package clould.storage.service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.handlers.Handler;
import service.middleware.linkage.framework.handlers.ServiceEvent;
import service.middleware.linkage.framework.io.nio.strategy.mixed.events.ServiceOnFileDataReceivedEvent;

import java.io.IOException;

public class FileHandler extends Handler {
	private static Logger logger = LoggerFactory.getLogger(FileHandler.class);
	
	@Override
	public void handleRequest(ServiceEvent event) throws IOException, Exception {
		// TODO Auto-generated method stub
		if(event instanceof ServiceOnFileDataReceivedEvent){
			ServiceOnFileDataReceivedEvent objServerOnFileDataReceivedEvent = (ServiceOnFileDataReceivedEvent)event;
			logger.debug("FileHandler receive message : " + objServerOnFileDataReceivedEvent.getFileID());
		}
	}

}
