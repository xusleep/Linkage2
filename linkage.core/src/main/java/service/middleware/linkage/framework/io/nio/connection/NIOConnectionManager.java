package service.middleware.linkage.framework.io.nio.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.io.nio.NIOWorkingChannelContext;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class NIOConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(NIOConnectionManager.class);

    /**
     * connect to the service
     *
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public static NIOWorkingChannelContext createNIOWorkingChannel(String address, int port, WorkingChannelMode workingChannelMode, WorkerPool workerPool) throws IOException {
        SocketChannel channel = createNIOConnection(address, port);
        NIOWorkingChannelContext objWorkingChannel = (NIOWorkingChannelContext) workerPool.register(channel, workingChannelMode);
        return objWorkingChannel;
    }

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
