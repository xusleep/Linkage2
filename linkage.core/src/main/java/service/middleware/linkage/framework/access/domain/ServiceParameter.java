package service.middleware.linkage.framework.access.domain;

import com.google.gson.annotations.Expose;

/**
 * Created by Smile on 2015/7/15.
 */
public class ServiceParameter {
    private String parameterName;
    private Object parameterValue;
    private String parameterTypeName;

    @Expose private Class parameterType;

    public ServiceParameter(String parameterName, Class parameterType, Object parameterValue) {
        this.parameterName = parameterName;
        this.parameterTypeName = parameterType.getName();
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }

    public ServiceParameter(Class parameterType, Object parameterValue) {
        this.parameterTypeName = parameterType.getName();
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }

    public ServiceParameter(String parameterTypeName, Object parameterValue) throws ClassNotFoundException {
        this.parameterTypeName = parameterTypeName;
        this.parameterType = Class.forName(parameterTypeName);
        this.parameterValue = parameterValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterTypeName() {
        return parameterTypeName;
    }

    public Class getParameterType() {
        return this.parameterType;
    }

    public Object getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(Object parameterValue) {
        this.parameterValue = parameterValue;
    }
}
