package com.lei.data.interceptor

import android.content.Context
import com.lei.data.exception.ApiResultCode
import com.lei.data.exception.ResultException
import com.lei.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author xianglei
 * @created 2018/7/10 9:15
 */
class CommonInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (NetworkUtils.isNetworkConnection(context)) {
            return chain.proceed(chain.request())
        } else {
            throw ResultException(ApiResultCode.NETWORK_NOTCONNECTION, "请检查网络连接")
        }
    }
}
