package com.lei.utils

import android.text.TextUtils

/**
 * @author xianglei
 * @created 2018/7/16 19:22
 */
object StringUtil {

    fun inSizeBound(str: String?, num: Int): Boolean {
        return str != null && !TextUtils.isEmpty(str) && str.length >= num
    }

}
