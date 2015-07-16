package service.middleware.linkage.center.respository;

import org.apache.commons.lang.StringUtils;
import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Smile on 2015/7/15.
 */
public class ServiceRegisterRepository {
    private static final List<ServiceRegisterEntry> serviceListCache = new CopyOnWriteArrayList<>();

    /**
     * add the service information domain to the repository
     * @param objServiceRegisterEntry
     */
    public static void addServiceInformation(ServiceRegisterEntry objServiceRegisterEntry){
        if(objServiceRegisterEntry != null)
            serviceListCache.add(objServiceRegisterEntry);
    }

    /**
     * add the service information domain to the repository
     * @param objServiceRegisterEntryList
     */
    public static void addServiceInformationList(List<ServiceRegisterEntry> objServiceRegisterEntryList){
        if(objServiceRegisterEntryList != null)
            serviceListCache.addAll(objServiceRegisterEntryList);
    }

    /**
     * remove the service information domain from the repository
     * @param objServiceRegisterEntry
     */
    public static void removeServiceInformation(ServiceRegisterEntry objServiceRegisterEntry){
        if(objServiceRegisterEntry != null)
            serviceListCache.remove(objServiceRegisterEntry);
    }

    /**
     * remove the service information domain list from the repository
     * @param objServiceRegisterEntryList
     */
    public static void removeServiceInformationList(List<ServiceRegisterEntry> objServiceRegisterEntryList){
        if(objServiceRegisterEntryList != null)
            serviceListCache.removeAll(objServiceRegisterEntryList);
    }


    /**
     * clear the service information domain
     */
    public static void clear(){
        serviceListCache.clear();
    }

    /**
     * get the service list from the repository by service name
     * @param serviceName
     * @return
     */
    public static List<ServiceRegisterEntry> getServiceInformationList(String serviceName){
        List<ServiceRegisterEntry> resultList = new LinkedList<ServiceRegisterEntry>();
        if(StringUtils.isEmpty(serviceName))
            return resultList;
        if(serviceListCache == null || serviceListCache.size() == 0)
            return resultList;
        for(ServiceRegisterEntry objServiceRegisterEntry :serviceListCache)
        {
            if(serviceName.equals(objServiceRegisterEntry.getServiceName()))
            {
                resultList.add(objServiceRegisterEntry);
            }
        }
        return resultList;
    }
}
