//package service.middleware.linkage.center.client;
//
//import static service.middleware.linkage.center.repository.ServiceInformationRepository.addServiceInformationList;
//import static service.middleware.linkage.center.repository.ServiceInformationRepository.removeServiceInformationList;
//
//import java.util.List;
//
//import service.middleware.linkage.center.common.ServiceCenterUtils;
//import service.middleware.linkage.framework.access.domain.ServiceInformation;
//
///**
// * this interface is used in the client.
// * the configure management center will notify the client
// * that new service is joined or the service is not available
// * @author Smile
// *
// */
//public class ClientServiceImpl implements ClientService {
//
//	@Override
//	public String removeService(String serviceInfor) {
//		List<ServiceInformation> objServiceInformationList = ServiceCenterUtils.deserializeServiceInformationList(serviceInfor);
//		removeServiceInformationList(objServiceInformationList);
//		return null;
//	}
//
//	@Override
//	public String addService(String serviceInfor) {
//		List<ServiceInformation> objServiceInformationList = ServiceCenterUtils.deserializeServiceInformationList(serviceInfor);
//		addServiceInformationList(objServiceInformationList);
//		return "true";
//	}
//}
