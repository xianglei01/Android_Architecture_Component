package com.lei.data.exception

/**
 * @author xianglei
 * @created 2018/7/10 18:39
 */
object ApiResultCode {

    //对应HTTP的状态码
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val REQUEST_TIMEOUT = 408
    const val INTERNAL_SERVER_ERROR = 500
    const val BAD_GATEWAY = 502
    const val SERVICE_UNAVAILABLE = 503
    const val GATEWAY_TIMEOUT = 504

    const val RESULT_NORMAL = 0
    const val UNKNOWN = "1000"
    const val PARSE_ERROR = "1001"
    const val NETWORK_NOTCONNECTION = "1002"
    const val UNKNOW_HOST = "1003"

    const val SSL_ERROR = "3001"

    const val API_RESULT_NORMAL = "000000"

}
