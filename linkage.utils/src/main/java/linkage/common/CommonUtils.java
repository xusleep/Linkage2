package linkage.common;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class CommonUtils {

    /**
     * ªÒµ√Œ®“ªid
     * @return
     */
    public static String generateId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getNetKey(String address, int port){
        return address + "_" + port;
    }

    public static String getNetKey(InetSocketAddress socketAddress){
        String key = socketAddress.getHostString() + "_" + socketAddress.getPort();
        return key;
    }

}
