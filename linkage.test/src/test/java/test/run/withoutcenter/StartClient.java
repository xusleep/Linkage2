package test.run.withoutcenter;

import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.bootstrap.NIOClientBootStrap;
import test.framework.concurrence.condition.MainConcurrentThread;
import test.framework.concurrence.condition.job.AbstractJob;
import test.framework.concurrence.condition.job.JobInterface;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Smile
 *
 */
public class StartClient extends AbstractJob {
	public static final AtomicInteger aint = new AtomicInteger(2320);
	private final AtomicBoolean isFailed = new AtomicBoolean(false);
	public static final AtomicInteger successCount = new AtomicInteger(0);
	public static final AtomicInteger requestCount = new AtomicInteger(0);

	public StartClient() {
		System.out.println("test");
	}

	@Override
	public void doBeforeJob() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doConcurrentJob() {
		NIOClientBootStrap clientBootStrap = new NIOClientBootStrap("localhost", 5003, "conf/client_client.properties", 5);
		clientBootStrap.run();
        ServiceAccess cb = clientBootStrap.getServiceAccess();
		for(long i = 0; i < 99999999; i++)
		{
			//System.out.println("request count ..." + requestCount.incrementAndGet());
	    	List<Object> args1 = new LinkedList<Object>();
	    	String a = "" + requestCount.incrementAndGet();
	    	String b = "" + aint.incrementAndGet();
	    	args1.add(a);
	    	args1.add(b);
            cb.requestService("localhost", 5003, "calculator", args1, new RequestCallback() {
                @Override
                public void runCallback(Object serviceResponse) {
                    System.out.println("result is " + serviceResponse);
                }

                @Override
                public void runException(Exception ex) {

                }
            });
        }
	}

	@Override
	public void doAfterJob() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException {
		StartClient job1 = new StartClient();
		job1.setThreadCount(10);
		List<JobInterface> jobList = new LinkedList<JobInterface>();
		jobList.add(job1);
		MainConcurrentThread mct1 = new MainConcurrentThread(jobList);
		mct1.start();
		System.out.println("success count = " + successCount.get());
	}
}
