package com.lei.androidarchitecture.main

import com.lei.data.model.Demo
import com.lei.data.rereofit.API
import com.lei.data.rereofit.ApiClient.apiService

/**
 * created by xianglei
 * 2019/10/28 16:13
 */
class MainRepository : MainDataSource {

    override suspend fun demo(): List<Demo>? {
        return apiService.commonGet(API.DEMO).await()
    }
}