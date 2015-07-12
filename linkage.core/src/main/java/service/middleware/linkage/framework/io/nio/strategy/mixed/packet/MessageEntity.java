package service.middleware.linkage.framework.io.nio.strategy.mixed.packet;

public class MessageEntity extends AbstractContentEntity {
	private byte[] data;
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
