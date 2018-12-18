package lib.android.timingbar.com.http.transformer;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import lib.android.timingbar.com.http.func.HttpResponseFunc;

/**
 * <p>描述：错误转换Transformer</p>
 * 作者： zhouyou<br>
 * 日期： 2017/5/15 17:09 <br>
 * 版本： v1.0<br>
 */
public class HandleErrTransformer<T> implements ObservableTransformer<T, T> {
    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.onErrorResumeNext(new HttpResponseFunc<T> ());
    }
}
