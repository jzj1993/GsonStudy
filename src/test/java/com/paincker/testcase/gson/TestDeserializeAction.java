package com.paincker.testcase.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.paincker.utils.StringUtils;
import com.paincker.utils.data.ItemNonNullList;
import com.paincker.utils.data.gson.DeserializeActionFactory;
import com.paincker.utils.data.gson.IAfterDeserializeAction;
import com.paincker.utils.data.gson.IDataValidateAction;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by jzj on 2017/12/1.
 */
public class TestDeserializeAction {

    public static class Data {

        // 原始list
        public List<User> list1;

        // 过滤掉null item
        public ItemNonNullList<User> list2;

        // 过滤掉invalid item
        public ItemNonNullList<ValidUser> list3;

    }

    public static class User implements IAfterDeserializeAction {

        private long id;
        private String name;

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
    }

    public static class ValidUser extends User implements IDataValidateAction {

        @Override
        public boolean isDataValid() {
            // 如果id为0或负值，说明接口异常，视为无效数据，丢弃不用
            return getId() > 0;
        }
    }

    @Test
    public void testDeserializeAction() {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new DeserializeActionFactory()).create();

        String json = "[{ \"id\": 1, \"name\": \"小明\" }, { \"id\": 2, \"name\": \"\" }, { \"id\": -1, \"name\": \"小红\" }, null ]";

        List<User> list1 = gson.fromJson(json, new TypeToken<List<User>>() {
        }.getType());

        ItemNonNullList<User> list2 = gson.fromJson(json, new TypeToken<ItemNonNullList<User>>() {
        }.getType());

        ItemNonNullList<ValidUser> list3 = gson.fromJson(json, new TypeToken<ItemNonNullList<ValidUser>>() {
        }.getType());

        Assert.assertNotNull(list1);
        Assert.assertEquals(4, list1.size());
        Assert.assertEquals("匿名", list1.get(1).name); // doAfterDeserialize

        Assert.assertNull(list1.get(3)); // null item
        Assert.assertNotNull(list2);
        Assert.assertEquals(3, list2.size()); // ItemNonNullList过滤掉null item

        Assert.assertTrue(list2.get(2).id == -1); // invalid item
        Assert.assertNotNull(list3);
        Assert.assertEquals(2, list3.size()); // IDataValidateAction过滤掉invalid item

        System.out.println("\nlist 1: " + StringUtils.objectDeepToString(list1));
        System.out.println("\nlist 2: " + StringUtils.objectDeepToString(list2));
        System.out.println("\nlist 3: " + StringUtils.objectDeepToString(list3));
    }
}
