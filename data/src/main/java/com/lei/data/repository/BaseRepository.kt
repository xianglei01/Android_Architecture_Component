package com.lei.data.repository

import com.lei.data.model.BaseModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * created by xianglei
 * 2019/10/28 17:04
 * 网络请求基础管理类
 * 包含添加请求，取消请求
 */
@Deprecated("暂时不用")
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

    suspend fun <T : Any?> request(call: suspend () -> BaseModel<T>): BaseModel<T> {
        return withContext(Dispatchers.IO) { call.invoke() }
    }

}