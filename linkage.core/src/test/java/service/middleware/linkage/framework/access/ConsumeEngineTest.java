//package service.middleware.linkage.framework.access;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;
//import service.middleware.linkage.framework.access.domain.ServiceRequest;
//import service.middleware.linkage.framework.bootstrap.NIOClientBootStrap;
//import service.middleware.linkage.framework.bootstrap.NIOServerBootStrap;
//import test.framework.concurrence.condition.MainConcurrentThread;
//import test.framework.concurrence.condition.job.AbstractJob;
//import test.framework.concurrence.condition.job.JobInterface;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
//import static org.junit.Assert.assertTrue;
//
//public class ConsumeEngineTest {
//    private static NIOServerBootStrap serviceBootStrap;
//    private static NIOClientBootStrap clientBootStrap;
//    private static Logger logger = LoggerFactory.getLogger(ConsumeEngineTest.class);
//
//    @BeforeClass
//    public static void setUp() {
//        try {
//            serviceBootStrap = new NIOServerBootStrap("service/middleware/linkage/framework/access/conf/service_server.properties", 5);
//            serviceBootStrap.run();
//            clientBootStrap = new NIOClientBootStrap("service/middleware/linkage/framework/access/conf/client_client.properties", 1);
//            clientBootStrap.run();
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Test
//    public void testBasicProcessRequest() {
//        for (int i = 0; i < 1000; i++) {
//            List<Object> args = new ArrayList<>();
//            args.add("121");
//            args.add("121");
//            List<Class<?>> argTypes = new ArrayList<>();
//            argTypes.add(String.class);
//            argTypes.add(String.class);
//            ServiceRequest objRequestEntity = clientBootStrap.getServiceAccess().getServiceAccessEngine().createRequestEntity("calculator", args, argTypes);
//            ServiceRequestResult result = new ServiceRequestResult();
//            result.setRequestID(objRequestEntity.getRequestID());
//            ServiceRegisterEntry serviceInformation = new ServiceRegisterEntry();
//            serviceInformation.setAddress(serviceBootStrap.getServicePropertyEntity().getServiceAddress());
//            serviceInformation.setPort(serviceBootStrap.getServicePropertyEntity().getServicePort());
//            result = clientBootStrap.getServiceAccess().getServiceAccessEngine().requestService(objRequestEntity, result, serviceInformation, false);
//            assertTrue("Exception happen when request , exception: " + (result.isException() ? result.getException().getMessage() : ""), !result.isException());
//            assertTrue("result not right, 121 + 234 = 355, the result is " + result.getResponseEntity().getJsonResult(),
//                    result.getResponseEntity().getJsonResult().equals("355"));
//        }
//    }
//
//    @Test
//    public void testConcurrentBasicProcessRequest() {
//        // Test with not cached channel
//        ConcurrentRequestJob job = new ConcurrentRequestJob(false);
//        job.setThreadCount(1000);
//        List<JobInterface> jobList = new LinkedList<JobInterface>();
//        jobList.add(job);
//        MainConcurrentThread mct1 = new MainConcurrentThread(jobList, false);
//        mct1.start();
//        try {
//            mct1.join();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        // Test with cached channel
//        ConcurrentRequestJob job1 = new ConcurrentRequestJob(true);
//        job1.setThreadCount(1000);
//        List<JobInterface> jobList1 = new LinkedList<JobInterface>();
//        jobList1.add(job1);
//        MainConcurrentThread mct2 = new MainConcurrentThread(jobList1, false);
//        mct2.start();
//        try {
//            mct2.join();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//
//    @AfterClass
//    public static void clear() {
//        serviceBootStrap.shutdown();
//        clientBootStrap.shutdown();
//    }
//
//    /**
//     * this is a job, which will be run by the concurrent test framework
//     *
//     * @author Smile
//     */
//    private class ConcurrentRequestJob extends AbstractJob {
//
//        private boolean isCachingChannel;
//
//        private ConcurrentRequestJob(boolean isCachingChannel) {
//            this.isCachingChannel = isCachingChannel;
//        }
//
//        @Override
//        public void doBeforeJob() {
//
//        }
//
//        @Override
//        public void doConcurrentJob() {
//            Random r = new Random();
//            int value1 = r.nextInt(10000);
//            int value2 = r.nextInt(10000);
//            int resultValue = value1 + value2;
//            // TODO Auto-generated method stub
//            List<Object> args = new ArrayList<>();
//            args.add("" + value1);
//            args.add("" + value2);
//            List<Class<?>> argTypes = new ArrayList<>();
//            argTypes.add(StWring.class);
//            argTypes.add(String.class);
//            ServiceRequest objRequestEntity = clientBootStrap.getServiceAccess().getServiceAccessEngine().createRequestEntity("calculator", args, argTypes);
//            ServiceRequestResult result = new ServiceRequestResult();
//            result.setRequestID(objRequestEntity.getRequestID());
//            ServiceRegisterEntry serviceInformation = new ServiceRegisterEntry();
//            serviceInformation.setAddress(serviceBootStrap.getServicePropertyEntity().getServiceAddress());
//            serviceInformation.setPort(serviceBootStrap.getServicePropertyEntity().getServicePort());
//            result = clientBootStrap.getServiceAccess().getServiceAccessEngine().requestService(objRequestEntity, result, serviceInformation, isCachingChannel);
//            assertTrue("Exception happen when request , exception: " + (result.isException() ? result.getException().getMessage() : ""), !result.isException());
//            assertTrue(String.format("result not right, %s + %s = %s, the result is %s", value1, value2, resultValue, result.getResponseEntity().getJsonResult()), result.getResponseEntity().getJsonResult().equals("" + resultValue));
//        }
//
//        @Override
//        public void doAfterJob() {
//        }
//
//    }
//}
