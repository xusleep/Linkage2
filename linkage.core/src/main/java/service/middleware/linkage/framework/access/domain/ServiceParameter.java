package service.middleware.linkage.framework.access.domain;

/**
 * Created by Smile on 2015/7/15.
 */
public class ServiceParameter {
    private String name;
    private String type;
    private Object value;

    public ServiceParameter(String name, Class type, Object value) {
        this.name = name;
        this.type = type.getName();
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ServiceParameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
