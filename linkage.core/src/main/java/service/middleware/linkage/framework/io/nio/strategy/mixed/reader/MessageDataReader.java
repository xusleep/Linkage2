package service.middleware.linkage.framework.io.nio.strategy.mixed.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.MessageEntity;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * read the message data
 * @author zhonxu
 *
 */
public class MessageDataReader extends ReaderDecorator {
	private static Logger logger = LoggerFactory.getLogger(MessageDataReader.class);
	
	
	public MessageDataReader(ReaderInterface wrappedReader) {
		super(wrappedReader);
	}
	
	@Override
	public boolean read(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity) throws IOException {
		MessageEntity messageEntity = (MessageEntity) abstractContentEntity;

	    ByteBuffer bb = ByteBuffer.allocate((int) (abstractContentEntity.getLength()));
	    long readCount = 0;
	    long ret = 0;
	    long totalCount = abstractContentEntity.getLength();
        while (totalCount > readCount) {
        	ret = sc.getSocketChannel().read(bb);
            if (ret < 0 ) {
				sc.close();
            	return false;
            }
        	readCount = readCount + ret;
        }
		logger.debug("read total count : " + readCount);
        messageEntity.setData(bb.array());
		return true;
	}
}
