package service.middleware.linkage.framework.io.nio.strategy.mixed.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketEntity;
import service.middleware.linkage.framework.utils.ConvertUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * when writting a packet data from the channel
 * we are gona to use the decorator pattern to write.
 * the packet writer is a decorator which will wapper 
 * the data type writer, the data type writer will
 * wapper the file writer and message writer
 * we write from the packet writer, write the size of the packet
 * then goto the data type writer, write the datatype of the packet
 * if the data type is message , we goto the message writer
 * else we goto the file writer.
 * @author zhonxu
 *
 */
public class PacketWriter extends WriterDecorator {
	private static Logger logger = LoggerFactory.getLogger(PacketWriter.class);
	public PacketWriter(WriterInterface wrappedWriter) {
		super(wrappedWriter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean write(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity)
			throws IOException {
		// ¼ÓËø
		logger.debug("trying to get the write lock");
		synchronized (sc.getWriteLock()) {
			logger.debug("get the lock, writting the socket : " + sc.getSocketChannel().hashCode());
			PacketEntity packetEntity = (PacketEntity) abstractContentEntity;
			byte[] data = ConvertUtils.longToBytes(packetEntity.getLength());
			ByteBuffer buffer = ByteBuffer.allocate(8);
			buffer.put(data, 0, 8);
			buffer.flip();
			int writtenCount = 0;
			int totalCount = 8;
			while (writtenCount != totalCount) {
				writtenCount = writtenCount + sc.getSocketChannel().write(buffer);
			}
			return this.getWrappedWriter().write(sc, abstractContentEntity);
		}
	}

}
