package service.middleware.linkage.framework.io;

import java.nio.channels.SocketChannel;

import service.middleware.linkage.framework.handlers.EventDistributionMaster;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;

/**
 * worker pool interface
 * @author zhonxu
 *
 */
public interface WorkerPool {
	public void start();
	public Worker getNextWorker();
	public WorkingChannelContext register(SocketChannel sc, WorkingChannelMode workingChannelMode);
	/**
	 * shutdown 
	 */
	public void shutdown();
	public EventDistributionMaster getEventDistributionHandler();
}
