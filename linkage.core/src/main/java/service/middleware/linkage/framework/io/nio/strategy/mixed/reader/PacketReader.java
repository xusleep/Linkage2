package service.middleware.linkage.framework.io.nio.strategy.mixed.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketEntity;
import service.middleware.linkage.framework.utils.ConvertUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * when reading a packet data from the channel
 * we are gona to use the decorator pattern to read.
 * the packet reader is a decorator which will wapper 
 * the data type reader, the data type reader will
 * wapper the file reader and message reader
 * we read from the packet reader, read the size of the packet
 * then goto the data type reader, read the datatype of the packet
 * if the data type is message , we goto the message reader
 * else we goto the file reader.
 * @author zhonxu
 *
 */
public class PacketReader extends ReaderDecorator {
	private static Logger logger = LoggerFactory.getLogger(PacketReader.class);
	
	public PacketReader(ReaderInterface wrappedReader) {
		super(wrappedReader);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean read(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity)
			throws IOException {
		logger.debug("trying to get the read lock");
		synchronized (sc.getReadLock()) {
			logger.debug("get the lock, reading the socket : " + sc.getSocketChannel().hashCode());
			PacketEntity packetEntity = (PacketEntity) abstractContentEntity;
			ByteBuffer bb = ByteBuffer.allocate(8);
			long readCount = 0;
			long ret = 0;
			long totalCount = 8;
			while (totalCount > readCount) {
				ret = sc.getSocketChannel().read(bb);
				if (ret < 0) {
					sc.close();
					return false;
				}
				readCount = readCount + ret;
			}
			long length = ConvertUtils.bytesToLong(bb.array());
			logger.debug("read total length : " + length);
			logger.debug("read total count : " + readCount);
			packetEntity.setLength(length);
			return this.getWrappedReader().read(sc, packetEntity);
		}
	}

}
