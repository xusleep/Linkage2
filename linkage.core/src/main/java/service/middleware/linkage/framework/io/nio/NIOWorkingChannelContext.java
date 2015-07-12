package service.middleware.linkage.framework.io.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.handlers.EventDistributionMaster;
import service.middleware.linkage.framework.io.Worker;
import service.middleware.linkage.framework.io.WorkingChannelContext;
import service.middleware.linkage.framework.io.WorkingChannelOperationResult;
import service.middleware.linkage.framework.io.WorkingChannelStrategy;
import service.middleware.linkage.framework.io.nio.strategy.WorkingChannelMode;
import service.middleware.linkage.framework.io.nio.strategy.mixed.NIOMixedStrategy;

import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * hold the object when request a connect,
 * the system will be wrapped by.
 *
 * @author zhonxu
 */
public class NIOWorkingChannelContext implements WorkingChannelContext {
    private final LinkageSocketChannel channel;
    private Worker worker;
    private String workingChannelCacheID;
    private SelectionKey key;
    private volatile WorkingChannelMode workingMode;
    private WorkingChannelStrategy workingChannelStrategy;
    private static Logger logger = LoggerFactory.getLogger(NIOWorkingChannelContext.class);
    private final EventDistributionMaster eventDistributionHandler;

    public NIOWorkingChannelContext(Channel channel, WorkingChannelMode workingMode, Worker worker, EventDistributionMaster eventDistributionHandler) {
        this.channel = new LinkageSocketChannel((SocketChannel) channel);
        this.worker = worker;
        this.eventDistributionHandler = eventDistributionHandler;
        setWorkingStrategy(workingMode);
    }

    /**
     * set the working strategy for the channel
     * the strategy is the way how the channel read or write the data into the channel
     */
    private void setWorkingStrategy(WorkingChannelMode theWorkingMode) {
        this.workingMode = theWorkingMode;
        this.workingChannelStrategy = new NIOMixedStrategy(this, eventDistributionHandler);
        logger.debug("working channel strategy is: " + theWorkingMode);
    }

    public WorkingChannelStrategy getWorkingChannelStrategy() {
        return this.workingChannelStrategy;
    }

    @Override
    public WorkingChannelOperationResult readChannel() {
        return getWorkingChannelStrategy().readChannel();
    }

    @Override
    public WorkingChannelOperationResult writeChannel() {
        return getWorkingChannelStrategy().writeChannel();
    }

    /**
     * close the channel
     *
     */
    public void closeWorkingChannel() {
        logger.debug("close working channel.");
        if(this.getKey() != null){
            key.cancel();
        }
        this.getLinkageSocketChannel().close();
    }

    public Worker getWorker() {
        return worker;
    }

    public LinkageSocketChannel getLinkageSocketChannel() {
        return channel;
    }

    public String getWoringChannelCacheID() {
        return workingChannelCacheID;
    }

    public void setWorkingChannelCacheID(String workingChannelCacheID) {
        this.workingChannelCacheID = workingChannelCacheID;
    }

    public WorkingChannelMode getWorkingChannelMode() {
        return workingMode;
    }

    public SelectionKey getKey() {
        return key;
    }

    public void setKey(SelectionKey key) {
        this.key = key;
    }
}
