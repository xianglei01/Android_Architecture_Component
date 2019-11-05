package com.lei.data.rereofit

import android.content.Context
import com.lei.data.BuildConfig
import com.lei.data.interceptor.CommonInterceptor
import com.lei.data.interceptor.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient internal constructor(context: Context) {

    private val retrofit: Retrofit
    private var apiService: ApiService? = null
    private var okHttpClient: OkHttpClient? = null

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(CommonInterceptor(context))
                .addInterceptor(httpLoggingInterceptor)
                .sslSocketFactory(SSLSocketFactoryUtil.createSSLSocketFactory()!!, SSLSocketFactoryUtil.createTrustAllManager()!!)
                .hostnameVerifier(SSLSocketFactoryUtil.TrustAllHostnameVerifier())
                .retryOnConnectionFailure(true)
        okHttpClient = builder.build()

        this.retrofit = Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient!!)
                .build()
    }

    fun getApiService(): ApiService {
        if (apiService == null) {
            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService!!
    }

    companion object {
        //超时时间 5s
        private const val DEFAULT_TIME_OUT = 30
        private const val DEFAULT_READ_TIME_OUT = 30
        private var apiClient: ApiClient? = null

        fun getApiService(context: Context): ApiService {
            if (apiClient == null) {
                apiClient = ApiClient(context)
            }
            return apiClient!!.getApiService()
        }
    }
}
