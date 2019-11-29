package com.haiyangwang.summer.NetWork.InterfaceDefines;

/*  实现该接口的子类 APIManager 可以通过定义下面的变量，
通过简单的逻辑实现加载下一页功能
    // 当前页码
    int currentPage
    // 每页的数据量
    int pageSize
    // 是否为第一页
    boolean isFirstPage
    // 是否为最后一页
    boolean isLastPage
*/
public interface ApiManagerLoadNextPage {

    /* 加载下一页*/
    void loadNextPage();


}
