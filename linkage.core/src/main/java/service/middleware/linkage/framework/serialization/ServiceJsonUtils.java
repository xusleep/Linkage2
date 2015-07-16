package service.middleware.linkage.framework.serialization;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceResponse;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ServiceJsonUtils {
    private static Logger logger = LoggerFactory.getLogger(ServiceJsonUtils.class);
    private static Gson gson = null;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ServiceRequest.class, new JsonDeserializer<ServiceRequest>() {
                    @Override
                    public ServiceRequest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
                            jsonDeserializationContext) throws JsonParseException {
                        ServiceRequest serviceRequestReturn = new ServiceRequest();
                        serviceRequestReturn.setServiceName(jsonElement.getAsJsonObject().get("serviceName").getAsString());
                        serviceRequestReturn.setMethodName(jsonElement.getAsJsonObject().get("methodName").getAsString());
                        serviceRequestReturn.setVersion(jsonElement.getAsJsonObject().get("version").getAsString());
                        serviceRequestReturn.setGroup(jsonElement.getAsJsonObject().get("group").getAsString());
                        JsonArray jsonArrayArgs = (JsonArray) jsonElement.getAsJsonObject().get("serviceParameters");
                        List<ServiceParameter> parameterList = new LinkedList<ServiceParameter>();
                        serviceRequestReturn.setServiceParameters(parameterList);
                        for (int i = 0; i < jsonArrayArgs.size(); i++) {
                            JsonObject jsonObject = jsonArrayArgs.get(i).getAsJsonObject();
                            String name = jsonObject.get("name").getAsString();
                            String type1 = jsonObject.get("type").getAsString();
                            if (type1.contains("int")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsInt()));
                            } else if (type1.contains("Integer")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsInt()));
                            } else if (type1.contains("String")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsString()));
                            } else if (type1.contains("Double")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsDouble()));
                            } else if (type1.contains("double")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsDouble()));
                            } else if (type1.contains("Long")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsLong()));
                            } else if (type1.contains("long")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsLong()));
                            } else if (type1.contains("Float")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsFloat()));
                            } else if (type1.contains("float")) {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsFloat()));
                            } else {
                                parameterList.add(new ServiceParameter(name, type1, jsonObject.get("value").getAsString()));
                            }
                        }
                        return serviceRequestReturn;
                    }
                }

        );
        gson = gsonBuilder.create();
    }

    public static String serializeResult(Object result) {
        return gson.toJson(result);
    }


    /**
     * deserialize request domain
     *
     * @return
     */
    public static <T> T deserializeResult(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    /**
     * serialize request domain
     *
     * @param request
     * @return
     */
    public static String serializeRequest(ServiceRequest request) {
        return gson.toJson(request);
    }

    /**
     * deserialize request domain
     *
     * @return
     */
    public static ServiceRequest deserializeRequest(String receiveData) {
        return gson.fromJson(receiveData, ServiceRequest.class);
    }

    /**
     * serialize Response domain
     *
     * @return
     */
    public static String serializeResponse(ServiceResponse response) {
        return gson.toJson(response);
    }

    /**
     * deserialize Response domain
     *
     * @return
     */
    public static ServiceResponse deserializeResponse(String receiveData) {
        return gson.fromJson(receiveData, ServiceResponse.class);
    }

    /**
     * get value from the parameters
     *
     * @param serviceParameters
     * @return
     */
    public static Object[] getParameterValues(List<ServiceParameter> serviceParameters) {
        Object[] values = new Object[serviceParameters.size()];
        for (int i = 0; i < serviceParameters.size(); i++) {
            ServiceParameter serviceParameter = serviceParameters.get(i);
            values[i] = serviceParameter.getValue();
        }
        return values;
    }

    /**
     * get types from the parameters
     *
     * @param serviceParameters
     * @return
     */
    public static Class<?>[] getParameterTypes(List<ServiceParameter> serviceParameters) {
        Class<?>[] types = new Class<?>[serviceParameters.size()];
        for (int i = 0; i < serviceParameters.size(); i++) {
            ServiceParameter serviceParameter = serviceParameters.get(i);
            if (serviceParameter.getType().contains("int")) {
                types[i] = int.class;
            } else if (serviceParameter.getType().contains("Integer")) {
                types[i] = Integer.class;
            } else if (serviceParameter.getType().contains("String")) {
                types[i] = String.class;
            } else if (serviceParameter.getType().contains("Double")) {
                types[i] = Double.class;
            } else if (serviceParameter.getType().contains("double")) {
                types[i] = double.class;
            } else if (serviceParameter.getType().contains("Long")) {
                types[i] = Long.class;
            } else if (serviceParameter.getType().contains("long")) {
                types[i] = long.class;
            } else if (serviceParameter.getType().contains("Float")) {
                types[i] = Float.class;
            } else if (serviceParameter.getType().contains("float")) {
                types[i] = float.class;
            } else {
                types[i] = String.class;
            }
        }
        return types;
    }
}
