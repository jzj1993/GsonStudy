package com.paincker.testcase.utils;

import com.paincker.utils.StringUtils;
import org.junit.Test;

import java.util.*;

/**
 * Created by jzj on 2017/12/1.
 */
public class TestDeepToString {

    @Test
    public void testDeepToString() {
        System.out.println(StringUtils.objectDeepToString(new Data()));
    }

    public static class BaseData<T> {
        private final Integer integer = 1;
        private final boolean bool = true;
        protected T type;
    }

    public static class Data extends BaseData<String> {

        private final String string1 = null;
        private final String string2 = "";

        private final String[] array = new String[]{"p", "q", "r"};
        private final String[][][] multiArray = new String[][][]{
                {{"1", "2", "3"}, {"7", "8", "9"}}, {{"a", "b", "c"}, {"x", "y", "z"}}
        };

        private final List<User> users = new ArrayList<>();

        private final List<String> list = Arrays.asList("x", "y");
        private final LinkedList<String> linkedList = new LinkedList<>();
        private final Map<String, Object> map = new HashMap<>();
        private final List<List<String>> lists = new ArrayList<>();

        public Data() {
            type = "type";
            lists.add(Arrays.asList("1", "2", "3"));
            lists.add(Arrays.asList("a", "b", "c"));
            map.put("key1", "val1");
            map.put("key2", "val2");
            map.put("key3", new User(0, "小王"));
            users.add(new User(1, "小明"));
            users.add(new User(2, "小红"));
        }
    }

    public static class User {
        private final long id;
        private final String name;

        public User(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
