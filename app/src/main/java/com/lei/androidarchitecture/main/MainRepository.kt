package com.lei.androidarchitecture.main

import android.content.Context
import com.lei.data.BaseObserver
import com.lei.data.BaseRepository
import com.lei.data.JobExecutor
import com.lei.data.model.Demo
import com.lei.data.rereofit.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * created by xianglei
 * 2019/10/28 16:13
 */
class MainRepository(val context: Context) : MainDataSource, BaseRepository() {

    override fun demo(observer: BaseObserver<Demo?>): Disposable {
        val disposable = ApiClient.getApiClient(context).getApiService()
                .demo()
                .subscribeOn(Schedulers.from(JobExecutor.getJobExecutor()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        add(disposable)
        return disposable
    }

}