package service.middleware.linkage.center;

import java.util.LinkedList;
import java.util.List;

import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;

public interface ServiceCenter {
	public static final List<ServiceRegisterEntry> SERVICE_REGISTER_ENTRY_LIST = new LinkedList<ServiceRegisterEntry>();
	public static final List<ServiceRegisterEntry> serviceClientList = new LinkedList<ServiceRegisterEntry>();
	public Boolean register(String serviceInfor);
	public Boolean registerClientList(String serviceInfor);
	public List<ServiceRegisterEntry> getServiceList(String servicename);
	public Boolean removeServiceList(String serviceName);
}
