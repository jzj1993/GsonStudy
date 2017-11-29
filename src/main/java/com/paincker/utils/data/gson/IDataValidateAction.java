package com.paincker.utils.data.gson;

/**
 * 自动解析完成后，对数据进行校验。如果是无效数据，则直接解析为null，从而自动剔除（在数据解析线程工作）
 * Created by jzj on 2017/11/22.
 */
public interface IDataValidateAction {

    boolean isDataValid();
}
