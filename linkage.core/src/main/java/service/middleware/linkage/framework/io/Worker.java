package service.middleware.linkage.framework.io;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * worker interface
 * @author zhonxu
 *
 */
public interface Worker extends Runnable{
	public WorkingChannelContext submitOpeRegister(WorkingChannelContext sc);
	/**
	 * shutdown 
	 */
	public void shutdown();
}
