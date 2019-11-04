package com.lei.data.rereofit

import com.lei.data.model.BaseModel
import com.lei.data.model.Demo
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/**
 * @author xianglei
 * @created 2018/7/9 10:39
 */
interface ApiService {

    @GET(API.DEMO)
    fun demo(): Observable<BaseModel<Demo?>>

    @GET(API.DEMO)
    fun cDemo(): Deferred<BaseModel<Demo?>>

}