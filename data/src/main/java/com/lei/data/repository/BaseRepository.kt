package com.lei.data.repository

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * created by xianglei
 * 2019/10/28 17:04
 * 网络请求基础管理类
 * 包含添加请求，取消请求
 */
open class BaseRepository {

    private var compositeDisposable = CompositeDisposable()

    fun remove(disposable: Disposable) {
        compositeDisposable.remove(disposable)
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

    protected fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

}