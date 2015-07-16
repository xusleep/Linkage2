package service.middleware.linkage.framework.io.nio.connection;

import linkage.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.exception.ServiceException;
import service.middleware.linkage.framework.io.WorkerPool;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.repository.WorkingChannelRepository;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.route.MultiConnectionRoute;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

public class NIOConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(NIOConnectionManager.class);

    private final MultiConnectionRoute multiConnectionRoute;
    private final WorkingChannelMode workingChannelMode;
    private final WorkerPool workerPool;

    public NIOConnectionManager(MultiConnectionRoute multiConnectionRoute, WorkingChannelMode workingChannelMode, WorkerPool workerPool){
        this.multiConnectionRoute = multiConnectionRoute;
        this.workingChannelMode = workingChannelMode;
        this.workerPool = workerPool;
    }

    /**
     * connect to the service
     *
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public WorkingChannelStoreBean connect(String address, int port) throws IOException {
        WorkingChannelStoreBean workingChannelStoreBean = multiConnectionRoute.chooseRoute(address, port);
        if(workingChannelStoreBean == null) {
            synchronized (this) {
                // double check, make sure only one connection connected.
                workingChannelStoreBean = multiConnectionRoute.chooseRoute(address, port);
                if(workingChannelStoreBean == null) {
                    SocketChannel channel = createNIOConnection(address, port);
                    workingChannelStoreBean = new WorkingChannelStoreBean(workerPool.register(channel, workingChannelMode));
                    WorkingChannelRepository.addWorkingChannelStoreBean(workingChannelStoreBean);
                    return workingChannelStoreBean;
                }
                else{
                    return workingChannelStoreBean;
                }
            }
        }
        else{
            return workingChannelStoreBean;
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
