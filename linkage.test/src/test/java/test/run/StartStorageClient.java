//package test.run;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import service.middleware.linkage.center.bootstrap.NIOCenterClientBootStrap;
//import service.middleware.linkage.center.serviceaccess.NIORouteServiceAccess;
//import service.middleware.linkage.framework.access.domain.ServiceInformation;
//import service.middleware.linkage.framework.access.domain.ServiceRequestResult;
//import service.middleware.linkage.framework.bootstrap.NIOServerBootStrap;
//
///**
// *
// * @author zhonxu
// *
// */
//public class StartStorageClient {
//
//	public static void main(String[] args) throws Exception {
//    	ServiceInformation centerServiceInformation = new ServiceInformation();
//    	centerServiceInformation.setAddress("localhost");
//    	centerServiceInformation.setPort(5002);
//		final NIOCenterClientBootStrap clientBootStrap = new NIOCenterClientBootStrap("conf/client_client.properties", 5, centerServiceInformation);
//		clientBootStrap.run();
//    	NIOServerBootStrap serviceBootStrap = new NIOServerBootStrap("conf/client_server.properties", 5);
//    	serviceBootStrap.run();
//    	NIORouteServiceAccess cb = clientBootStrap.getServiceAccess();
//		List<String> args1 = new LinkedList<String>();
//		args1.add("1.jar");
//		ServiceRequestResult result = clientBootStrap.getServiceAccess().requestServicePerConnectSync("storage", args1);
//		System.out.println("result is : " + result.getResponseEntity().getResult());
//		//clientBootStrap.shutdownImediate();
//		//serviceBootStrap.shutdownImediate();
//	}
//}
