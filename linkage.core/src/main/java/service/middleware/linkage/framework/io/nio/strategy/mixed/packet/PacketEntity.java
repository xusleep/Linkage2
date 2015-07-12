package service.middleware.linkage.framework.io.nio.strategy.mixed.packet;

public class PacketEntity extends AbstractContentEntity {
	private PacketDataType packetDataType; 
	private AbstractContentEntity abstractContentEntity;
	
	public PacketDataType getPacketDataType() {
		return packetDataType;
	}
	public void setPacketDataType(PacketDataType packetDataType) {
		this.packetDataType = packetDataType;
	}
	public AbstractContentEntity getContentEntity() {
		return abstractContentEntity;
	}
	public void setContentEntity(AbstractContentEntity abstractContentEntity) {
		this.abstractContentEntity = abstractContentEntity;
	}
	
	
}
