package com.lei.utils

import android.content.Context
import android.widget.Toast

/**
 * @author xianglei
 * @created 2018/7/10 18:46
 */
object ToastUtil {

    fun toast(context: Context?, resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    fun toast(context: Context?, res: String?) {
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
    }

}
