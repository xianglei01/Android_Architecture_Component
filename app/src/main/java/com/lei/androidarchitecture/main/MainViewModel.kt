package com.lei.androidarchitecture.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lei.androidarchitecture.BaseViewModel

/**
 * created by xianglei
 * 2019/10/28 15:33
 */
class MainViewModel(private val mainRepository: MainRepository) : BaseViewModel() {

    fun request() {
        progressState().value = true
        execute(
                request = {
                    mainRepository.cDemo()
                },
                onSuccess = {
                    //弱提示title
                    toastContent().value = it.data?.title
                    progressState().value = false
                },
                onFail = {
                    toastContent().value = it.message
                    progressState().value = false
                }
        )
//        mainRepository.demo(object : BaseObserver<Demo?>() {
//            override fun onSuccess(data: Demo?) {
//                progressState().value = false
//                //弱提示title
//                toastContent().value = data?.title
//            }
//
//            override fun onFailed(e: ResultException, data: Demo?) {
//                progressState().value = false
//                toastContent().value = e.message
//            }
//        })
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