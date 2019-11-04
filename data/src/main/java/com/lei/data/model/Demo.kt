package com.lei.data.model

import java.io.Serializable

/**
 * created by xianglei
 * 2019/11/1 10:47
 */
data class Demo(val date: Date?,
                val author: String,
                val title: String,
                val digest: String,
                val content: String,
                val wc: String) : Serializable