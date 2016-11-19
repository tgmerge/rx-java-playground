package me.tgmerge.rxjavaplayground._backbone.network;

import me.tgmerge.rxjavaplayground._backbone.model.BaseModel;

/**
 * 包装网络请求的 Response
 *
 * Response 的原始数据形如：
 *
 *      {
 *          "status": 200,
 *          "msg": "ok",
 *          "result": ... ...
 *      }
 */

public class Response<T> extends BaseModel {

    private int status;
    private String msg;
    private T result;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getResult() {
        return result;
    }
}
