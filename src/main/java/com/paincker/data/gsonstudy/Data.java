package com.paincker.data.gsonstudy;

import com.paincker.utils.data.ItemNonNullList;
import com.paincker.utils.data.gson.IAfterDeserializeAction;
import com.paincker.utils.data.gson.IDataValidateAction;
import com.paincker.utils.data.gson.NonNullString;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jzj on 2017/11/22.
 */

public class Data {

    public static class User implements IAfterDeserializeAction {

        private long id;
        private String name;
        // 带有注解的字段解析后非空
        @NonNullString
        private String address;
        // 没有注解的字段解析后为null
        private String phone;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public void doAfterDeserialize() {
            if (name == null || name.length() == 0) {
                name = "匿名";
            }
        }

        @Override
        public String toString() {
            return name + "(" + id + ", " + address + ", " + phone + ")";
        }
    }

    public static class ValidUser extends User implements IDataValidateAction {

        @Override
        public boolean isDataValid() {
            // 如果id为0或负值，说明接口异常，视为无效数据，丢弃不用
            return getId() > 0;
        }
    }

    // 原始list
    public List<User> list1;

    // 过滤掉null item
    public ItemNonNullList<User> list2;

    // 过滤掉invalid item
    public ItemNonNullList<ValidUser> list3;

    @Override
    public String toString() {
        return String.format("Data (\n  list1 = %s\n\n  list2 = %s\n\n  list3 = %s\n)",
                toString(list1), toString(list2), toString(list3));
    }

    private static String toString(List list) {
        return list == null ? "null" : Arrays.toString(list.toArray());
    }
}
