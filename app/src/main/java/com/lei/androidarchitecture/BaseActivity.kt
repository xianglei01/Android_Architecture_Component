package com.lei.androidarchitecture

import android.support.v4.app.FragmentActivity
import android.os.Bundle

/**
 * created by xianglei
 * 2019/10/28 14:48
 */
abstract class BaseActivity : FragmentActivity(), IBasePage {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        initView()
        initListener()
        initData()
    }

    override fun showBaseLoading(transparent: Boolean) {
    }

    override fun hideBaseLoading() {
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }
}