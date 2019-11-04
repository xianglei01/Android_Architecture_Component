package com.lei.data.interceptor

import android.os.Handler
import android.os.Looper
import com.lei.data.DownloadListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


/**
 * created by xianglei
 * 2019/3/18 15:31
 */
class DownloadResponseBody(private var responseBody: ResponseBody?, private var downloadListener: DownloadListener?) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun contentLength(): Long = responseBody?.contentLength() ?: 0

    override fun contentType(): MediaType? = responseBody?.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody?.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source?): Source {
        return object : ForwardingSource(source) {
            private var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                handler.post {
                    if(totalBytesRead != responseBody?.contentLength()) {
                        downloadListener?.update(totalBytesRead, responseBody?.contentLength()
                                ?: 0, bytesRead == -1L)
                    }
                }
                return bytesRead
            }
        }
    }
}