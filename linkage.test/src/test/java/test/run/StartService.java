package test.run;
import service.middleware.linkage.center.bootstrap.NIOCenterClientBootStrap;
import service.middleware.linkage.center.client.ServiceCenterClientUtils;
import service.middleware.linkage.framework.access.domain.ServiceInformation;
import service.middleware.linkage.framework.bootstrap.NIOMessageModeServerBootStrap;


/**
 * 
 * @author Smile
 *
 */
public class StartService {

    public static void main(String[] args) {
        try {
        	NIOMessageModeServerBootStrap serviceBootStrap = new NIOMessageModeServerBootStrap("conf/service_server.properties", 5);
        	ServiceInformation centerServiceInformation = new ServiceInformation();
        	centerServiceInformation.setAddress("localhost");
        	centerServiceInformation.setPort(5002);
        	NIOCenterClientBootStrap clientBootStrap = new NIOCenterClientBootStrap("conf/service_client.properties", 
        			5, centerServiceInformation);
    		serviceBootStrap.run();
    		clientBootStrap.run();
    		//FileInformationEntity fileInformationEntity = new FileInformationEntity();
    		//fileInformationEntity.setFileID(1000);
    		//fileInformationEntity.setFilePath("E:\\testworkingfolder\\downloadClient\\1.mkv");
    		//NIOMixedStrategy.addFileInformationEntity(fileInformationEntity);
    		ServiceCenterClientUtils.defaultRouteConsume = clientBootStrap.getServiceAccess();
    		try {
    			ServiceCenterClientUtils.registerServiceList(ServiceCenterClientUtils.defaultRouteConsume, centerServiceInformation, serviceBootStrap.getServicePropertyEntity());
    		}
    		catch (Exception e) {
    			
    		}
    		
    		//Thread.sleep(1000);
    		//serviceBootStrap.shutdownImediate();
    		//clientBootStrap.shutdownImediate();
        }
        catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Server error: " + e.getMessage());
        }
 
    }
}
