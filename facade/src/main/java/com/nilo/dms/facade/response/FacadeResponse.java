package com.nilo.dms.facade.response;

/**
 * Created by admin on 2018/1/9.
 */
public class FacadeResponse<T> {

    private boolean result;

    private String msg;

    private String errorCode;

    private T t;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
