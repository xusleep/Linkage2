package service.middleware.linkage.framework.repository;

import linkage.common.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class WorkingChannelRepository {
    private static final List<WorkingChannelStoreBean> WORKING_CHANNEL_STORE_BEAN_LIST = new CopyOnWriteArrayList<WorkingChannelStoreBean>();
    private static Logger logger = LoggerFactory.getLogger(WorkingChannelRepository.class);
    /**
     * add working channel entity
     * @param workingChannelStoreBean
     */
    public static void addWorkingChannelStoreBean(WorkingChannelStoreBean workingChannelStoreBean){
        WORKING_CHANNEL_STORE_BEAN_LIST.add(workingChannelStoreBean);
    }

    /**
     * add working channel entity list
     * @param workingChannelStoreBeanList
     */
    public static void addWorkingChannelStoreBeanList(List<WorkingChannelStoreBean> workingChannelStoreBeanList){
        workingChannelStoreBeanList.addAll(workingChannelStoreBeanList);
    }

    /**
     * remove working channel entity
     * @param workingChannelStoreBean
     */
    public static void removeWorkingChannelStoreBean(WorkingChannelStoreBean workingChannelStoreBean){
        WORKING_CHANNEL_STORE_BEAN_LIST.remove(workingChannelStoreBean);
    }

    /**
     * remove working channel entity
     * @param workingChannelStoreBeanList
     */
    public static void removeWorkingChannelStoreBeanList(List<WorkingChannelStoreBean> workingChannelStoreBeanList){
        workingChannelStoreBeanList.removeAll(workingChannelStoreBeanList);
    }

    /**
     * remove working channel entity by id
     * @param id
     */
    public static void removeWorkingChannelStoreBean(String id){
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
    public static WorkingChannelStoreBean getWorkingChannelStoreBeanByChannelId(String id){
        for(WorkingChannelStoreBean workingChannelStoreBean : WorkingChannelRepository.WORKING_CHANNEL_STORE_BEAN_LIST){
            if(StringUtils.equals(workingChannelStoreBean.getWorkingChannelContext().getId(), id)){
                return workingChannelStoreBean;
            }
        }
        return null;
    }

    /**
     * 获得working channel
     * @param netKey
     * @return
     */
    public static List<WorkingChannelStoreBean> getWorkingChannelStoreBeansByNetKey(String netKey){
        List<WorkingChannelStoreBean> workingChannelStoreBeans = new LinkedList<>();
        for(WorkingChannelStoreBean workingChannelStoreBean : WorkingChannelRepository.WORKING_CHANNEL_STORE_BEAN_LIST){
            try {
                SocketChannel socketChannel = workingChannelStoreBean.getWorkingChannelContext().getLinkageSocketChannel().getSocketChannel();
                if(workingChannelStoreBean.getWorkingChannelContext().getLinkageSocketChannel().isOpen()) {
                    try {
                        InetSocketAddress socketAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                        if (StringUtils.equals(CommonUtils.getNetKey(socketAddress), netKey)) {
                            workingChannelStoreBeans.add(workingChannelStoreBean);
                        }
                    }
                    catch (Exception ex){
                        logger.error(ex.getMessage(), ex);
                    }
                }else{
                    logger.debug("channel is closed");
                }
            }
            catch (Exception ex){
                logger.error(ex.getMessage(), ex);
            }
        }
        return workingChannelStoreBeans;
    }
}
