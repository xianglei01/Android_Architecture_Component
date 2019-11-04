package com.lei.data.model

import java.io.Serializable

/**
 * created by xianglei
 * 2019/10/28 15:19
 */
data class BaseModel<T>(val resultCode: String,
                        val resultMessage: String,
                        val data: T?) : Serializable