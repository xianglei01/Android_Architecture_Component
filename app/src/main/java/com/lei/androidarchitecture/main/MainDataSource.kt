package com.lei.androidarchitecture.main

import com.lei.data.model.Demo
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * created by xianglei
 * 2019/10/28 16:13
 */
interface MainDataSource {

    fun demo(observer: DisposableObserver<List<Demo>?>): Disposable

    suspend fun cDemo(): List<Demo>?
}