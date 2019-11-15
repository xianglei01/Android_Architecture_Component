package com.lei.androidarchitecture.main

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lei.androidarchitecture.BaseFragment
import com.lei.androidarchitecture.R
import com.lei.utils.ToastUtil
import com.lei.utils.permissions.RxPermissions
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * created by xianglei
 * 2019/11/15 15:05
 */
class MainFragment : BaseFragment() {

    private lateinit var mViewModel: MainViewModel

    override val pageTag: String = "MainFragment"
    override val layoutResId: Int = R.layout.fragment_main

    override fun canBack(): Boolean = true

    @SuppressLint("CheckResult")
    override fun initViewModel() {
        mViewModel = ViewModelProviders.of(this,
                MainViewModel.MainViewModelFactory(MainRepository())).get(MainViewModel::class.java)
        mViewModel.toastContent().observe(this, Observer {
            ToastUtil.toast(context, it)
        })
        mViewModel.loadingDataState().observe(this, Observer {
            if (it == true) {
                showBaseLoading()
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
            //权限申请
            RxPermissions(this)
                    .request(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                    .subscribe { granted ->
                        if (granted) {
                            // All requested permissions are granted
                            ToastUtil.toast(context, "全部通过")
                        } else {
                            // At least one permission is denied
                            ToastUtil.toast(context, "拒绝")
                        }
                    }
        }
    }

    override fun initData() {
    }
}