package com.paincker.utils.data.gson;

import com.google.gson.InstanceCreator;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>默认的InstanceCreator，创建非空对象</p>
 * Created by jzj on 2017/11/29.
 */
public class NonNullFieldConstructor implements InstanceCreator<Object> {
    /**
     * 保存基本类型及其默认值。基本类型默认值的内容不能被修改，因此可以重复利用，赋值给多个Field。
     */
    private static final Map<Class, Object> basicMap = new HashMap<>();
    /**
     * Gson的Constructor
     */
    private static final ConstructorConstructor constructor = new ConstructorConstructor(new HashMap<>());

    static {
        basicMap.put(Boolean.class, false);
        basicMap.put(Byte.class, (byte) 0);
        basicMap.put(Character.class, (char) 0);
        basicMap.put(Short.class, (short) 0);
        basicMap.put(Integer.class, 0);
        basicMap.put(Long.class, 0L);
        basicMap.put(Float.class, 0F);
        basicMap.put(Double.class, (double) 0);
        basicMap.put(String.class, "");
    }

    @Override
    public Object createInstance(Type type) {
        if (type instanceof Class) {
            Object o = basicMap.get(type);
            if (o != null) { // Integer.class
                return o;
            } else if (((Class) type).isArray()) { // String[].class
                return Array.newInstance($Gson$Types.getRawType(((Class) type).getComponentType()), 0);
            }
        } else if (type instanceof GenericArrayType) { // String[]
            return Array.newInstance($Gson$Types.getRawType(((GenericArrayType) type).getGenericComponentType()), 0);
        }
        // 其他类型使用constructor创建
        TypeToken<?> typeToken = TypeToken.get(type);
        return constructor.get(typeToken).construct();
    }
}
