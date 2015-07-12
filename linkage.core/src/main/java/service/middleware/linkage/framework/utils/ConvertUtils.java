package service.middleware.linkage.framework.utils;

import java.nio.ByteBuffer;

public class ConvertUtils {
	
	/**
	 * convert long to byte array
	 * @param x
	 * @return
	 */
    public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8); 
        buffer.putLong(0, x);
        return buffer.array();
    }

    /**
     * convert byte array to long
     * @param bytes
     * @return
     */
    public static long bytesToLong(byte[] bytes) {
    	ByteBuffer buffer = ByteBuffer.allocate(8); 
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip 
        return buffer.getLong();
    }
    
	/**
	 * convert the int to byte
	 * @param x
	 * @return
	 */
	public static byte intToByte(int x) {
		return (byte) x;
	}
	
	/**
	 * convert byte to int
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte b) {
		//Java ���ǰ� byte �����з��������ǿ���ͨ������� 0xFF ���ж�������õ������޷�ֵ
		return b & 0xFF;
	}
}
