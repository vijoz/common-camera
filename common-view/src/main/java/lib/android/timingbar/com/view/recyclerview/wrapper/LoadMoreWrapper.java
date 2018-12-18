package lib.android.timingbar.com.view.recyclerview.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import lib.android.timingbar.com.view.recyclerview.base.ViewHolder;
import lib.android.timingbar.com.view.recyclerview.utils.LoadMoreScrollListener;
import lib.android.timingbar.com.view.recyclerview.utils.WrapperUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * LoadMoreWrapper
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 自动加载更多，有加载中、加载出错、加载完成3中状态，每种状态的ui可以自己自定义
 *
 * @author rqmei on 2018/2/28
 */

public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //加载失败
    public static final int ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1;
    //没有更多
    public static final int ITEM_TYPE_NO_MORE_VIEW = Integer.MAX_VALUE - 2;
    //加载更多
    public static final int ITEM_TYPE_LOAD_MORE_VIEW = Integer.MAX_VALUE - 3;
    //不展示footer view
    public static final int ITEM_TYPE_NO_VIEW = Integer.MAX_VALUE - 4;
    private RecyclerView.Adapter mInnerAdapter;
    //加载更多状态的View
    private View mLoadMoreView;
    //加载更多的LayoutId
    private int mLoadMoreLayoutId;
    //加载失败状态的View
    private View mLoadMoreFailedView;
    //加载更多的LayoutId
    private int mLoadMoreFailedLayoutId;
    //没有更多状态的view
    private View mNoMoreView;
    //加载更多的LayoutId
    private int mNoMoreLayoutId;
    //当前状态（默认加载更多）
    private int mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
    //滑动到底部自动加载更多的事件
    private LoadMoreScrollListener mLoadMoreScrollListener;
    //标记是否加载出错
    private boolean isLoadError = false;
    //标记是否有相关状态提示的底部view
    private boolean isHaveStatesView = true;
    private boolean isShowFootView = false;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        this.mInnerAdapter = adapter;
        mLoadMoreScrollListener = new LoadMoreScrollListener () {
            @Override
            public void loadMore() {
                if (mOnLoadListener != null && isHaveStatesView) {
                    if (!isLoadError) {
                        // showLoadMore ();
                        mOnLoadListener.onLoadMore ();
                    }
                }
            }
        };
    }

    /**
     * 显示加载更多
     */
    public void showLoadMore() {
        mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
        isLoadError = false;
        isHaveStatesView = true;
        isShowFootView = true;
        notifyItemChanged (getItemCount ());
    }

    /**
     * 显示加载出错
     */
    public void showLoadError() {
        mCurrentItemType = ITEM_TYPE_LOAD_FAILED_VIEW;
        isLoadError = true;
        isHaveStatesView = true;
        isShowFootView = true;
        notifyItemChanged (getItemCount ());
    }

    /**
     * 显示加载完成
     */
    public void showLoadComplete() {
        mCurrentItemType = ITEM_TYPE_NO_MORE_VIEW;
        isLoadError = false;
        isHaveStatesView = true;
        isShowFootView = true;
        notifyItemChanged (getItemCount ());
    }

    /**
     * 取消状态提示的相关底部View
     */
    public void disableLoadMore() {
        mCurrentItemType = ITEM_TYPE_NO_VIEW;
        isHaveStatesView = false;
        notifyDataSetChanged ();
    }

    //region Get ViewHolder

    /**
     * 获取加载更多相关状态提示的ViewHolder
     *
     * @param parent   父类容器
     * @param layoutId 布局id
     * @param view     view控件
     * @param msg      默认提示消息
     * @return
     */
    private ViewHolder getViewHolder(ViewGroup parent, int layoutId, View view, String msg) {
        ViewHolder holder;
        if (layoutId != 0) {
            holder = ViewHolder.createViewHolder (parent.getContext (), parent, layoutId);
        } else {
            if (view == null) {
                view = new TextView (parent.getContext ());
                view.setLayoutParams (new ViewGroup.LayoutParams (MATCH_PARENT, WRAP_CONTENT));
                view.setPadding (20, 20, 20, 20);
                ((TextView) view).setText (msg);
                ((TextView) view).setGravity (Gravity.CENTER);
            }
            holder = ViewHolder.createViewHolder (view);
        }
        return holder;
    }

    //endregion
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount () - 1 && isHaveStatesView) {
            return mCurrentItemType;
        }
        return mInnerAdapter.getItemViewType (position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i ("common-view","LoadMoreWrapper onCreateViewHolder====");
        if (viewType == ITEM_TYPE_NO_MORE_VIEW) {
            Log.i ("common-view","LoadMoreWrapper onCreateViewHolder 1====");
            return getViewHolder (parent, mNoMoreLayoutId, mNoMoreView, "--end--");
        } else if (viewType == ITEM_TYPE_LOAD_MORE_VIEW) {
            Log.i ("common-view","LoadMoreWrapper onCreateViewHolder 2====");
            return getViewHolder (parent, mLoadMoreLayoutId, mLoadMoreView, "正在加载中");
        } else if (viewType == ITEM_TYPE_LOAD_FAILED_VIEW) {
            Log.i ("common-view","LoadMoreWrapper onCreateViewHolder 3====");
            return getViewHolder (parent, mLoadMoreFailedLayoutId, mLoadMoreFailedView, "加载失败，请点我重试");
        }
        return mInnerAdapter.onCreateViewHolder (parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i ("common-view","LoadMoreWrapper onBindViewHolder====");
        if (isFooterType (holder.getItemViewType ())) {
            //底部View相关逻辑处理
            if (!isShowFootView) {
                holder.itemView.setVisibility (View.GONE);
                return;
            } else {
                holder.itemView.setVisibility (View.VISIBLE);
            }
            if (holder.getItemViewType () == ITEM_TYPE_LOAD_FAILED_VIEW) {
                //加载失败
                holder.itemView.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        if (mOnLoadListener != null) {
                            //重新加载更多
                            mOnLoadListener.onRetry ();
                            showLoadMore ();
                        }
                    }
                });
                return;
            }
        }
        if (!isFooterType (holder.getItemViewType ())) {
            mInnerAdapter.onBindViewHolder (holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView (mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback () {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (position == getItemCount () - 1 && isHaveStatesView) {
                    return layoutManager.getSpanCount ();
                }
                if (oldLookup != null && isHaveStatesView) {
                    return oldLookup.getSpanSize (position);
                }
                return 1;
            }
        });
        recyclerView.addOnScrollListener (mLoadMoreScrollListener);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow (holder);
        if (holder.getLayoutPosition () == getItemCount () - 1 && isHaveStatesView) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams ();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan (true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount () + (isHaveStatesView ? 1 : 0);
    }

    public boolean isFooterType(int type) {

        return type == ITEM_TYPE_NO_VIEW ||
                type == ITEM_TYPE_LOAD_FAILED_VIEW ||
                type == ITEM_TYPE_NO_MORE_VIEW ||
                type == ITEM_TYPE_LOAD_MORE_VIEW;
    }

    /**
     * 通过view设置加载更多的UI
     *
     * @param loadMoreView
     * @return
     */
    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    /**
     * 通过layoutId设置加载更多的UI
     *
     * @param layoutId
     * @return
     */
    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }


    /**
     * 通过view设置加载失败的UI
     *
     * @param mLoadMoreFailedView
     * @return
     */
    public void setmLoadMoreFailedView(View mLoadMoreFailedView) {
        this.mLoadMoreFailedView = mLoadMoreFailedView;
    }


    /**
     * 通过layoutId设置加载失败的UI
     *
     * @param mLoadMoreFailedLayoutId
     * @return
     */
    public void setmLoadMoreFailedLayoutId(int mLoadMoreFailedLayoutId) {
        this.mLoadMoreFailedLayoutId = mLoadMoreFailedLayoutId;
    }


    /**
     * 通过view设置没有更多的UI
     *
     * @param mNoMoreView
     * @return
     */
    public void setmNoMoreView(View mNoMoreView) {
        this.mNoMoreView = mNoMoreView;
    }

    /**
     * 通过layoutId设置没有更多的UI
     *
     * @param mNoMoreLayoutId
     */
    public void setmNoMoreLayoutId(int mNoMoreLayoutId) {
        this.mNoMoreLayoutId = mNoMoreLayoutId;
    }

    //region 加载监听
    public interface OnLoadListener {
        //重试处理
        void onRetry();

        //加载更多
        void onLoadMore();
    }

    public void setShowFootView(boolean showFootView) {
        isShowFootView = showFootView;
    }

    private OnLoadListener mOnLoadListener;

    public LoadMoreWrapper setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
        return this;
    }

    //endregion
}