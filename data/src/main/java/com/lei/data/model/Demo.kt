package com.lei.data.model

import java.io.Serializable

/**
 * created by xianglei
 * 2019/11/1 10:47
 */
data class Demo(val id: Long,
                val node_id: String,
                val name: String,
                val full_name: String,
                val private: Boolean,
                val owner: Any) : Serializable