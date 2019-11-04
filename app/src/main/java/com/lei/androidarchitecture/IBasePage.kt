package com.lei.androidarchitecture

/**
 * @author xianglei
 * @create 2018/10/23 下午3:46
 */
interface IBasePage {

    val tag: String

    val layoutResId: Int

    fun canBack(): Boolean

    fun initView()

    fun initListener()

    fun initData()

    fun showBaseLoading(transparent: Boolean)

    fun hideBaseLoading()

    fun destroy()

}
