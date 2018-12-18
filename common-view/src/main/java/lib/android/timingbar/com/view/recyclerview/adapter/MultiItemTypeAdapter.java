package lib.android.timingbar.com.view.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import lib.android.timingbar.com.view.recyclerview.base.ItemViewDelegate;
import lib.android.timingbar.com.view.recyclerview.base.ItemViewDelegateManager;
import lib.android.timingbar.com.view.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiItemTypeAdapter
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/2/28
 */

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;
    public int offset = 0;

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager ();
    }

    public MultiItemTypeAdapter(Context context) {
        this (context, new ArrayList<T> ());
    }

    /**
     * 获取当前项Item(position参数)是哪种类型的布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager ())
            return super.getItemViewType (position);
        return mItemViewDelegateManager.getItemViewType (mDatas.get (position), position);
    }

    /**
     * 通过viewType这个类型判断去创建不同item的ViewHolder
     *
     * @param parent
     * @param viewType View的类型
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mItemViewDelegateManager.getItemViewLayoutId (viewType);
        ViewHolder holder = ViewHolder.createViewHolder (mContext, parent, layoutId);
        setListener (parent, holder, viewType);
        return holder;
    }

    /**
     * 绑定holder 数据
     *
     * @param holder
     * @param t
     */
    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert (holder, t, holder.getAdapterPosition ());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    /**
     * 设置事件
     *
     * @param parent
     * @param viewHolder
     * @param viewType
     */
    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled (viewType))
            return;
        viewHolder.getConvertView ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition ();
                    mOnItemClickListener.onItemClick (v, viewHolder, mDatas.get (position - offset), position);
                }
            }
        });

        viewHolder.getConvertView ().setOnLongClickListener (new View.OnLongClickListener () {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition ();
                    return mOnItemClickListener.onItemLongClick (v, viewHolder, mDatas.get (position - offset), position);
                }
                return false;
            }
        });
    }

    /**
     * 绑定holder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert (holder, mDatas.get (position));
    }

    /**
     * @return 数据的个数
     */
    @Override
    public int getItemCount() {
        int itemCount = mDatas.size ();
        return itemCount;
    }

    /**
     * @return 数据值的集合
     */
    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate (itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate (viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount () > 0;
    }

    /**
     * RecyclerView 的item的点击事件
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T o, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addDataAll(List data) {
        mDatas.addAll (data);
    }

    /**
     * 清空数据
     */
    public void clearData() {
        mDatas.clear ();
    }

}