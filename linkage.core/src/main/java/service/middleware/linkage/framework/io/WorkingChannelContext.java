package service.middleware.linkage.framework.io;

import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;


/**
 * hold the object when request a connect,
 * the system will be wrapped by.
 * @author zhonxu
 *
 */
public interface WorkingChannelContext extends WorkingChannelReadWrite {
	public LinkageSocketChannel getLinkageSocketChannel();
	public void closeWorkingChannel();
	public WorkingChannelMode getWorkingChannelMode();
	public WorkingChannelStrategy getWorkingChannelStrategy();
	public String getId();
}
