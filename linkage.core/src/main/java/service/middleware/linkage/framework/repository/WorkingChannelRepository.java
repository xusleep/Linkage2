package service.middleware.linkage.framework.repository;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class WorkingChannelRepository {
    private static final List<WorkingChannelStoreBean> WORKING_CHANNEL_STORE_BEAN_LIST = new LinkedList<WorkingChannelStoreBean>();
    private static Logger logger = LoggerFactory.getLogger(WorkingChannelRepository.class);
    /**
     * add working channel entity
     * @param workingChannelStoreBean
     */
    public static synchronized void addWorkingChannelStoreBean(WorkingChannelStoreBean workingChannelStoreBean){
        WORKING_CHANNEL_STORE_BEAN_LIST.add(workingChannelStoreBean);
    }

    /**
     * add working channel entity list
     * @param workingChannelStoreBeanList
     */
    public static synchronized void addWorkingChannelStoreBeanList(List<WorkingChannelStoreBean> workingChannelStoreBeanList){
        workingChannelStoreBeanList.addAll(workingChannelStoreBeanList);
    }

    /**
     * remove working channel entity
     * @param workingChannelStoreBean
     */
    public static synchronized void removeWorkingChannelStoreBean(WorkingChannelStoreBean workingChannelStoreBean){
        WORKING_CHANNEL_STORE_BEAN_LIST.remove(workingChannelStoreBean);
    }

    /**
     * remove working channel entity
     * @param workingChannelStoreBeanList
     */
    public static synchronized void removeWorkingChannelStoreBeanList(List<WorkingChannelStoreBean> workingChannelStoreBeanList){
        workingChannelStoreBeanList.removeAll(workingChannelStoreBeanList);
    }

    /**
     * remove working channel entity by id
     * @param id
     */
    public static synchronized void removeWorkingChannelStoreBean(String id){
        List<WorkingChannelStoreBean> workingChannelStoreBeanList = new LinkedList<>();
        for(WorkingChannelStoreBean workingChannelStoreBean : WorkingChannelRepository.WORKING_CHANNEL_STORE_BEAN_LIST){
            if(StringUtils.equals(workingChannelStoreBean.getWorkingChannelContext().getId(), id)){
                workingChannelStoreBeanList.add(workingChannelStoreBean);
            }
        }
        removeWorkingChannelStoreBeanList(workingChannelStoreBeanList);
    }

    /**
     * 获得working channel
     * @param id
     * @return
     */
    public static synchronized WorkingChannelStoreBean getWorkingChannelStoreBeanByChannelId(String id){
        for(WorkingChannelStoreBean workingChannelStoreBean : WorkingChannelRepository.WORKING_CHANNEL_STORE_BEAN_LIST){
            if(StringUtils.equals(workingChannelStoreBean.getWorkingChannelContext().getId(), id)){
                return workingChannelStoreBean;
            }
        }
        return null;
    }

    /**
     * 获得working channel
     * @param id
     * @return
     */
    public static synchronized List<WorkingChannelStoreBean> getWorkingChannelStoreBeansByNetKey(String netKey){
        List<WorkingChannelStoreBean> workingChannelStoreBeans = new LinkedList<>();
        for(WorkingChannelStoreBean workingChannelStoreBean : WorkingChannelRepository.WORKING_CHANNEL_STORE_BEAN_LIST){
            try {
                SocketChannel socketChannel = workingChannelStoreBean.getWorkingChannelContext().getLinkageSocketChannel().getSocketChannel();
                InetSocketAddress socketAddress = (InetSocketAddress)socketChannel.getRemoteAddress();
                String key = socketAddress.getHostString() + "_" + socketAddress.getPort();
                if (StringUtils.equals(key, netKey)) {
                    workingChannelStoreBeans.add(workingChannelStoreBean);
                }
            }
            catch (Exception ex){
                logger.error(ex.getMessage(), ex);
            }
        }
        return workingChannelStoreBeans;
    }
}
