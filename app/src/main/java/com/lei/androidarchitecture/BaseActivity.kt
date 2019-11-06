package com.lei.androidarchitecture

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

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

}