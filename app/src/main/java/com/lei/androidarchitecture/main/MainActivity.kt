package com.lei.androidarchitecture.main

import android.Manifest.permission
import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lei.androidarchitecture.BaseActivity
import com.lei.androidarchitecture.R
import com.lei.utils.ToastUtil
import com.lei.utils.permissions.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private lateinit var mViewModel: MainViewModel

    override val tag: String = "MainActivity"
    override val layoutResId: Int = R.layout.activity_main

    override fun canBack(): Boolean = true

    @SuppressLint("CheckResult")
    override fun initView() {
        mViewModel = ViewModelProviders.of(this,
                MainViewModel.MainViewModelFactory(MainRepository(this))).get(MainViewModel::class.java)
        mViewModel.toastContent().observe(this, Observer {
            ToastUtil.toast(this, it)
        })
        mViewModel.progressState().observe(this, Observer {
            if (it == true) {
                showBaseLoading(true)
            } else {
                hideBaseLoading()
            }
        })
    }

    override fun initListener() {
        btn_request?.setOnClickListener {
            mViewModel.request()
        }
        btn_permission?.setOnClickListener {
            RxPermissions(this)
                    .request(permission.CAMERA,
                            READ_PHONE_STATE)
                    .subscribe { granted ->
                        if (granted) {
                            // All requested permissions are granted
                            ToastUtil.toast(this@MainActivity, "全部通过")
                        } else {
                            // At least one permission is denied
                            ToastUtil.toast(this@MainActivity, "拒绝")
                        }
                    }
        }
    }

    override fun initData() {
    }

}
