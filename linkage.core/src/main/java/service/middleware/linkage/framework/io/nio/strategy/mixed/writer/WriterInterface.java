package service.middleware.linkage.framework.io.nio.strategy.mixed.writer;

import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;

import java.io.IOException;

/**
 * writer interface for all of the writer 
 * @author zhonxu
 *
 */
public interface WriterInterface {
	public boolean write(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity) throws IOException;
}
