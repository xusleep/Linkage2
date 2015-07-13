package service.middleware.linkage.center.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.center.ServiceCenter;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.utils.ShareingData;
import service.middleware.linkage.framework.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class HeartBeatSender implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(HeartBeatSender.class);
	private final ServiceAccess consume;
	private volatile boolean isShutdown = false;
	
	public HeartBeatSender(ServiceAccess consume){
		this.consume = consume;
	}
	
	/**
	 * shutdown 
	 */
	public void shutdown()
	{
		logger.debug("shutdown heat beat sender.");
		isShutdown = true;
	}
	
	@Override
	public void run() {
		while(true){
			synchronized(ServiceCenter.serviceInformationList)
			{
				List<ServiceInformation> failedServiceInformationList = new LinkedList<ServiceInformation>();
				for(ServiceInformation objServiceInformation : ServiceCenter.serviceInformationList)
				{
					// TODO Auto-generated method stub
					List<Object> args = new LinkedList<Object>();
					args.add(ShareingData.HEART_BEAT_SEND);
					List<Class<?>> argTypes = new LinkedList<>();
					argTypes.add(String.class);
					args.add(ShareingData.HEART_BEAT_SEND);
					ServiceRequestResult result = this.consume.requestServicePerConnectSync(ShareingData.HEART_BEAT_CLIENT_ID, args, argTypes, objServiceInformation);
					if(ShareingData.HEART_BEAT_REPLY.equals(result.getResponseEntity().getResult())){
						logger.debug("service :" + objServiceInformation.toString() + " is available");
					}
					// if there is an exception when request the heart beat to the service
					// we have to remove the service from the service list
					if(result.isException())
					{
						logger.debug("service :" + objServiceInformation.toString() + " no service found ...");
	
						logger.error("HeartBeatSender unexpected exception happned ..." + StringUtils.ExceptionStackTraceToString(result.getException()));
						if(result.getServiceInformation() != null)
						{
							logger.debug("failed request information : " + result.getServiceInformation().toString());
							logger.debug("will remove information : " + result.getServiceInformation().toString());
							failedServiceInformationList.add(result.getServiceInformation());
						}
					}
					else
					{
						logger.debug("sucessfull request information : " + result.getServiceInformation().toDetailString());
					}
				}
				ServiceCenter.serviceInformationList.removeAll(failedServiceInformationList);
			}
			try {
				Thread.sleep(2000);
				logger.debug("Sleeping for 2 seconds and loop the service again." );
				if(isShutdown)
					break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
