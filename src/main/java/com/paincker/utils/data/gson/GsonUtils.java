package com.paincker.utils.data.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Created by jzj on 2017/11/22.
 */
public class GsonUtils {

    public static final String TAG = "Gson";

    private static final Gson gson;

    static {
        // 优先级高的后注册
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new DeserializeActionFactory())
                .registerTypeAdapterFactory(new NonNullFieldFactory())
                .create();
    }

    public static Gson getGson() {
        return gson;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    public static <T> String toJson(T obj) {
        return gson.toJson(obj);
    }
}
