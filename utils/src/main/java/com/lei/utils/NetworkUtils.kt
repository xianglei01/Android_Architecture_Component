package com.lei.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by xianglei on 2018/11/19.
 */
object NetworkUtils {

    fun isNetworkConnection(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
