
package lib.android.timingbar.com.http.func;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import lib.android.timingbar.com.http.exception.ApiException;
import lib.android.timingbar.com.http.exception.ServerException;
import lib.android.timingbar.com.http.module.ApiResult;
import lib.android.timingbar.com.http.util.HttpLog;


/**
 * <p>描述：ApiResult<T>转换T</p>
 * 默认传入实体类解析，不自定义ApiResult相关
 */
public class HandleFuc<T> implements Function<ApiResult<T>, T> {
    @Override
    public T apply(@NonNull ApiResult<T> tApiResult) throws Exception {
        HttpLog.i ("HandleFuc==" + tApiResult);
        if (ApiException.isOk (tApiResult)) {
            HttpLog.i ("HandleFuc getData==" + tApiResult.getData ());
            return tApiResult.getData ();// == null ? Optional.ofNullable(tApiResult.getData()).orElse(null) : tApiResult.getData();
        } else {
            throw new ServerException (tApiResult.getCode (), tApiResult.getMsg ());
        }
    }
}
