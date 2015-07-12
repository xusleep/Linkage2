package test.framework.concurrence.condition.job;

public interface JobInterface {
	/**
	 * �˷���Ϊ���в�������ǰ��׼��
	 */
	void doBeforeJob();
	/**
	 * �˷������ڲ����߳������У����ɶ���߳�ͬʱ����
	 */
	void doConcurrentJob();
	/**
	 * �˷���Ϊ�����겢��������Ĵ��������ڲ��Ե�ʱ������ڴ˷������������ԱȽ�
	 */
	void doAfterJob();
	
	/**
	 * �˷��������أ���jobӦ���������߳���Ŀ
	 * @return
	 */
	int  getThreadCount();
	
	/**
	 * ����Ӧ���������̵߳���Ŀ
	 */
	void setThreadCount(int count);
}
