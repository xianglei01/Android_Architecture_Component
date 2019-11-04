package com.lei.androidarchitecture

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * created by xianglei
 * 2019/10/28 15:34
 */
open class BaseViewModel : ViewModel() {

    /**
     * loading框状态监听
     */
    private val progressMutableLiveData = MutableLiveData<Boolean>()
    /**
     * 弱提示语
     */
    private val toastMutableLiveData = MutableLiveData<String>()

    fun getProgressState(): MutableLiveData<Boolean> {
        return progressMutableLiveData
    }

    fun getToastContent(): MutableLiveData<String> {
        return toastMutableLiveData
    }

}