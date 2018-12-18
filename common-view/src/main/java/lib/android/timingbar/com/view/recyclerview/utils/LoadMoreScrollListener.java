package lib.android.timingbar.com.view.recyclerview.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * LoadMoreScrollListener
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 用于RecyclerView加载更多的监听，实现滑动到底部自动加载更多
 *
 * @author rqmei on 2018/3/6
 */

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {


    private int previousTotal;
    //控制不要重复加载更多 
    private boolean isLoading = true;
    private LinearLayoutManager lm;
    private StaggeredGridLayoutManager sm;
    private int[] lastPositions;
    //item总数量 
    private int totalItemCount;
    //屏幕上最后一个可见item位置位置,只显示部分也算 
    private int lastVisibleItemPosition;
    ///可见item总数量,只显示了部分也算可见 
    private int visibleItemCount;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged (recyclerView, newState);
        if (recyclerView.getLayoutManager () instanceof LinearLayoutManager) {
            lm = (LinearLayoutManager) recyclerView.getLayoutManager ();
        } else if (recyclerView.getLayoutManager () instanceof StaggeredGridLayoutManager) {
            sm = (StaggeredGridLayoutManager) recyclerView.getLayoutManager ();
            lastPositions = sm.findLastVisibleItemPositions (null);
        }

        visibleItemCount = recyclerView.getChildCount ();
        if (lm != null) {
            totalItemCount = lm.getItemCount ();
            lastVisibleItemPosition = lm.findLastVisibleItemPosition ();
            //  visibleItemCount = lm.getChildCount ();
        } else if (sm != null) {
            totalItemCount = sm.getItemCount ();
            lastVisibleItemPosition = lastPositions[0];
        }

        if (isLoading) {
            if (totalItemCount > previousTotal) {//加载更多结束
                isLoading = false;
                previousTotal = totalItemCount;
            } else if (totalItemCount < previousTotal) {//用户刷新结束
                previousTotal = totalItemCount;
                isLoading = false;
            } else {//有可能是在第一页刷新也可能是加载完毕

            }


        }
        if (!isLoading && visibleItemCount > 0 && totalItemCount - 1 == lastVisibleItemPosition && totalItemCount > visibleItemCount && recyclerView.getScrollState () == RecyclerView.SCROLL_STATE_IDLE) {
            loadMore ();
        }

    }


    public abstract void loadMore();
}