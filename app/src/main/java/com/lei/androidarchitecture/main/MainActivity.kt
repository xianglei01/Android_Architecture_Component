package com.lei.androidarchitecture.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.lei.androidarchitecture.BaseActivity
import com.lei.androidarchitecture.R
import com.lei.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var mViewModel: MainViewModel? = null

    override val tag: String = "MainActivity"
    override val layoutResId: Int = R.layout.activity_main

    override fun canBack(): Boolean = true

    override fun initView() {
        mViewModel = ViewModelProviders.of(this,
                MainViewModel.MainViewModelFactory(MainRepository(this))).get(MainViewModel::class.java)
        mViewModel?.getToastContent()?.observe(this, Observer {
            ToastUtil.toast(this, it)
        })
        mViewModel?.getProgressState()?.observe(this, Observer {
            if (it == true) {
                showBaseLoading(true)
            } else {
                hideBaseLoading()
            }
        })
    }

    override fun initListener() {
        btn_request?.setOnClickListener {
            mViewModel?.request()
        }
    }

    override fun initData() {
    }

}
