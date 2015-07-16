package test.run.withoutcenter;

import clould.storage.service.utils.StringFileUtils;
import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.bootstrap.NIOClientBootStrap;
import test.framework.concurrence.condition.MainConcurrentThread;
import test.framework.concurrence.condition.job.AbstractJob;
import test.framework.concurrence.condition.job.JobInterface;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Smile
 *
 */
public class StartClientMoreConnection extends AbstractJob {
	public static final AtomicInteger aint = new AtomicInteger(0);
	public static final AtomicInteger successCount = new AtomicInteger(0);
	public static final AtomicInteger requestCount = new AtomicInteger(0);
	public static final int THREAD_COUNT = 1;
	public static final int CAL_COUNT = 10000;
	public static final CountDownLatch countDown = new CountDownLatch(THREAD_COUNT * CAL_COUNT);
	public StartClientMoreConnection() {

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
		for(long i = 0; i < CAL_COUNT; i++)
		{
			//System.out.println("request count ..." + requestCount.incrementAndGet());
	    	List<ServiceParameter> args1 = new LinkedList<ServiceParameter>();
	    	args1.add(new ServiceParameter("a", int.class, requestCount.incrementAndGet()));
	    	args1.add(new ServiceParameter("b", int.class, aint.incrementAndGet()));
            cb.requestService("localhost", 5003, "calculator", args1, new RequestCallback() {
                @Override
                public void runCallback(String serviceResponse) {
                    System.out.println("result is " + serviceResponse);
					countDown.countDown();
					successCount.incrementAndGet();
                }

                @Override
                public void runException(Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
                }
            });

			try
			{
				Thread.sleep(1000);
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
        }

	}

	@Override
	public void doAfterJob() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException {
		StartClientMoreConnection job1 = new StartClientMoreConnection();
		job1.setThreadCount(THREAD_COUNT);
		List<JobInterface> jobList = new LinkedList<JobInterface>();
		jobList.add(job1);
		MainConcurrentThread mct1 = new MainConcurrentThread(jobList);
		mct1.start();
		try {
			countDown.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("success count = " + successCount.get());
	}
}
