package service.middleware.linkage.framework.io.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Created by Smile on 2015/7/4.
 */
public class LinkageSocketChannel {
    private static Logger logger = LoggerFactory.getLogger(LinkageSocketChannel.class);
    private final SocketChannel socketChannel;
    private final Object readLock;
    private final Object writeLock;
    private boolean isOpen;

    public LinkageSocketChannel(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.isOpen = true;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public Object getReadLock() {
        return readLock;
    }

    public Object getWriteLock() {
        return writeLock;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void close(){
        try {
            this.getSocketChannel().close();
            System.out.println("close the channel.");
            logger.debug("close the channel.");
        }
        catch (IOException ex){
            logger.error("close channel exception: " + ex.getMessage(), ex);
        }
        this.setIsOpen(false);
    }
}
