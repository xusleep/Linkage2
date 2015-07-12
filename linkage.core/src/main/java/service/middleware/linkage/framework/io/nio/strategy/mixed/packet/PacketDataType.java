package service.middleware.linkage.framework.io.nio.strategy.mixed.packet;

public enum PacketDataType {
	MESSAGE(1),
	FILE(2);

	private int code;

	PacketDataType(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static PacketDataType valueOfCode(int code) {
		for (PacketDataType type : values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return null;
	}
}
