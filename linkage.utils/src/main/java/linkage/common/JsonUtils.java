package linkage.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Json×ª»»¹¤¾ß
 */
public class JsonUtils {
    private static Gson gson = new Gson();

    public JsonUtils() {
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public static JsonObject parseJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    public static JsonArray parseJsonToArray(String json) {
        JsonParser parser = new JsonParser();
        JsonArray asJsonArray = parser.parse(json).getAsJsonArray();
        return asJsonArray;
    }

    public static Map<String, Object> toMap(String json) {
        return toMap(parseJson(json));
    }

    public static Map<String, Object> toMap(JsonObject json) {
        HashMap map = new HashMap();
        Set entrySet = json.entrySet();
        Iterator iter = entrySet.iterator();

        while(iter.hasNext()) {
            Entry entry = (Entry)iter.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if(value instanceof JsonArray) {
                map.put(key, toList((JsonArray)value));
            } else if(value instanceof JsonObject) {
                map.put(key, toMap((JsonObject)value));
            } else {
                map.put(key, value);
            }
        }

        return map;
    }

    public static Properties toProperties(JsonObject json) {
        Properties properties = new Properties();
        Set entrySet = json.entrySet();
        Iterator iter = entrySet.iterator();

        while(iter.hasNext()) {
            Entry entry = (Entry)iter.next();
            String key = (String)entry.getKey();
            JsonElement value = (JsonElement)entry.getValue();
            if(value instanceof JsonArray) {
                properties.put(key, toList((JsonArray)value));
            } else if(value instanceof JsonObject) {
                properties.put(key, toMap((JsonObject)value));
            } else {
                properties.put(key, value.getAsString());
            }
        }

        return properties;
    }

    public static List<Object> toList(JsonArray json) {
        ArrayList list = new ArrayList();

        for(int i = 0; i < json.size(); ++i) {
            JsonElement value = json.get(i);
            if(value instanceof JsonArray) {
                list.add(toList((JsonArray)value));
            } else if(value instanceof JsonObject) {
                list.add(toMap((JsonObject)value));
            } else {
                list.add(value);
            }
        }

        return list;
    }
}