package service.middleware.linkage.framework.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;

/**
 * the interface of the the working channel strategy
 * @author zhonxu
 *
 */
public abstract class WorkingChannelStrategy implements WorkingChannelReadWrite{
	private final NIOWorkingChannelContext workingChannelContext;
	private static Logger logger = LoggerFactory.getLogger(WorkingChannelStrategy.class);
	
	public WorkingChannelStrategy(NIOWorkingChannelContext workingChannelContext){
		this.workingChannelContext = workingChannelContext;

	}
	
	public NIOWorkingChannelContext getWorkingChannelContext() {
		return workingChannelContext;
	}
}
