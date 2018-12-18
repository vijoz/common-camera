package com.timingbar.android.app;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lib.android.timingbar.com.base.mvp.IModel;
import lib.android.timingbar.com.base.mvp.IPresenter;

/**
 * BasePresenter
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/2/5
 */

public class BasePresenter<M extends IModel> implements IPresenter {
    //用于集中管理Observable订阅
    CompositeDisposable mCompositeDisposable;
    public M mModel;

    public BasePresenter() {
        onStart ();
    }

    public BasePresenter(M model) {
        this.mModel = model;
        onStart ();
    }


    @Override
    public void onStart() {
    }

    @Override
    public void onDestroy() {
        unDispose ();//解除订阅
        if (mModel != null)
            mModel.onDestory ();
        this.mModel = null;
        this.mCompositeDisposable = null;
    }

    /**
     * RXjava注册
     *
     * @param disposable
     */
    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable ();
        }
        mCompositeDisposable.add (disposable);//将所有disposable放入,集中处理
    }

    /**
     * RXjava取消注册，以避免内存泄露
     */
    protected void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear ();//保证activity结束时取消所有正在执行的订阅
        }
    }
}
