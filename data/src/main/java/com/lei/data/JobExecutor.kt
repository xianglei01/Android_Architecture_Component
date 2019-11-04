package com.lei.data

import io.reactivex.annotations.NonNull
import java.util.concurrent.*

/**
 * created by xianglei
 * 2019/10/28 16:51
 */
class JobExecutor internal constructor() : Executor {
    private val threadPoolExecutor: ThreadPoolExecutor

    init {
        this.threadPoolExecutor = ThreadPoolExecutor(3, 5, 10, TimeUnit.SECONDS,
                LinkedBlockingQueue(), JobThreadFactory())
    }

    override fun execute(@NonNull runnable: Runnable) {
        this.threadPoolExecutor.execute(runnable)
    }

    private class JobThreadFactory : ThreadFactory {
        private var counter = 0

        override fun newThread(@NonNull runnable: Runnable): Thread {
            return Thread(runnable, "android_" + counter++)
        }
    }

    companion object {
        private var jobExecutor: JobExecutor? = null

        fun getJobExecutor(): JobExecutor {
            if (jobExecutor == null) {
                jobExecutor = JobExecutor()
            }
            return jobExecutor!!
        }
    }
}