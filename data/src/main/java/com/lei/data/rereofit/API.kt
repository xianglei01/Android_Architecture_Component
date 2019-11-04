package com.lei.data.rereofit

import com.lei.data.BuildConfig

/**
 * @author xianglei
 * @created 2018/7/4 20:02
 */
object API {

    private const val DEBUG_URL = ""
    private const val RELEASE_URL = ""

    var BASE_URL = if (BuildConfig.DEBUG) DEBUG_URL else RELEASE_URL

    const val DEMO = "https://interface.meiriyiwen.com/article/today"

}
