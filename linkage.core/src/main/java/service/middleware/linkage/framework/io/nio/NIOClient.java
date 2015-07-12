package service.middleware.linkage.framework.io.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.Client;
import service.middleware.linkage.framework.io.WorkerPool;

/**
 * this class will be used in the client
 * a facade pattern used to capsulate all of the behavior 
 * @author zhonxu
 *
 */
public class NIOClient implements Client {
	private final WorkerPool workerPool;
	private static Logger logger = LoggerFactory.getLogger(NIOClient.class);

	public NIOClient(WorkerPool workerPool){
		this.workerPool = workerPool;
	}
	
	@Override
	public void run() {
		logger.debug("start the client.");
		workerPool.start();
	}

	public WorkerPool getWorkerPool() {
		return workerPool;
	}

	@Override
	public void shutdown() {
		logger.debug("shutdown the client.");
		workerPool.shutdown();
	}
}
