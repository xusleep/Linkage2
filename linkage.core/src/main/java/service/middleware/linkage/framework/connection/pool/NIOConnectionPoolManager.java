package service.middleware.linkage.framework.connection.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.exception.ServiceException;

public class NIOConnectionPoolManager {
    private static Logger logger = LoggerFactory.getLogger(NIOConnectionPoolManager.class);

    /**
     * connect to the server by the ip address and port
     *
     * @param ip
     * @param port
     * @return
     * @throws IOException
     */
    public static SocketChannel getIOConnection(String ip, int port) throws ServiceException {
        try {
            logger.debug("created the connection and put it into the cached pool. ip: " + ip + " port : " + port);
            return createNIOConnection(ip, port);
        } catch (IOException e) {
            throw new ServiceException(e, e.getMessage());
        }
    }

    /**
     * create a new NIO connection to the ip with port
     *
     * @param ip
     * @param port
     * @return
     * @throws IOException
     */
    private static SocketChannel createNIOConnection(String ip, int port) throws IOException {
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
