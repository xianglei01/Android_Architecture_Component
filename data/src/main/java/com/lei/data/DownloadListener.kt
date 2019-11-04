package com.lei.data

/**
 * created by xianglei
 * 2019/3/18 15:35
 */
interface DownloadListener {

    fun update(totalBytesRead: Long, contentLength: Long, done: Boolean)
}