package service.middleware.linkage.framework.setting.reader;

import java.util.List;

import service.middleware.linkage.framework.setting.ServiceSettingEntity;

/**
 * this class used to read the properties from the service
 * @author zhonxu
 *
 */
public interface ServiceSettingReader {
	public String getServiceAddress();
	public int getServicePort();
	public List<ServiceSettingEntity> getServiceList();
}
