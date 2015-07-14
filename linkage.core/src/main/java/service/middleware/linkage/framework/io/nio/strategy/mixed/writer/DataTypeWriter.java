package service.middleware.linkage.framework.io.nio.strategy.mixed.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.FileEntityAbstract;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketDataType;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.PacketEntity;
import linkage.common.ConvertUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * write the data type, if is message packet
 * goto the message writer
 * else goto the file writer
 * @author zhonxu
 *
 */
public class DataTypeWriter extends WriterDecorator {
	private static Logger logger = LoggerFactory.getLogger(DataTypeWriter.class);
	private WriterInterface wrappedFileWriter;
	private WriterInterface wrappedMessageWriter;
	
	public DataTypeWriter(WriterInterface wrappedMessageWriter, WriterInterface wrappedFileWriter) {
		super(wrappedMessageWriter);
		this.wrappedFileWriter = wrappedFileWriter;
		this.wrappedMessageWriter = wrappedMessageWriter;
	}

	@Override
	public boolean write(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity)
			throws IOException {
		PacketEntity packetEntity = (PacketEntity) abstractContentEntity;
		ByteBuffer buffer = ByteBuffer.allocate(1);
		byte data = ConvertUtils.intToByte(packetEntity.getPacketDataType().getCode());
		buffer.put(data);
		buffer.flip();
		int writtenCount = 0;
		int totalCount = 1;
		while(writtenCount != totalCount)
		{
			writtenCount = writtenCount + sc.getSocketChannel().write(buffer);
		}
		if(packetEntity.getPacketDataType() == PacketDataType.MESSAGE){
			return wrappedMessageWriter.write(sc, packetEntity.getContentEntity());
		}
		else if(packetEntity.getPacketDataType() == PacketDataType.FILE){
			FileEntityAbstract fileEntity = new FileEntityAbstract();
			fileEntity.setLength(abstractContentEntity.getLength() - 1);
			return wrappedFileWriter.write(sc, packetEntity.getContentEntity());
		}
		return false;
	}
}
