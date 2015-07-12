package service.middleware.linkage.framework.io.nio.strategy.mixed.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.*;
import service.middleware.linkage.framework.utils.ConvertUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * read the data type, if is message packet
 * goto the message reader
 * else goto the file reader
 * @author zhonxu
 *
 */
public class DataTypeReader extends ReaderDecorator {
	private ReaderInterface wrappedFileReader;
	private ReaderInterface wrappedMessageReader;
	private static Logger logger = LoggerFactory.getLogger(DataTypeReader.class);
	
	public DataTypeReader(ReaderInterface wrappedMessageReader, ReaderInterface wrappedFileReader) {
		super(wrappedMessageReader);
		this.wrappedFileReader = wrappedFileReader;
		this.wrappedMessageReader = wrappedMessageReader;
	}

	@Override
	public boolean read(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity)
			throws IOException {
		PacketEntity packetEntity = (PacketEntity) abstractContentEntity;
		ByteBuffer bb = ByteBuffer.allocate(1);
		long readCount = 0;
		long ret = 0;
		long totalCount = 1;
        while (totalCount > readCount) {
        	ret = sc.getSocketChannel().read(bb);
            if (ret < 0 ) {
				sc.close();
            	return false;
            }
        	readCount = readCount + ret;
        }
		logger.debug("read total count : " + readCount);
		int iDataType = ConvertUtils.byteToInt(bb.get(0));
		bb.clear();
		PacketDataType dataType = PacketDataType.valueOfCode(iDataType);
		packetEntity.setPacketDataType(dataType);
		if(dataType == PacketDataType.MESSAGE){
			MessageEntity messageEntity = new MessageEntity();
			messageEntity.setLength(abstractContentEntity.getLength() - 1 - 8);
			packetEntity.setContentEntity(messageEntity);
			return wrappedMessageReader.read(sc, messageEntity);
		}
		else if(dataType == PacketDataType.FILE){
			FileEntityAbstract fileEntity = new FileEntityAbstract();
			fileEntity.setLength(abstractContentEntity.getLength() - 1 - 8);
			packetEntity.setContentEntity(fileEntity);
			return wrappedFileReader.read(sc, fileEntity);
		}
		return false;
	}
}
