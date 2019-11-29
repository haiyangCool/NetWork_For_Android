package com.haiyangwang.summer.NetWork.InterfaceDefines;

import com.haiyangwang.summer.NetWork.VVBaseApiManager;

/*  Data Reformer
 *  reformer 充当数据适配器
 *  任何实现了该接口的类都可作为数据格式化处理
 *  在这里建议采用去Model化的策略（使用通用数据模型字典Map），
 *  在项目中引入具体的Model是引入耦合的罪魁之一
 *  而且Model的重用几乎不可能（除非服务器的数据字段高度统一）*/
public interface ApiManagerResponseDataReformer {

    /* 这里的reformer一般用来格式化数据之后，一字典的形式抛出
     * 但不要被这种形式固定，它完全可以当做适配器，把数据和View组合后，抛出View
     * */
    Object reformerData(VVBaseApiManager manager, Object jsonString);
}

