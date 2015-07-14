//package clould.storage.run;
//
//import clould.storage.service.handler.FileHandler;
//import service.middleware.linkage.center.bootstrap.NIOCenterClientBootStrap;
//import service.middleware.linkage.center.client.ServiceCenterClientUtils;
//import service.middleware.linkage.framework.access.domain.ServiceInformation;
//import service.middleware.linkage.framework.bootstrap.NIOServerBootStrap;
//
//
///**
// *
// * @author Smile
// *
// */
//public class StartStorageService {
//
//    public static void main(String[] args) {
//        try {
//        	NIOServerBootStrap serviceBootStrap = new NIOServerBootStrap("conf/service_server.properties", 5);
//        	ServiceInformation centerServiceInformation = new ServiceInformation();
//        	centerServiceInformation.setAddress("localhost");
//        	centerServiceInformation.setPort(5002);
//        	NIOCenterClientBootStrap clientBootStrap = new NIOCenterClientBootStrap("conf/service_client.properties",
//        			5, centerServiceInformation);
//    		serviceBootStrap.run();
//    		clientBootStrap.run();
//    		clientBootStrap.getEventDistributionMaster().addHandler(new FileHandler());
//    		ServiceCenterClientUtils.defaultRouteConsume = clientBootStrap.getServiceAccess();
//    		try {
//    			ServiceCenterClientUtils.registerServiceList(ServiceCenterClientUtils.defaultRouteConsume, centerServiceInformation, serviceBootStrap.getServicePropertyEntity());
//    		}
//    		catch (Exception e) {
//
//    		}
//        }
//        catch (Exception e) {
//        	e.printStackTrace();
//            System.out.println("Server error: " + e.getMessage());
//        }
//
//    }
//}
