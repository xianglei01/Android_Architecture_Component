package com.lei.androidarchitecture.main

import com.lei.data.net.BaseObserver
import com.lei.data.model.BaseModel
import com.lei.data.model.Demo
import io.reactivex.disposables.Disposable

/**
 * created by xianglei
 * 2019/10/28 16:13
 */
interface MainDataSource {

    fun demo(observer: BaseObserver<Demo?>): Disposable

    suspend fun cDemo(): BaseModel<Demo?>
}