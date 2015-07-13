package service.middleware.linkage.framework.repository;

import org.apache.commons.lang.StringUtils;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hzxuzhonglin on 2015/7/13.
 */
public class WorkingChannelRepository {
    private static final List<WorkingChannelStoreBean> WORKING_CHANNEL_STORE_BEAN_LIST = new LinkedList<WorkingChannelStoreBean>();

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
     * »ñµÃworking channel
     * @param id
     * @return
     */
    public static synchronized WorkingChannelStoreBean getWorkingChannelStoreBean(String id){
        for(WorkingChannelStoreBean workingChannelStoreBean : WorkingChannelRepository.WORKING_CHANNEL_STORE_BEAN_LIST){
            if(StringUtils.equals(workingChannelStoreBean.getWorkingChannelContext().getId(), id)){
                return workingChannelStoreBean;
            }
        }
        return null;
    }
}
