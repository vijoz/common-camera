package com.timingbar.android.library.presenter;

import com.timingbar.android.app.ApiConfig;
import com.timingbar.android.library.control.CommonControl;
import com.timingbar.android.library.module.CommonRepository;
import com.timingbar.android.library.module.entity.BaseJson;
import com.timingbar.android.library.module.entity.BaseResult;
import com.timingbar.android.library.module.entity.Lesson;
import com.timingbar.android.library.module.entity.VersionCode;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lib.android.timingbar.com.base.app.BaseApplication;
import lib.android.timingbar.com.base.mvp.EventMessage;
import lib.android.timingbar.com.base.util.EventBusUtils;
import lib.android.timingbar.com.http.EasyHttp;
import lib.android.timingbar.com.http.callback.SimpleCallBack;
import lib.android.timingbar.com.http.exception.ApiException;
import com.timingbar.android.app.BasePresenter;
import lib.android.timingbar.com.base.mvp.Message;
import lib.android.timingbar.com.http.module.ApiResult;
import lib.android.timingbar.com.http.subsciber.BaseSubscriber;
import lib.android.timingbar.com.http.util.HttpLog;

import java.util.List;

/**
 * CommonPresenter
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/10
 */

public class CommonPresenter extends BasePresenter<CommonRepository> {
    public CommonPresenter(BaseApplication application) {
        super (application.repositoryManager ().createRepository (CommonRepository.class));
    }

    public void getVersionCode() {
        Disposable disposable = EasyHttp.post (ApiConfig.GET_VERSION_CODE).params ("type", "1").execute (new SimpleCallBack<VersionCode> () {
            @Override
            public void onError(ApiException e) {
                HttpLog.i ("getVersionCode==" + e);
            }

            @Override
            public void onSuccess(VersionCode versionCode) {
                HttpLog.i ("getVersionCode==" + versionCode.getVersionName ());
            }
        });
        addDispose (disposable);
    }

    /**
     * 获取视频章
     */
    public void getLsssonPhase(final CommonControl.View view) {
        HttpLog.i ("CommonPresenter mModel=" + mModel);
        mModel.getLessonPhase ().doOnSubscribe (new Consumer<Disposable> () {
            @Override
            public void accept(Disposable disposable) throws Exception {
                view.showLoading ();
                //在订阅时必须调用这个方法,不然Activity退出时可能内存泄漏
                addDispose (disposable);
            }
        }).subscribe (new BaseSubscriber<List<Lesson>> () {
            @Override
            public void onError(ApiException e) {
                view.hideLoading ();

            }

            @Override
            public void onNext(List<Lesson> lessons) {
                super.onNext (lessons);
                view.hideLoading ();
                HttpLog.i ("size=" + lessons.size ());
                EventBusUtils.post (new EventMessage<> (100, lessons));
            }

        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        unDispose ();
    }
}
