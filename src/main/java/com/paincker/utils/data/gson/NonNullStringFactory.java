package com.paincker.utils.data.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.paincker.utils.L;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现 {@link NonNullString}。
 * Created by jzj on 2017/11/29.
 */
public class NonNullStringFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();

        // 读取Type中所有带有NonNullString注解的String类型Field，包括继承自父类的
        final List<Field> fields = findMatchedFields(rawType, String.class, NonNullString.class);

        if (fields != null && !fields.isEmpty()) {

            // 如果找到了，则包裹一层Adapter
            L.d(GsonUtils.TAG, "[NonNullStringFactory]  create type = %s, find fields count = %d", type, fields.size());
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    T t = delegate.read(in);
                    if (t != null) {
                        for (Field field : fields) {
                            // 如果Field为null，则修改为""
                            replaceFieldValue(field, t, null, "");
                        }
                    }
                    return t;
                }
            };
        }
        return null;
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean shouldSearch(Class clazz) {
        // 跳过不需要处理的类
        if (clazz == null || clazz == Object.class || clazz.isPrimitive() || clazz.isEnum() || clazz.isArray()) {
            L.d(GsonUtils.TAG, "[NonNullStringFactory]   skip class %s by properties", clazz);
            return false;
        }
        // 跳过Java和Android系统中的类
        String packageName = clazz.getPackage().getName();
        if (packageName.startsWith("java") || packageName.startsWith("android")) {
            L.d(GsonUtils.TAG, "[NonNullStringFactory]   skip class %s by package", clazz);
            return false;
        }
        // 只匹配特定的类、跳过其他第三方库的类……
        return true;
    }

    private static void replaceFieldValue(Field field, Object object, Object src, String dst) {
        try {
            Object o = field.get(object);
            if (o == src) {
                field.set(object, dst);
                L.d(GsonUtils.TAG, "[NonNullStringFactory]  modify field '%s'", field);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            L.e(e);
        }
    }

    private static List<Field> findMatchedFields(Class clazz, Class fieldType, Class<? extends Annotation> annotation) {
        List<Field> list = null;
        while (shouldSearch(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (fieldType == field.getType() && field.getAnnotation(annotation) != null) {
                    field.setAccessible(true);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }
}
