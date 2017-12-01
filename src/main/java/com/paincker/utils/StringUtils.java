package com.paincker.utils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by jzj on 2017/11/22.
 */
public class StringUtils {

    private static final HashSet<Object> basicSet = new HashSet<>();

    static {
        basicSet.add(Boolean.class);
        basicSet.add(Byte.class);
        basicSet.add(Character.class);
        basicSet.add(Short.class);
        basicSet.add(Integer.class);
        basicSet.add(Long.class);
        basicSet.add(Float.class);
        basicSet.add(Double.class);
        basicSet.add(String.class);
        basicSet.add(Object.class);
    }

    /**
     * 通过反射将对象格式化成字符串，主要用于Log输出
     */
    public static String objectDeepToString(Object o) {
        TabStringBuilder builder = new TabStringBuilder();
        objectDeepToString(builder, o);
        return builder.toString();
    }

    private static void objectDeepToString(TabStringBuilder builder, Object o) {
        if (o == null) {
            builder.append("null");
            return;
        }

        if (builder.getTabCount() > 15) { // max depth
            builder.append(o);
            return;
        }

        final Class<?> clazz = o.getClass();
        if (o instanceof String) {
            builder.append('"').append(o).append('"');
            return;
        }
        if (clazz.isPrimitive() || basicSet.contains(clazz)) {
            builder.append(o);
            return;
        }
        if (clazz.isArray()) {
            builder.append('[');
            int length = Array.getLength(o);
            if (length > 0) {
                builder.incTab();
                for (int i = 0; i < length; i++) {
                    if (i > 0) {
                        builder.append(',');
                    }
                    builder.newLine();
                    objectDeepToString(builder, Array.get(o, i));
                }
                builder.decTab();
                builder.newLine();
            }
            builder.append(']');
            return;
        }
        if (o instanceof Collection) {
            builder.append('[');
            Collection collection = (Collection) o;
            if (!collection.isEmpty()) {
                builder.incTab();
                boolean first = true;
                for (Object item : collection) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(',');
                    }
                    builder.newLine();
                    objectDeepToString(builder, item);
                }
                builder.decTab();
                builder.newLine();
            }
            builder.append(']');
            return;
        }
        if (o instanceof Map) {
            builder.append('{');
            Map map = (Map) o;
            if (!((Map) o).isEmpty()) {
                builder.incTab();
                boolean first = true;
                //noinspection unchecked
                for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(',');
                    }
                    builder.newLine().append(entry.getKey()).append(": ");
                    objectDeepToString(builder, entry.getValue());
                }
                builder.decTab();
                builder.newLine();
            }
            builder.append('}');
            return;
        }
        builder.append('{');
        ArrayList<Field> fields = getAllFields(clazz, true);
        if (!fields.isEmpty()) {
            builder.incTab();
            boolean first = true;
            for (Field field : fields) {
                if (first) {
                    first = false;
                } else {
                    builder.append(',');
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(o);
                    builder.newLine().append(field.getName()).append(": ");
                    objectDeepToString(builder, value);
                } catch (IllegalAccessException e) {
                    L.e(e);
                }
            }
            builder.decTab();
            builder.newLine();
        }
        builder.append('}');
    }

    /**
     * 获取Class的所有Field，包括继承的
     *
     * @param clazz           Class
     * @param superClassFirst 继承自父类的Field放前面
     */
    public static ArrayList<Field> getAllFields(Class clazz, boolean superClassFirst) {
        ArrayList<Field> list = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                if (superClassFirst) {
                    list.addAll(0, Arrays.asList(fields));
                } else {
                    Collections.addAll(list, fields);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public static String readAsString(File file) {
        try {
            return readAsString(new FileInputStream(file), "utf-8");
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static String readAsString(InputStream stream) {
        return readAsString(stream, "utf-8");
    }

    public static String readAsString(InputStream is, String encode) {
        if (is == null) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, encode));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            L.e(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    L.e(e);
                }
            }
        }
        return null;
    }

    /**
     * 换行时带有缩进的StringBuilder
     */
    public static class TabStringBuilder {

        private final StringBuilder mBuilder = new StringBuilder();
        private final Tab mTab = new Tab();

        /**
         * 获取当前缩进数量，初始值为0，可能为负数
         */
        public int getTabCount() {
            return mTab.getCount();
        }

        /**
         * 增加缩进
         */
        public TabStringBuilder incTab() {
            mTab.inc();
            return this;
        }

        /**
         * 减少缩进
         */
        public TabStringBuilder decTab() {
            mTab.dec();
            return this;
        }

        public TabStringBuilder append(char c) {
            if (c == '\n' && mTab.notEmpty()) {
                mBuilder.append(c).append(mTab.get());
            } else {
                mBuilder.append(c);
            }
            return this;
        }

        public TabStringBuilder append(CharSequence csq) {
            mBuilder.append(replaceTab(csq));
            return this;
        }

        public TabStringBuilder append(Object o) {
            mBuilder.append(replaceTab(String.valueOf(o)));
            return this;
        }

        public TabStringBuilder newLine() {
            return append('\n');
        }

        private CharSequence replaceTab(CharSequence csq) {
            if (csq != null && mTab.notEmpty()) {
                return csq.toString().replace("\n", "\n" + mTab.get());
            } else {
                return csq;
            }
        }

        @Override
        public String toString() {
            return mBuilder.toString();
        }
    }

    private static class Tab {

        public static final String TAB = "  ";

        private int mCount = 0;
        private String mString = "";

        private void refresh() {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < mCount; i++) {
                s.append(TAB);
            }
            mString = s.toString();
        }

        public void inc() {
            mCount++;
            refresh();
        }

        public void dec() {
            mCount--;
            refresh();
        }

        public int getCount() {
            return mCount;
        }

        public String get() {
            return mString;
        }

        public boolean notEmpty() {
            return mString.length() > 0;
        }
    }
}
