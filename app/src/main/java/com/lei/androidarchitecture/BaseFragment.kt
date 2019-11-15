package com.lei.androidarchitecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * created by xianglei
 * 2019/10/28 14:49
 */
abstract class BaseFragment : Fragment(), IBasePage {

    private var parentView: View? = null
    private var hasInitView = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (parentView == null) {
            parentView = inflater.inflate(layoutResId, container, false)
        }
        return parentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Fragment中使用kotlin获取控件需要在onViewCreated之后执行才有效
        if (!hasInitView) {
            hasInitView = true
            initViewModel()
            initListener()
            initData()
        }
    }

    override fun showBaseLoading() {
    }

    override fun hideBaseLoading() {
    }
}