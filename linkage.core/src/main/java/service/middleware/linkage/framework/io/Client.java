package service.middleware.linkage.framework.io;

import service.middleware.linkage.framework.handlers.EventDistributionMaster;

/**
 * client interface
 * @author zhonxu
 *
 */
public interface Client extends Runnable{
	public WorkerPool getWorkerPool();
	/**
	 * shutdown 
	 */
	public void shutdown();
}
