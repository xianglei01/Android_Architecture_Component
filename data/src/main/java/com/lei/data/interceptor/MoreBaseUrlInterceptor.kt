package com.lei.data.interceptor

import com.lei.data.rereofit.API

import java.io.IOException

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author xianglei
 * @created 2018/8/10 16:24
 */
class MoreBaseUrlInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始的originalRequest
        var originalRequest = chain.request()
        //获取老的url
        val oldUrl = originalRequest.url()
        if (oldUrl.host().startsWith("wirelessgateway")) {
            //获取originalRequest的创建者builder
            val builder = originalRequest.newBuilder()
            //获取头信息的集合如：manage,mdffx
            val baseURL = HttpUrl.parse(API.BASE_URL)
            //重建新的HttpUrl，需要重新设置的url部分
            val newHttpUrl = oldUrl.newBuilder()
                    .scheme(baseURL!!.scheme())
                    .host(baseURL.host())
                    .port(baseURL.port())
                    .build()
            //获取处理后的新newRequest
            originalRequest = builder.url(newHttpUrl).build()
        }
        return chain.proceed(originalRequest)

    }

}
