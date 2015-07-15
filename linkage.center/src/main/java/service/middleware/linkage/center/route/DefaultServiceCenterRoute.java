package service.middleware.linkage.center.route;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import linkage.common.JsonUtils;
import org.apache.commons.lang.StringUtils;
import service.middleware.linkage.center.respository.ServiceCenterCommonRepository;
import service.middleware.linkage.center.respository.ServiceRegisterRepository;
import service.middleware.linkage.framework.access.RequestCallback;
import service.middleware.linkage.framework.access.ServiceAccess;
import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Smile on 2015/7/15.
 */
public class DefaultServiceCenterRoute implements ServiceCenterRoute {

    private final ServiceAccess serviceAccess;
    private final String centerAddress;
    private final int centerPort;

    public DefaultServiceCenterRoute(ServiceAccess serviceAccess, String centerAddress, int centerPort){
        this.serviceAccess = serviceAccess;
        this.centerPort = centerPort;
        this.centerAddress = centerAddress;
    }

    @Override
    public ServiceRegisterEntry chooseRouteByServiceName(String serviceName) {
        List<ServiceRegisterEntry> serviceRegisterEntryList = ServiceRegisterRepository.getServiceInformationList(serviceName);
        if(serviceRegisterEntryList == null || serviceRegisterEntryList.size() == 0){
            List<ServiceParameter> serviceParameters = new LinkedList<>();
            final CountDownLatch arrived = new CountDownLatch(1);
            serviceAccess.requestService(centerAddress, centerPort,
                    ServiceCenterCommonRepository.SERVICE_CENTER_GET_SERVICE_ID, serviceParameters, new RequestCallback(){
                        @Override
                        public void runCallback(String jsonResult) {
                            if(!StringUtils.isBlank(jsonResult)){
//                                Gson gson = new Gson();
//                                List<ServiceRegisterEntry> registerEntryList =
//                                        gson.fromJson(jsonResult, new TypeToken<List<ServiceRegisterEntry>>(){}.getType());
                                List<ServiceRegisterEntry> registerEntryList = JsonUtils.fromJson(jsonResult);
                                ServiceRegisterRepository.addServiceInformationList(registerEntryList);
                            }
                            arrived.countDown();
                        }

                        @Override
                        public void runException(Exception ex) {
                            arrived.countDown();
                        }
                    });
            try {
                arrived.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
