package test.run.withoutcenter;

import service.middleware.linkage.framework.bootstrap.NIOServerBootStrap;


/**
 * 
 * @author Smile
 *
 */
public class StartService {

    public static void main(String[] args) {
        try {
        	NIOServerBootStrap serviceBootStrap = new NIOServerBootStrap("conf/service_server.properties", 5);
    		serviceBootStrap.run();
        }
        catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Server error: " + e.getMessage());
        }
 
    }
}
