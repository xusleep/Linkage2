package service.middleware.linkage.framework.serialization;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.middleware.linkage.framework.access.domain.ServiceParameter;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceResponse;

import java.lang.reflect.Type;
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
                            String parameterName = jsonObject.get("parameterName").getAsString();
                            String parameterTypeName = jsonObject.get("parameterTypeName").getAsString();
                            if (parameterTypeName.equals(int.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, int.class, jsonObject.get("parameterValue").getAsInt());
                            } else if (parameterTypeName.equals(Integer.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, Integer.class, jsonObject.get("parameterValue").getAsInt());
                            } else if (parameterTypeName.equals(String.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, String.class, jsonObject.get("parameterValue").getAsString());
                            } else if (parameterTypeName.equals(Double.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, Double.class, jsonObject.get("parameterValue").getAsDouble());
                            } else if (parameterTypeName.equals(double.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, double.class, jsonObject.get("parameterValue").getAsDouble());
                            } else if (parameterTypeName.equals(Long.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, Long.class, jsonObject.get("parameterValue").getAsLong());
                            } else if (parameterTypeName.equals(long.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, long.class, jsonObject.get("parameterValue").getAsLong());
                            } else if (parameterTypeName.equals(Float.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, Float.class, jsonObject.get("parameterValue").getAsFloat());
                            } else if (parameterTypeName.equals(float.class.getName())) {
                                serviceParameter = new ServiceParameter(parameterName, float.class, jsonObject.get("parameterValue").getAsFloat());
                            } else {
                                serviceParameter = new ServiceParameter(parameterName, String.class, jsonObject.get("parameterValue").getAsString());
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
            values[i] = serviceParameters.get(i).getParameterValue();
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
            types[i] = serviceParameters.get(i).getParameterType();
        }
        return types;
    }

    public static void main(String[] args) {
    }
}
