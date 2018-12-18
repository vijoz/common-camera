package com.timingbar.android.library.module.api;

import com.timingbar.android.library.module.entity.Lesson;
import io.reactivex.Observable;
import lib.android.timingbar.com.http.module.ApiResult;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

/**
 * CommonService
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/7
 */

public interface CommonService {
    @POST("{path}")
    @FormUrlEncoded
    Observable<ApiResult<Lesson>> getLessonPhase(@Path("path") String path, @FieldMap Map<String, String> map);
}
