package com.lei.androidarchitecture

/**
 * @author xianglei
 * @create 2018/10/23 下午3:46
 */
interface IBasePage {

    /**
     * 页面tag
     */
    val pageTag: String

    /**
     * 页面layout
     */
    val layoutResId: Int

    /**
     * 页面是否可返回上一页（返回页按钮）
     */
    fun canBack(): Boolean

    /**
     * 初始化页面
     */
    fun initViewModel()

    /**
     * 初始化事件
     */
    fun initListener()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 显示请求loading
     */
    fun showBaseLoading()

    /**
     * 隐藏请求loading
     */
    fun hideBaseLoading()

}
