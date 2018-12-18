package com.timingbar.android.library.module.entity;



import com.timingbar.android.app.ApiConfig;

import java.io.Serializable;

/**
 * BaseJson
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 针对服务器返回的json字符串，通用处理的实体类
 *
 * @author rqmei on 2018/2/5
 */

public class BaseJson<T> implements Serializable {
    private T data;//数据内容
    private int code;//标识code
    private String msg;//标识msg
    private String errors;//错误信息
    private boolean success;//是否成功的标识

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getErrors() {
        return errors;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (success == ApiConfig.RequestSuccess) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "data=" + data + ",code=" + code + ",msg=" + msg + ",errors=" + ",success=" + success;
    }
}
