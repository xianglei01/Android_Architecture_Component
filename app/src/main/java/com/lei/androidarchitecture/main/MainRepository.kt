package com.lei.androidarchitecture.main

import com.lei.data.JobExecutor
import com.lei.data.model.Demo
import com.lei.data.repository.BaseRepository
import com.lei.data.rereofit.API
import com.lei.data.rereofit.ApiClient.apiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * created by xianglei
 * 2019/10/28 16:13
 */
class MainRepository : MainDataSource, BaseRepository() {

    override fun demo(observer: DisposableObserver<List<Demo>?>): Disposable {
        val disposable = apiService
                .commonGet(API.DEMO)
                .subscribeOn(Schedulers.from(JobExecutor.getJobExecutor()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        add(disposable)
        return disposable
    }

    override suspend fun cDemo(): List<Demo>? {
        return apiService.cDemo().await()
    }
}