package com.lei.data.interceptor

import com.lei.data.DownloadListener
import okhttp3.Interceptor
import okhttp3.Response

/**
 * created by xianglei
 * 2019/3/18 15:29
 */
class DownloadInterceptor(private var mDownloadListener: DownloadListener?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var oldRequest = chain.request()
        if (mDownloadListener != null) {
            var originalResponse = chain.proceed(oldRequest)
            return originalResponse.newBuilder().body(DownloadResponseBody(originalResponse.body(), mDownloadListener)).build()
        }
        return chain.proceed(oldRequest)
    }
}