package test.framework.concurrence.condition;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import test.framework.concurrence.condition.job.JobInterface;
import test.framework.concurrence.condition.job.TestJob;

/**
 *  �������ڣ�ͬʱ�������job�Ķ���̣߳�ʹ�䲢������
 *  ���е�jobӦ���̳�JobInterface����
 *  ��������Ҳ��һ���̣߳���Ҫ�ȴ����̵߳Ľ�������ʹ��.join();����
 *  
 * @author Smile
 *
 */
public class MainConcurrentThread extends Thread {
	//����������ɵ��ź�
	private CountDownLatch doneSignal;
	//ͬʱ��������������ź�
	private CountDownLatch startSignal;
	private List<JobInterface> jobList;
	private boolean deadMonitorCheck;
	
	public MainConcurrentThread(List<JobInterface> jobList, boolean deadMonitorCheck){
		this.jobList = jobList;
		init();
		this.deadMonitorCheck = deadMonitorCheck;
	}
	
	public MainConcurrentThread(List<JobInterface> jobList)
	{ 
		this(jobList, false);
	}
	
	/**
	 * ������Ҫ���������е��߳���Ŀ
	 * @return
	 */
	private int getAllJobThreadCount()
	{
		int countJobs = jobList.size();
		int countAllJobThreads = 0;
		for(int i = 0; i < countJobs; i++)
		{
			countAllJobThreads = countAllJobThreads + jobList.get(i).getThreadCount();
		}
		return countAllJobThreads;
	}
	
	/**
	 * ��ʼ��
	 * 
	 */
	private void init()
	{
		int allJobThreadCount = getAllJobThreadCount();
		this.doneSignal = new CountDownLatch(allJobThreadCount);
		this.startSignal = new CountDownLatch(1);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
		ScheduledExecutorService executor = null;
		if(this.deadMonitorCheck){
			executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(new FindMonitorRunnable(), 0, 1, TimeUnit.SECONDS);
		}
		
		int countJobs = jobList.size();
		for(int i = 0; i < countJobs; i++)
		{
			JobInterface job = jobList.get(i);
			//����jobǰ��������һЩ׼������
			job.doBeforeJob();
		}
		
		for(int i = 0; i < countJobs; i++)
		{
			final JobInterface job = jobList.get(i);
			int jobThreadCount = job.getThreadCount();
			//�������е��߳�
			for(int j = 0; j < jobThreadCount; j++)
			{
				Thread thr = new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
							//�ȴ������ź���ɣ���ǰ����� countdown�����ｫ����ִ����ȥ
							awaitStartSignal();
							job.doConcurrentJob();
							//������һ��֤���Ѿ���һ���߳������job
							countDownDoneSignal();
						}
					};
				thr.start();
			}
		}
		
		//�����ֻ��1��countdown�Ժ������������߳̽��������У���������
		this.countDownStartSignal();
		
		//�ȴ��������߳��������
		this.awaitDoneSignal();
		for(int i = 0; i < countJobs; i++)
		{
			JobInterface job = jobList.get(i);
			//������job��������һЩ��������
			job.doAfterJob();
		}
		if(this.deadMonitorCheck){
			executor.shutdownNow();
		}
	}

	/**
	 * ��������һ���������ʹ��ͬ������ ���̵߳�count down ��Ҫ�������߳� �ȴ����е��������
	 */
	public synchronized void countDownDoneSignal()
	{
		if(this.doneSignal != null)
		{
			this.doneSignal.countDown();
		}
	}
	/**
	 * �����̣߳��������ʹ��ͬ������ ���̵߳�count down ��Ҫ�������߳� �ȴ����е��������
	 */
	public void awaitDoneSignal()
	{
		if(this.doneSignal != null)
		{
			try {
				this.doneSignal.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * ��������һ���������ʹ��ͬ������ ���̵߳�count down ��Ҫ���ڲ����߳� �ȴ����е��������
	 */
	public synchronized void countDownStartSignal()
	{
		if(this.startSignal != null)
		{
			this.startSignal.countDown();
		}
	}
	/**
	 * �����̣߳��������ʹ��ͬ������ �����̵߳�count down ��Ҫ���ڲ����߳� �ȴ����е��������
	 */
	public void awaitStartSignal()
	{
		if(this.startSignal != null)
		{
			try {
				this.startSignal.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
//		TestJob job1 = new TestJob(1);
//		job1.setThreadCount(1);
//		TestJob job2 = new TestJob(2);
//		job2.setThreadCount(10);
//		List<JobInterface> jobList = new LinkedList<JobInterface>();
//		jobList.add(job1);
//		jobList.add(job2);
//		MainConcurrentThread ct = new MainConcurrentThread(jobList);
//		ct.start();
//		AtomicInteger t = new AtomicInteger(10);
		
		Thread t1 = new Thread(){

			@Override
			public void run() {
					int sum = 1000;
					for(int j = 0; j < 10; j++)
					{
						if(Thread.interrupted())
						{
							break;
						}
						// TODO Auto-generated method stub
						for(int i = 0; i < 1000000000; i++)
						{
							for(int l = 0; l < 1000000000; l++)
							{
								sum += 2;
								
							}
						}
						System.out.println(sum);
					}
			}
			
			
		};
		t1.start();
		System.out.println("test 112");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t1.interrupt();
		System.out.println("test 112");
		if(t1.interrupted())
		{
			System.out.println("test");
		}
	}
}
