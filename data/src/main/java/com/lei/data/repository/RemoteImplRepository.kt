package com.lei.data.repository

import android.content.Context
import com.lei.data.net.BaseObserver
import com.lei.data.JobExecutor
import com.lei.data.model.Demo
import com.lei.data.rereofit.API
import com.lei.data.rereofit.ApiClient
import com.lei.data.rereofit.ApiClient.apiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * created by xianglei
 * 2019/11/4 14:00
 */
@Deprecated("暂时未用")
class RemoteImplRepository(val context: Context) : RemoteRepository {

    private var compositeDisposable = CompositeDisposable()

    override fun demo(observer: BaseObserver<Demo?>) {
        compositeDisposable.add(apiService
                .commonGet(API.DEMO)
                .subscribeOn(Schedulers.from(JobExecutor.getJobExecutor()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer))
    }
}