package service.middleware.linkage.center.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.utils.ShareingData;

public class DefaultHeartBeatReceiver implements HeartBeatReceiver {
	private static Logger logger = LoggerFactory.getLogger(DefaultHeartBeatReceiver.class);
	@Override
	public String receive(String msg) {
		logger.debug("DefaultHeartBeatReceiver.receive " + msg);
		if(ShareingData.HEART_BEAT_SEND.equals(msg))
		{
			return ShareingData.HEART_BEAT_REPLY;
		}
		return ShareingData.HEART_BEAT_ERROR;
	}

}
