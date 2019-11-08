package com.lei.androidarchitecture.main

import com.lei.data.model.Demo

/**
 * created by xianglei
 * 2019/10/28 16:13
 */
interface MainDataSource {

    suspend fun demo(): List<Demo>?
}