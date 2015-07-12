package test.framework.concurrence.condition;

import test.framework.concurrence.condition.job.JobInterface;

public class ConcurrentThread extends Thread{
	private MainConcurrentThread mainThread;
	private JobInterface job;
	
	public ConcurrentThread(MainConcurrentThread mainThread, JobInterface job)
	{
		this.mainThread = mainThread;
		this.job = job;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		if(this.getMainThread() != null)
		{
			//�ȴ������ź���ɣ���ǰ����� countdown�����ｫ����ִ����ȥ
			this.getMainThread().awaitStartSignal();
			if(this.job != null)
			{
				this.job.doConcurrentJob();
			}
			//������һ��֤���Ѿ���һ���߳������job
			this.getMainThread().countDownDoneSignal();
		}
	}
	
	public MainConcurrentThread getMainThread() {
		return mainThread;
	}

	public void setMainThread(MainConcurrentThread mainThread) {
		this.mainThread = mainThread;
	}
}
