package service.middleware.linkage.framework.access;

/**
 * Created by Smile on 2015/7/14.
 */
public interface RequestCallback {
    public void runCallback(Object result);
    public void runException(Exception ex);
}
