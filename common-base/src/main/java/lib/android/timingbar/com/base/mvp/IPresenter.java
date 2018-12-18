package lib.android.timingbar.com.base.mvp;

/**
 * IPresenter
 * -----------------------------------------------------------------------------------------------------------------------------------
 * mvp设计模式中的Presenter
 * 协调Model和View模块工作，处理交互
 *
 * @author rqmei on 2018/1/30
 */

public interface IPresenter {
    void onStart();

    void onDestroy();
}
