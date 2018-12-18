package com.timingbar.android.library.module.entity;

import lib.android.timingbar.com.http.module.ApiResult;

/**
 * BaseResult
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/11
 */

public class BaseResult<T> extends ApiResult<T> {
    private String errors;//错误信息
}
