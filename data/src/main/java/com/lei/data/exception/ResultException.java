package com.lei.data.exception;

/**
 * @author xianglei
 * @created 2018/7/5 12:21
 */
public class ResultException extends RuntimeException {

    public String errCode;

    public ResultException(String errCode, String msg) {
        super(msg);
        this.errCode = errCode;
    }

    public String getErrCode() {
        return errCode;
    }
}
