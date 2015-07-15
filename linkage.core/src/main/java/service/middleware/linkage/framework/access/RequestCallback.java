package service.middleware.linkage.framework.access;

import service.middleware.linkage.framework.access.domain.ServiceResponse;

/**
 * Created by Smile on 2015/7/14.
 */
public interface RequestCallback {
    public void runCallback(String jsonResult);
    public void runException(Exception ex);
}
