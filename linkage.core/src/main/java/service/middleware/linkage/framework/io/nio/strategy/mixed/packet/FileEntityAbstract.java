package service.middleware.linkage.framework.io.nio.strategy.mixed.packet;

/**
 * This domain retain the file content
 * @author zhonxu
 *
 */
public class FileEntityAbstract extends AbstractContentEntity {
	private long fileID;
	public long getFileID() {
		return fileID;
	}
	public void setFileID(long fileID) {
		this.fileID = fileID;
	}

}
