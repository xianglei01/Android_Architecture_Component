package com.lei.androidarchitecture.main

import com.lei.androidarchitecture.BaseActivity
import com.lei.androidarchitecture.R

class MainActivity : BaseActivity() {

    override val pageTag: String = "MainActivity"
    override val layoutResId: Int = R.layout.activity_main

    override fun canBack(): Boolean = true

    override fun initViewModel() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_content, MainFragment())
                .commit()
    }

    override fun initListener() {
    }

    override fun initData() {
    }
}
