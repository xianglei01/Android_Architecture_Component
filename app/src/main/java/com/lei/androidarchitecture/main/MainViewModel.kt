package com.lei.androidarchitecture.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.lei.androidarchitecture.BaseViewModel
import com.lei.data.net.BaseObserver
import com.lei.data.exception.ResultException
import com.lei.data.model.Demo

/**
 * created by xianglei
 * 2019/10/28 15:33
 */
class MainViewModel(private val mainRepository: MainRepository) : BaseViewModel() {

    fun request() {
        getProgressState().value = true
//        GlobalScope.launch {
//            val demo = mainRepository.cDemo()
//            //弱提示title
//            getToastContent().value = demo?.data?.title
//            getProgressState().value = false
//        }
        mainRepository.demo(object : BaseObserver<Demo?>() {
            override fun onSuccess(data: Demo?) {
                getProgressState().value = false
                //弱提示title
                getToastContent().value = data?.title
            }

            override fun onFailed(e: ResultException, data: Demo?) {
                getProgressState().value = false
                getToastContent().value = e.message
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        mainRepository.dispose()
    }

    class MainViewModelFactory(private val mainRepository: MainRepository) :
            ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(mainRepository) as T
        }
    }

}