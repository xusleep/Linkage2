package service.middleware.linkage.framework.connection.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.exception.ServiceException;

public class NIOConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(NIOConnectionManager.class);

    /**
     * create a new NIO connection to the ip with port
     *
     * @param ip
     * @param port
     * @return
     * @throws IOException
     */
    public static SocketChannel createNIOConnection(String ip, int port) throws IOException {
        // get a Socket channel
        SocketChannel channel = SocketChannel.open();
        // connect
        channel.connect(new InetSocketAddress(ip, port));
        // finish the connect
        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }
        return channel;
    }
}
