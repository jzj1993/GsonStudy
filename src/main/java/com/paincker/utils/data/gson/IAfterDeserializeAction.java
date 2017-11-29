package com.paincker.utils.data.gson;

/**
 * 自动解析完成后，对数据进行处理（在数据解析线程工作）
 * Created by jzj on 2017/11/22.
 */
public interface IAfterDeserializeAction {

    void doAfterDeserialize();
}
