package com.lei.data.net

import com.lei.data.model.BaseModel

/**
 * @author xianglei
 * @create 2018/10/23
 */
abstract class BaseObserver<T> : HttpObserver<BaseModel<T>, T>() {

    override fun onComplete(t: BaseModel<T>) {
//        if (ApiResultCode.API_RESULT_NORMAL != t.resultCode) {
//            //Result != 0请求失败
//            onFailed(ResultException(t.resultCode, t.resultMessage), t.data)
//            return
//        }
        onSuccess(t.data)
    }

    abstract fun onSuccess(data: T?)

}
