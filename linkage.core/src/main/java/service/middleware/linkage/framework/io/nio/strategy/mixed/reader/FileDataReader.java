package service.middleware.linkage.framework.io.nio.strategy.mixed.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.FileInformationStorageList;
import service.middleware.linkage.framework.io.nio.LinkageSocketChannel;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.AbstractContentEntity;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.FileEntityAbstract;
import service.middleware.linkage.framework.io.nio.strategy.mixed.packet.FileInformationEntity;
import service.middleware.linkage.framework.utils.ConvertUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * read the file data,
 * first we reader the file id,
 * and find the file information by the file id
 * then read the channel and write file
 * @author zhonxu
 *
 */
public class FileDataReader extends ReaderDecorator {

    private static Logger logger = LoggerFactory.getLogger(FileDataReader.class);
	private static final long FILE_TRANSFER_BUFFER_SIZE = 1024 * 1024 * 10;
	
	public FileDataReader(ReaderInterface wrappedReader) {
		super(wrappedReader);
	}
	
	@Override
	public boolean read(LinkageSocketChannel sc, AbstractContentEntity abstractContentEntity) throws IOException {
		FileEntityAbstract fileEntity = (FileEntityAbstract) abstractContentEntity;
		ByteBuffer bb = ByteBuffer.allocate(8);
		long readCount = 0;
		long ret = 0;
		long totalCount = 8;
        while (totalCount > readCount) {
        	ret = sc.getSocketChannel().read(bb);
            if (ret < 0 ) {
                sc.close();
            	return false;
            }
        	readCount = readCount + ret;
        }
        if (ret < 0 ) {
        	throw new IOException("channel is closed.");
        }
        long fileID = ConvertUtils.bytesToLong(bb.array());
        bb.clear();
        fileEntity.setFileID(fileID);
        long fileSize = abstractContentEntity.getLength() - 8;
        FileInformationEntity fileInformationEntity = FileInformationStorageList.findFileInformationEntity(fileID);
        logger.debug("FileReader fileID:" + fileID + " filesize:" + fileSize + " save path:" + fileInformationEntity.getFilePath());
        FileOutputStream fos = new FileOutputStream(new File(fileInformationEntity.getFilePath()));
		FileChannel fileChannel = fos.getChannel();
		readCount = 0;
		totalCount = fileSize;
    	while(totalCount > readCount){
			long buffSize = FILE_TRANSFER_BUFFER_SIZE; 
			if (totalCount - readCount < buffSize) {
				buffSize = totalCount - readCount;
			}
    		ret = fileChannel.transferFrom(sc.getSocketChannel(), readCount, buffSize);
            if (ret < 0 ) {
            	return false;
            }
    		readCount = readCount + ret;
    	}
    	fileChannel.close();
        fos.close();
		return true;
	}
	
	/**
	 *  the writting queue for the request
	 * @author zhonxu
	 *
	 */
	private final class FileQueue implements Queue<FileInformationEntity> {

       private final Queue<FileInformationEntity> queue;

       public FileQueue() {
           queue = new ConcurrentLinkedQueue<FileInformationEntity>();
       }

       public FileInformationEntity remove() {
           return queue.remove();
       }

       public FileInformationEntity element() {
           return queue.element();
       }

       public FileInformationEntity peek() {
           return queue.peek();
       }

       public int size() {
           return queue.size();
       }

       public boolean isEmpty() {
           return queue.isEmpty();
       }

       public Iterator<FileInformationEntity> iterator() {
           return queue.iterator();
       }

       public Object[] toArray() {
           return queue.toArray();
       }

       public <T> T[] toArray(T[] a) {
           return queue.toArray(a);
       }

       public boolean containsAll(Collection<?> c) {
           return queue.containsAll(c);
       }

       public boolean addAll(Collection<? extends FileInformationEntity> c) {
           return queue.addAll(c);
       }

       public boolean removeAll(Collection<?> c) {
           return queue.removeAll(c);
       }

       public boolean retainAll(Collection<?> c) {
           return queue.retainAll(c);
       }

       public void clear() {
           queue.clear();
       }

       public boolean add(FileInformationEntity e) {
           return queue.add(e);
       }

       public boolean remove(Object o) {
           return queue.remove(o);
       }

       public boolean contains(Object o) {
           return queue.contains(o);
       }

       public boolean offer(FileInformationEntity e) {
           boolean success = queue.offer(e);
           return success;
       }

       public FileInformationEntity poll() {
    	   FileInformationEntity e = queue.poll();
           return e;
       }
	}
}
