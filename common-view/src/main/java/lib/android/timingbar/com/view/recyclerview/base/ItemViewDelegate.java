package lib.android.timingbar.com.view.recyclerview.base;

/**
 * ItemViewDelegate
 * -----------------------------------------------------------------------------------------------------------------------------------
 * RecyclerView item对应的view相关类型配置数据
 *
 * @author rqmei on 2018/2/28
 */

public interface ItemViewDelegate<T> {
    /**
     * @return item view的布局id
     */
    int getItemViewLayoutId();

    /**
     * 获取当前项Item(position参数)是哪种类型的布局
     *
     * @param item
     * @param position
     * @return
     */
    boolean isForViewType(T item, int position);

    /**
     * 绑定数据
     *
     * @param holder   控件对应的holder
     * @param t        数据源
     * @param position 下标
     */
    void convert(ViewHolder holder, T t, int position);

}