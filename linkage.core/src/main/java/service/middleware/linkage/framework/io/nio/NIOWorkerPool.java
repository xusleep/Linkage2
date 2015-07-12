package service.middleware.linkage.framework.io.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.handlers.EventDistributionMaster;
import service.middleware.linkage.framework.io.Worker;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.io.WorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.utils.StringUtils;

import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Worker pool, put the channel into the worker pool
 * the worker deal with the bussiness of receiving message & send message
 * @author zhonxu
 *
 */
public class NIOWorkerPool implements WorkerPool {
	private Worker[] workers;
	private int nextWorkCount = 0;
	private static Logger logger = LoggerFactory.getLogger(NIOWorkerPool.class);
	private final EventDistributionMaster eventDistributionHandler;
	
	/**
	 * 
	 * @param eventDistributionHandler
	 * @param workerCounter assign the point
	 */
	public NIOWorkerPool(EventDistributionMaster eventDistributionHandler, int workerCounter){
		this.eventDistributionHandler = eventDistributionHandler;
		workers = new Worker[workerCounter];
		for(int i = 0; i < workers.length; i++){
			try {
				workers[i] = new NIOWorker();
			} catch (Exception e) {
				logger.error("not expected interruptedException happened. exception detail : " 
						+ StringUtils.ExceptionStackTraceToString(e));
			}
		}
	}
	
	public NIOWorkerPool(EventDistributionMaster eventDistributionHandler){
		this.eventDistributionHandler = eventDistributionHandler;
		int workerCounter = Runtime.getRuntime().availableProcessors();
		workers = new Worker[workerCounter];
		for(int i = 0; i < workers.length; i++){
			try {
				workers[i] = new NIOWorker();
			} catch (Exception e) {
				logger.error("not expected interruptedException happened. exception detail : " 
						+ StringUtils.ExceptionStackTraceToString(e));
			}
		}
	}
	
	public void start(){
		this.eventDistributionHandler.start();
		logger.debug("starting all of  the worker.");
		for(int i = 0; i < workers.length; i++){
			new Thread(workers[i]).start();
		}
	}
	
	public Worker getNextWorker(){
		return workers[Math.abs(nextWorkCount++)%workers.length];
	}
	
	public WorkingChannelContext register(SocketChannel sc, WorkingChannelMode workingChannelMode){
		Worker worker = getNextWorker();
		return worker.submitOpeRegister(new NIOWorkingChannelContext(sc, workingChannelMode, worker, this.eventDistributionHandler));
	}

	@Override
	public void shutdown() {
		logger.debug("shutdown all of the workers.");
		for(int i = 0; i < workers.length; i++){
			workers[i].shutdown();
		}
		this.eventDistributionHandler.shutdown();
	}

	public EventDistributionMaster getEventDistributionHandler() {
		return eventDistributionHandler;
	}
}
