package test.framework.concurrence.condition;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class MainConcurrentExecutor extends Thread {
	//����������ɵ��ź�
	private CountDownLatch doneSignal;
	//ͬʱ��������������ź�
	private CountDownLatch startSignal;
	private List<JobInterface> jobList;
	private final ExecutorService executor;
	
	public MainConcurrentExecutor(List<JobInterface> jobList)
	{
		this(jobList, Executors.newFixedThreadPool(1000));
	}
	
	public MainConcurrentExecutor(List<JobInterface> jobList, ExecutorService executor)
	{ 
		this.jobList = jobList;
		this.executor = executor;
		init();
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
				Runnable task = new Runnable(){
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
				this.executor.execute(task);
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
		//ִ�������շ���
		this.executor.shutdown();
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
		TestJob job1 = new TestJob(1);
		job1.setThreadCount(1);
		TestJob job2 = new TestJob(2);
		job2.setThreadCount(10);
		List<JobInterface> jobList = new LinkedList<JobInterface>();
		jobList.add(job1);
		jobList.add(job2);
		//MainConcurrentExecutor ct = new MainConcurrentExecutor(jobList, new ThreadPerTaskExecutor());
		MainConcurrentExecutor ct = new MainConcurrentExecutor(jobList);
		ct.start();
		//BlockingQueue<BigInteger> bq = new ArrayBlockingQueue<BigInteger>();
		
	}
}
