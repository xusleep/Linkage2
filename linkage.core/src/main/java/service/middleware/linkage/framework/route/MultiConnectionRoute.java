package service.middleware.linkage.framework.route;

import service.middleware.linkage.framework.repository.domain.WorkingChannelStoreBean;

/**
 * Created by hzxuzhonglin on 2015/7/16.
 */
public interface MultiConnectionRoute {
    public WorkingChannelStoreBean chooseRoute(String address, int port);
}
