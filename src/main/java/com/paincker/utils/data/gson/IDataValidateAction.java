package com.paincker.utils.data.gson;

/**
 * 自动解析完成后，对数据进行校验。如果是无效数据，则直接解析为null，从而自动剔除（在数据解析线程工作）。
 * 注意，只有当数据是由Json解析而来且非空时，才会执行Action。
 * Created by jzj on 2017/11/22.
 */
public interface IDataValidateAction {

    boolean isDataValid();
}
