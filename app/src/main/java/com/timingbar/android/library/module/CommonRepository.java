package com.timingbar.android.library.module;

import com.google.gson.reflect.TypeToken;
import com.timingbar.android.app.ApiConfig;
import com.timingbar.android.library.control.CommonControl;
import com.timingbar.android.library.module.entity.Lesson;
import io.reactivex.Observable;
import lib.android.timingbar.com.base.mvp.IRepositoryManager;
import lib.android.timingbar.com.http.EasyHttp;
import lib.android.timingbar.com.base.mvp.IModel;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

/**
 * CommonRepository
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/7
 */

public class CommonRepository implements CommonControl.Modle {
    IRepositoryManager manager;
    /**
     * 必须含有一个接收IRepositoryManager接口的构造函数,否则会报错
     *
     * @param manager
     */
    public CommonRepository(IRepositoryManager manager) {
        this.manager = manager;
    }
    /**
     * 获取视频章、
     * 268287
     * 365344
     */
    public Observable<List<Lesson>> getLessonPhase() {
        Observable<List<Lesson>> observable = EasyHttp.post ( ApiConfig.GET_LESSON_PHASE)
                .baseUrl (ApiConfig.HOSTSERVER)
                .addConverterFactory (GsonConverterFactory.create ())
                .params ("userTrainId", "365344")
               /* .execute (new CallClazzProxy<ApiResult<List<Lesson>>, List<Lesson>> (new TypeToken<List<Lesson>> () {
                }.getType ()) {
                });*/
                .execute (new TypeToken<List<Lesson>> () {
                }.getType ());
        return observable;
    }

    @Override
    public void onDestory() {
    }
}
