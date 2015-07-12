package service.middleware.linkage.framework.io.nio.strategy.mixed.reader;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;

/**
 * an interface for all of the reader
 * @author zhonxu
 *
 */
public interface ReaderInterface {
	public boolean read(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity) throws IOException;
}
