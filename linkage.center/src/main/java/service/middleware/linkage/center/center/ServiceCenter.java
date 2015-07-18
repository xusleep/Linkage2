package service.middleware.linkage.center.center;

import java.util.LinkedList;
import java.util.List;

import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;

public interface ServiceCenter {
	public Boolean register(String serviceInfor);
	public Boolean registerClientList(String serviceInfor);
	public List<ServiceRegisterEntry> getServiceList(String servicename);
	public Boolean removeServiceList(String serviceName);
}
