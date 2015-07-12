package service.middleware.linkage.framework.io.nio.strategy.mixed.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.MessageEntity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * write the message data
 * @author zhonxu
 *
 */
public class MessageDataWriter extends WriterDecorator {
	private static Logger logger = LoggerFactory.getLogger(MessageDataWriter.class);
	public MessageDataWriter(WriterInterface wrappedWriter) {
		super(wrappedWriter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean write(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity)
			throws IOException {
		MessageEntity messageEntity = (MessageEntity) abstractContentEntity;
		ByteBuffer buffer = ByteBuffer.allocate((int) messageEntity.getLength());
		buffer.put(messageEntity.getData(), 0, (int) messageEntity.getLength());
		buffer.flip();
		int writtenCount = 0;
		int totalCount = (int) messageEntity.getLength();
		while(writtenCount != totalCount)
		{
			writtenCount = writtenCount + sc.getSocketChannel().write(buffer);
		}
		return false;
	}

}
