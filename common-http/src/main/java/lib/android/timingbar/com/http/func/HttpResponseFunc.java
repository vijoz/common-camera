
package lib.android.timingbar.com.http.func;


import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import lib.android.timingbar.com.http.exception.ApiException;

/**
 * <p>描述：异常转换处理</p>
 * 作者： zhouyou<br>
 * 日期： 2017/5/15 16:55 <br>
 * 版本： v1.0<br>
 */
public class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        return Observable.error(ApiException.handleException(throwable));
    }
}
