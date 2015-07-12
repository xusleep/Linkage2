package service.middleware.linkage.framework.io;


/**
 * Server interface
 * @author zhonxu
 *
 */
public interface Server extends Runnable{
	public WorkerPool getWorkerPool();
	/**
	 * shutdown 
	 */
	public void shutdown();
}
