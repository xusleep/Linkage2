package service.middleware.linkage.framework.io.nio.strategy.mixed.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.FileInformationStorageList;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.FileEntityAbstract;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.FileInformationEntity;
import linkage.common.ConvertUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * write the file data,
 * find the file information by the file id
 * then write the channel and write file
 * @author zhonxu
 *
 */
public class FileDataWriter extends WriterDecorator {
	private static final long FILE_TRANSFER_BUFFER_SIZE = 1024 * 1024 * 10;
	private static Logger logger = LoggerFactory.getLogger(FileDataWriter.class);
	
	public FileDataWriter(WriterInterface wrappedWriter) {
		super(wrappedWriter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean write(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity)
			throws IOException {
		FileEntityAbstract fileEntity = (FileEntityAbstract) abstractContentEntity;
		FileInformationEntity fileInformationEntity = FileInformationStorageList.findFileInformationEntity(fileEntity.getFileID());
		FileInputStream fis = new FileInputStream(new File(fileInformationEntity.getFilePath()));
		FileChannel fileChannel = fis.getChannel();
		long writtenCount = 0;
		long totalCount = 8;
		ByteBuffer bb = ByteBuffer.allocate((int) totalCount);
		byte[] dst = ConvertUtils.longToBytes(fileEntity.getFileID());
		bb.put(dst, 0, (int) totalCount);
		bb.flip();
		while(writtenCount != totalCount)
		{
			writtenCount = writtenCount + sc.getSocketChannel().write(bb);
		}
		writtenCount = 0;
		totalCount = fileEntity.getLength() - 8;
		while (writtenCount < totalCount) {
			long buffSize = FILE_TRANSFER_BUFFER_SIZE; 
			if (totalCount - writtenCount < buffSize) {
				buffSize = totalCount - writtenCount;
			}
			long transferred = fileChannel.transferTo(writtenCount, buffSize, sc.getSocketChannel());
			if (transferred > 0) {
				writtenCount += transferred;
			}
		}
		return true;
	}

}
