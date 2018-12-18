package lib.android.timingbar.com.view.recyclerview.base;

import android.support.v4.util.SparseArrayCompat;

/**
 * ItemViewDelegateManager
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/2/28
 */

public class ItemViewDelegateManager<T> {
    /**
     * 存放RecyclerView item对应的view相关的配置数据
     */
    SparseArrayCompat<ItemViewDelegate<T>> delegates = new SparseArrayCompat ();

    /**
     * 获取当前RecyclerView的item总共有多少种类型的布局。
     *
     * @return RecyclerView的item的类型总数
     */
    public int getItemViewDelegateCount() {
        return delegates.size ();
    }

    /**
     * 添加RecyclerView的item类型
     *
     * @param delegate item配置的类型值
     * @return
     */
    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> delegate) {
        int viewType = delegates.size ();
        if (delegate != null) {
            delegates.put (viewType, delegate);
            viewType++;
        }
        return this;
    }

    /**
     * 添加RecyclerView的item类型
     *
     * @param viewType 类型的标识键
     * @param delegate item配置的类型值
     * @return
     */
    public ItemViewDelegateManager<T> addDelegate(int viewType, ItemViewDelegate<T> delegate) {
        if (delegates.get (viewType) != null) {
            throw new IllegalArgumentException (
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + delegates.get (viewType));
        }
        delegates.put (viewType, delegate);
        return this;
    }

    /**
     * 通过配置值{@link ItemViewDelegate}删除RecyclerView的item类型
     *
     * @param delegate
     * @return
     */
    public ItemViewDelegateManager<T> removeDelegate(ItemViewDelegate<T> delegate) {
        if (delegate == null) {
            throw new NullPointerException ("ItemViewDelegate is null");
        }
        int indexToRemove = delegates.indexOfValue (delegate);

        if (indexToRemove >= 0) {
            delegates.removeAt (indexToRemove);
        }
        return this;
    }

    /**
     * 通过配置时的键itemType删除RecyclerView的item类型
     *
     * @param itemType
     * @return
     */
    public ItemViewDelegateManager<T> removeDelegate(int itemType) {
        int indexToRemove = delegates.indexOfKey (itemType);

        if (indexToRemove >= 0) {
            delegates.removeAt (indexToRemove);
        }
        return this;
    }

    /**
     * 获取当前项Item(position参数)是哪种类型的布局
     *
     * @param item
     * @param position
     * @return
     */
    public int getItemViewType(T item, int position) {
        int delegatesCount = delegates.size ();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewDelegate<T> delegate = delegates.valueAt (i);
            if (delegate.isForViewType (item, position)) {
                return delegates.keyAt (i);
            }
        }
        throw new IllegalArgumentException (
                "No ItemViewDelegate added that matches position=" + position + " in data source");
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param item
     * @param position
     */
    public void convert(ViewHolder holder, T item, int position) {
        int delegatesCount = delegates.size ();
        for (int i = 0; i < delegatesCount; i++) {
            ItemViewDelegate<T> delegate = delegates.valueAt (i);
            if (delegate.isForViewType (item, position)) {
                delegate.convert (holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException (
                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }

    /**
     * 通过view的类型获取item view 的布局id
     *
     * @param viewType
     * @return
     */
    public int getItemViewLayoutId(int viewType) {
        return delegates.get (viewType).getItemViewLayoutId ();
    }

    /**
     * 获取item view 对应的类型（type）
     *
     * @param itemViewDelegate
     * @return
     */
    public int getItemViewType(ItemViewDelegate itemViewDelegate) {
        return delegates.indexOfValue (itemViewDelegate);
    }
}
