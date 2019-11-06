package com.lei.data.rereofit

import com.lei.data.model.BaseModel
import com.lei.data.model.Demo
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * @author xianglei
 * @created 2018/7/9 10:39
 */
interface ApiService {

    @GET
    fun commonGet(@Url url: String, @QueryMap maps: Map<String, String?> = HashMap()): Observable<BaseModel<Demo?>>

    @GET(API.DEMO)
    fun cDemo(): Deferred<BaseModel<Demo?>>

}