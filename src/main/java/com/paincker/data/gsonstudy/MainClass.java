package com.paincker.data.gsonstudy;

import com.paincker.utils.StringUtils;
import com.paincker.utils.data.gson.GsonUtils;

import java.io.File;

public class MainClass {

    public static void main(String[] args) {
        String json = StringUtils.readAsString(new File("src/main/resources/test_json.json"));
        Data data = GsonUtils.fromJson(json, Data.class);
        System.out.println(String.valueOf(data));
    }
}
