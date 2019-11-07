package com.lei.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author xianglei
 * @created 2018/7/10 9:15
 */
class CommonInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //可添加统一header或者参数
        return chain.proceed(chain.request())
    }
}
