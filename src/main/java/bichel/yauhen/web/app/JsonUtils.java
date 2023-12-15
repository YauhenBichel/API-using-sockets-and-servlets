package bichel.yauhen.web.app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Utility class for json operations
 */
public final class JsonUtils {
    /**
     * Wraps response body for response with not OK 200 HTTP status
     * @param param invalid param from request
     * @return String of response body in json format
     */
    public static String wrapInvalidResponseToJson(String param) {
        JsonObject jsonResponseObject = new JsonObject();
        jsonResponseObject.addProperty("success", false);
        jsonResponseObject.addProperty(param, "invalid");
        return jsonResponseObject.toString();
    }

    public static <T> String getJsonString(T obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
