package service.middleware.linkage.center;

import java.util.LinkedList;
import java.util.List;

import service.middleware.linkage.framework.access.domain.ServiceInformation;

public interface ServiceCenter {
	public static final List<ServiceInformation> serviceInformationList = new LinkedList<ServiceInformation>();
	public static final List<ServiceInformation> serviceClientList = new LinkedList<ServiceInformation>();
	public String register(String serviceInfor);
	public String registerClientList(String serviceInfor);
	public String getServiceList(String servicename);
	public String removeServiceList(String serviceName);
}
