package service.middleware.linkage.center.center;

import linkage.common.JsonUtils;
import service.middleware.linkage.center.respository.ServiceRegisterRepository;
import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;

import java.util.List;

/**
 *
 */
public class ServiceCenterImpl implements ServiceCenter {

    @Override
    public Boolean register(String serviceInfor) {
        // TODO Auto-generated method stub
        List<ServiceRegisterEntry> objServiceInformation = JsonUtils.fromJson(serviceInfor, List.class);
        ServiceRegisterRepository.addServiceInformationList(objServiceInformation);
        System.out.println("register service count = " + objServiceInformation.size());
        return true;
    }

    @Override
    public Boolean registerClientList(String serviceInfor) {
//		ServiceRegisterEntry objServiceInformation = ServiceCenterUtils.deserializeServiceInformation(serviceInfor);
//		ServiceCenter.serviceClientList.add(objServiceInformation);
//		System.out.println("serviceInfor = " + serviceInfor);
//		System.out.println("service client count = " + ServiceCenter.SERVICE_REGISTER_ENTRY_LIST.size());
        return true;
    }

    @Override
    public List<ServiceRegisterEntry> getServiceList(String serviceName) {
        return ServiceRegisterRepository.getServiceInformationList(serviceName);
    }

    @Override
    public Boolean removeServiceList(String serviceName) {
        return null;
    }
}
