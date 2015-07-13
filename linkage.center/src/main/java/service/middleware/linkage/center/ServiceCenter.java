package service.middleware.linkage.center;

import java.util.LinkedList;
import java.util.List;

import service.middleware.linkage.framework.access.domain.ServiceInformation;

public interface ServiceCenter {
	public static final List<ServiceInformation> serviceInformationList = new LinkedList<ServiceInformation>();
	public static final List<ServiceInformation> serviceClientList = new LinkedList<ServiceInformation>();
	public Boolean register(String serviceInfor);
	public Boolean registerClientList(String serviceInfor);
	public List<ServiceInformation> getServiceList(String servicename);
	public Boolean removeServiceList(String serviceName);
}
