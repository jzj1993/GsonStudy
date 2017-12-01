package com.paincker.utils.data.gson;

/**
 * 自动解析完成后，对数据进行处理（在数据解析线程工作）。
 * 注意，只有当数据是由Json解析而来且非空时，才会执行Action。
 * Created by jzj on 2017/11/22.
 */
public interface IAfterDeserializeAction {

    void doAfterDeserialize();
}
