package com.lei.data.rereofit

import com.lei.data.BuildConfig
import com.lei.data.interceptor.CommonInterceptor
import com.lei.data.interceptor.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    //超时时间 30s
    private const val DEFAULT_TIME_OUT = 30L
    private const val DEFAULT_READ_TIME_OUT = 30L

    val apiService: ApiService by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(CommonInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .sslSocketFactory(SSLSocketFactoryUtil.createSSLSocketFactory(), SSLSocketFactoryUtil.createTrustAllManager())
                .hostnameVerifier(SSLSocketFactoryUtil.TrustAllHostnameVerifier())
                .retryOnConnectionFailure(true)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutinesCallAdapterFactory())
                .client(okHttpClient)
                .build()
        return@lazy retrofit.create(ApiService::class.java)
    }

}
