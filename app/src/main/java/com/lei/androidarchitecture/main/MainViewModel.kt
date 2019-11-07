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
                    toastContent().value = it?.get(0)?.full_name
                    progressState().value = false
                },
                onFail = {
                    toastContent().value = it.message
                    progressState().value = false
                }
        )
    }

    class MainViewModelFactory(private val repository: MainRepository) :
            ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }

}