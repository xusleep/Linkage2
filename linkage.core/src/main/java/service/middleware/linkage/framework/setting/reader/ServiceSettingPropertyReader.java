package service.middleware.linkage.framework.setting.reader;

import static service.middleware.linkage.framework.utils.ShareingData.SERVICE_ADDRESS;
import static service.middleware.linkage.framework.utils.ShareingData.SERVICE_PORT;
import static service.middleware.linkage.framework.utils.ShareingData.SERVICE_START_STRING;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import service.middleware.linkage.framework.setting.ServiceSettingEntity;

public class ServiceSettingPropertyReader implements ServiceSettingReader {
	private final String serviceAddress;
	private final int servicePort;
	private final List<ServiceSettingEntity> serviceList = new LinkedList<ServiceSettingEntity>();

	public ServiceSettingPropertyReader(String propertyFileName){
		InputStream inputStream = ServiceSettingReader.class.getClassLoader().getResourceAsStream(propertyFileName);
		//����һ��Properties���� 
        Properties properties = new Properties(); 
        //�����м���properties�ļ���Ϣ 
        try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
        this.serviceAddress = properties.getProperty(SERVICE_ADDRESS);
        String strServicePort = properties.getProperty(SERVICE_PORT);
        if(strServicePort != null && strServicePort != "")
        	this.servicePort = Integer.parseInt(strServicePort);
        else
        	this.servicePort = 0;
        for(int i = 1; i < 1000; i++)
        {
        	String serviceName = properties.getProperty(SERVICE_START_STRING + i + ".name");
        	if(serviceName != null && serviceName != ""){
        		ServiceSettingEntity entity = new ServiceSettingEntity();
            	entity.setServiceName(serviceName);
            	entity.setServiceGroup(properties.getProperty(SERVICE_START_STRING + i + ".group"));
            	entity.setServiceInterface(properties.getProperty(SERVICE_START_STRING + i + ".interface"));
            	entity.setServiceTarget(properties.getProperty(SERVICE_START_STRING + i + ".target"));
            	entity.setServiceVersion(properties.getProperty(SERVICE_START_STRING + i + ".version"));
            	serviceList.add(entity);
        	}
        	if(serviceName == null || serviceName == ""){
        		break;
        	}
        }
	}
	
	public String getServiceAddress() {
		return serviceAddress;
	}

	public int getServicePort() {
		return servicePort;
	}

	public List<ServiceSettingEntity> getServiceList() {
		return serviceList;
	}
}
