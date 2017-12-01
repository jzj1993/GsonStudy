package com.paincker.testcase.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.paincker.utils.StringUtils;
import com.paincker.utils.data.gson.NonNullField;
import com.paincker.utils.data.gson.NonNullFieldFactory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by jzj on 2017/12/1.
 */
public class TestNonNullField {

    public static class BaseData<T> {
        // 泛型参数类型；成员变量继承
        @NonNullField
        T type;
    }

    public static class Data extends BaseData<String> {

        String nullField;
        @NonNullField
        String string;
        @NonNullField
        private String[] array;
        @NonNullField
        private List<String> list;
        @NonNullField
        private Map<String, Object> map;
        @NonNullField
        private String[][][] multiArray;
        @NonNullField
        private List<List<String>> lists;

        // 自定义InstanceCreator；
        // 嵌套class(Extra内部又定义了NonNullField)
        @NonNullField(ExtraCreator.class)
        Extra extra;
    }

    public static class Extra {
        final String name;
        @NonNullField
        Integer integer;

        public Extra(String name) {
            this.name = name;
        }
    }

    public static class ExtraCreator implements InstanceCreator<Extra> {
        @Override
        public Extra createInstance(Type type) {
            return new Extra("extra");
        }
    }

    @Test
    public void testNonNullField() {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NonNullFieldFactory()).create();
        Data data = gson.fromJson("{}", Data.class);

        Assert.assertNull(data.nullField);
        Assert.assertEquals("", data.type);

        Assert.assertNotNull(data.array);
        Assert.assertNotNull(data.list);
        Assert.assertNotNull(data.map);
        Assert.assertNotNull(data.multiArray);
        Assert.assertNotNull(data.lists);
        Assert.assertNotNull(data.extra);

        Assert.assertEquals("extra", data.extra.name);
        Assert.assertEquals((Integer) 0, data.extra.integer);

        System.out.println(StringUtils.objectDeepToString(data));
    }
}
