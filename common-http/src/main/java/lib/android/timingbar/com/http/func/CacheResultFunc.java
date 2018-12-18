
package lib.android.timingbar.com.http.func;


import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import lib.android.timingbar.com.http.cache.module.CacheResult;

/**
 * <p>描述：缓存结果转换</p>
 * 作者： zhouyou<br>
 * 日期： 2017/4/21 10:53 <br>
 * 版本： v1.0<br>
 */
public class CacheResultFunc<T> implements Function<CacheResult<T>, T> {
    @Override
    public T apply(@NonNull CacheResult<T> tCacheResult) throws Exception {
        return tCacheResult.data;
    }
}
