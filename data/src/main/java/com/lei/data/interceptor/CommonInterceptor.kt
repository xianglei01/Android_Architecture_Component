package com.lei.data.interceptor

import android.content.Context
import com.lei.data.exception.ApiResultCode
import com.lei.data.exception.ResultException
import com.lei.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @author xianglei
 * @created 2018/7/10 9:15
 */
class CommonInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (NetworkUtils.isNetworkConncetion(context)) {
            var original = chain.request()
            val requestBuilder = original.newBuilder()
            original = requestBuilder.build()
            requestBuilder.method(original.method(), original.body())
            val response = chain.proceed(original)
            val gateWayException = handleGateWayError(response)
            if (gateWayException != null) {
                throw gateWayException
            }
            return response
        } else {
            throw ResultException(ApiResultCode.NETWORK_NOTCONNECTION, "请检查网络连接")
        }
    }

    /**
     * 网关错误
     */
    private fun handleGateWayError(response: Response): ResultException? {
        return null
    }

    private fun addHeaders(requestBuilder: Request.Builder): Request.Builder {
        requestBuilder
                .header("", "")
        return requestBuilder
    }

}
