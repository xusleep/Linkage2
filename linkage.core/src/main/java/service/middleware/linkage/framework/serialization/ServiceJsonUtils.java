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
        gsonBuilder.registerTypeAdapter(Class.class, new JsonDeserializer<Class>() {
            @Override
            public Class deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
                    jsonDeserializationContext) throws JsonParseException {
                try {
                    Class t = Class.forName(jsonElement.getAsString());
                    return t;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        gsonBuilder.registerTypeAdapter(Class.class, new JsonSerializer<Class>() {
            @Override
            public JsonElement serialize(Class aClass, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(aClass.getName());
            }
        });
        gsonBuilder.registerTypeAdapter(ServiceParameter.class, new JsonDeserializer<ServiceParameter>() {
                    @Override
                    public ServiceParameter deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
                            jsonDeserializationContext) throws JsonParseException {
                        try {
                            ServiceParameter serviceParameter = null;
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            String name = jsonObject.get("name").getAsString();
                            String type1 = jsonObject.get("type").getAsString();
                            if (type1.equals(int.class.getName())) {
                                serviceParameter = new ServiceParameter(name, int.class, jsonObject.get("value").getAsInt());
                            } else if (type1.equals(Integer.class.getName())) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsInt());
                            } else if (type1.equals(String.class.getName())) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsString());
                            } else if (type1.equals(Double.class.getName())) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsDouble());
                            } else if (type1.contains("double")) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsDouble());
                            } else if (type1.contains("Long")) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsLong());
                            } else if (type1.contains("long")) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsLong());
                            } else if (type1.contains("Float")) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsFloat());
                            } else if (type1.contains("float")) {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsFloat());
                            } else {
                                serviceParameter = new ServiceParameter(name, type1, jsonObject.get("value").getAsString());
                            }
                            return serviceParameter;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return null;
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

    public static void main(String[] args) {
        ServiceRequest request = new ServiceRequest();
        request.set
    }
}
