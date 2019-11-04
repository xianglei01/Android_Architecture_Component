package com.lei.data.repository

import com.lei.data.BaseObserver
import com.lei.data.model.Demo

/**
 * created by xianglei
 * 2019/11/4 14:00
 */
@Deprecated("暂时未用")
interface RemoteRepository {

    fun demo(observer: BaseObserver<Demo?>)
}