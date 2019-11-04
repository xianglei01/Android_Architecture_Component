package com.lei.data

/**
 * created by xianglei
 * 2019/10/28 16:18
 */
sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}