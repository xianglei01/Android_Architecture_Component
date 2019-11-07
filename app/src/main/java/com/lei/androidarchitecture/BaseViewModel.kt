package com.lei.androidarchitecture

import android.net.ParseException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.lei.data.exception.ApiResultCode
import com.lei.data.exception.ResultException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import javax.net.ssl.SSLHandshakeException

/**
 * created by xianglei
 * 2019/10/28 15:34
 */
open class BaseViewModel : ViewModel() {

    /**
     * loading框状态监听
     */
    private val progressMutableLiveData = MutableLiveData<Boolean>()
    /**
     * 弱提示语
     */
    private val toastMutableLiveData = MutableLiveData<String>()

    fun progressState(): MutableLiveData<Boolean> = progressMutableLiveData

    fun toastContent(): MutableLiveData<String> = toastMutableLiveData

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun <T> execute(request: suspend () -> T?, onSuccess: (T?) -> Unit = {}, onFail: (ResultException) -> Unit = {}) =
            viewModelScope.launch {
                try {
                    //请求成功
                    onSuccess(withContext(Dispatchers.IO) { request() })
                } catch (e: Exception) {
                    //请求异常捕获
                    onFail(getResultException(e))
                }
            }

    private fun getResultException(e: Exception): ResultException {
        val ex: ResultException
        if (e is ResultException) {
            ex = e
        } else if (e is HttpException) {
            ex = when (e.code()) {
                ApiResultCode.UNAUTHORIZED, ApiResultCode.FORBIDDEN,
                    //权限错误，需要实现
                ApiResultCode.NOT_FOUND -> ResultException(e.code().toString(), "网络错误")
                ApiResultCode.REQUEST_TIMEOUT, ApiResultCode.GATEWAY_TIMEOUT -> ResultException(e.code().toString(), "网络连接超时")
                ApiResultCode.INTERNAL_SERVER_ERROR, ApiResultCode.BAD_GATEWAY, ApiResultCode.SERVICE_UNAVAILABLE -> ResultException(e.code().toString(), "服务器错误")
                else -> ResultException(e.code().toString(), "网络错误")
            }
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
                || e is MalformedJsonException) {
            ex = ResultException(ApiResultCode.PARSE_ERROR, "解析错误")
        } else if (e is SocketException) {
            ex = ResultException(ApiResultCode.REQUEST_TIMEOUT.toString(), "请检查网络连接")
        } else if (e is SocketTimeoutException) {
            ex = ResultException(ApiResultCode.REQUEST_TIMEOUT.toString(), "网络连接超时")
        } else if (e is SSLHandshakeException) {
            ex = ResultException(ApiResultCode.SSL_ERROR, "证书验证失败")
            return ex
        } else if (e is UnknownHostException) {
            ex = ResultException(ApiResultCode.UNKNOW_HOST, "网络错误，请切换网络重试")
            return ex
        } else if (e is UnknownServiceException) {
            ex = ResultException(ApiResultCode.UNKNOW_HOST, "网络错误，请切换网络重试")
        } else {
            ex = ResultException(ApiResultCode.UNKNOWN, "未知错误")
        }
        return ex
    }

}