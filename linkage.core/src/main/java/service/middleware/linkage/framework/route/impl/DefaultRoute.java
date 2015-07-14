package service.middleware.linkage.framework.route.impl;

import linkage.common.CommonUtils;
import service.middleware.linkage.framework.repository.WorkingChannelRepository;
import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;
import service.middleware.linkage.framework.route.Route;

import java.util.List;
import java.util.Random;

/**
 * default route choose , random choose
 * Created by hzxuzhonglin on 2015/7/14.
 */
public class DefaultRoute implements Route {

    @Override
    public WorkingChannelStoreBean chooseRoute(String address, int port) {
        List<WorkingChannelStoreBean> workingChannelStoreBeans = WorkingChannelRepository.getWorkingChannelStoreBeansByNetKey(CommonUtils.getNetKey(address, port));
        Random r = new Random();
        WorkingChannelStoreBean workingChannelStoreBean = workingChannelStoreBeans.get(r.nextInt(workingChannelStoreBeans.size()));
        return workingChannelStoreBean;
    }
}