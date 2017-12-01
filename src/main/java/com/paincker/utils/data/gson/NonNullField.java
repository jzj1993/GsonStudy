package com.paincker.utils.data.gson;

import com.google.gson.InstanceCreator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析后不会为null的NonNullField，不需要判空。注意，只有当对象自身解析非空，其NonNullField才会被填充。
 * Created by jzj on 2017/11/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NonNullField {
    Class<? extends InstanceCreator> value() default NonNullFieldConstructor.class;
}
