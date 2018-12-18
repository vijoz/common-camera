package lib.android.timingbar.com.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseListViewAdapter
 * -----------------------------------------------------------------------------------------------------------------------------------
 * ListView适配器基类
 *
 * @author rqmei on 2018/11/2
 */

public abstract class BaseListViewAdapter<T, VH extends BaseListViewAdapter.ViewHolder> extends BaseAdapter {
    private List<T> mDatas;
    private Context mContext;

    public BaseListViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size ();
    }

    /**
     * 获取某个位置上的数据
     */
    @Override
    public T getItem(int position) {
        return mDatas == null ? null : mDatas.get (position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh = ViewHolder.getViewHolder (this.mContext, convertView, parent, this.getLayoutResId (), position);
        onBindViewHolder (vh, getItem (position), position);
        return vh.getConvertView ();

    }

    public abstract int getLayoutResId();

    // 提供给外部填充实际的显示数据，以及可以一些其他的操作，如：隐藏＝＝
    public abstract void onBindViewHolder(ViewHolder vh, T item, int position);


    /**
     * 设置新的数据
     */
    public void setData(List<T> data) {
        mDatas = data;
        notifyDataSetChanged ();
    }

    /**
     * 获取当前数据
     */
    public List<T> getData() {
        return mDatas;
    }

    /**
     * 追加一些数据
     */
    public void addData(List<T> data) {
        if (mDatas != null) {
            mDatas.addAll (data);
        } else {
            mDatas = data;
        }
        notifyDataSetChanged ();
    }

    /**
     * 清空当前数据
     */
    public void clearData() {
        //当前的数据不能为空
        if (mDatas == null || mDatas.size () == 0)
            return;

        mDatas.clear ();
        notifyDataSetChanged ();
    }


    /**
     * 更新某个位置上的数据
     */
    public void setItem(int position, T item) {
        if (mDatas == null)
            mDatas = new ArrayList<> ();
        mDatas.set (position, item);
        notifyDataSetChanged ();
    }

    /**
     * 添加单条数据
     */
    public void addItem(T item) {
        addItem (mDatas.size () - 1, item);
    }

    /**
     * 添加单条数据
     */
    public void addItem(int position, T item) {
        if (mDatas == null)
            mDatas = new ArrayList<> ();

        //如果是在for循环添加后要记得position++
        if (position < mDatas.size ()) {
            mDatas.add (position, item);
        } else {
            mDatas.add (item);
        }
        notifyDataSetChanged ();
    }

    /**
     * 删除单条数据
     */
    public void removeItem(T item) {
        int index = mDatas.indexOf (item);
        if (index != -1) {
            removeItem (index);
        }
    }

    public void removeItem(int position) {
        //如果是在for循环删除后要记得i--
        mDatas.remove (position);
        notifyDataSetChanged ();
    }


    //----------------------------------------------------------------viewHolder------------------------------------------------------------------------
    public static class ViewHolder {

        // 用于存储listView item的所有控件容器,和map类似,比map效率高，键只能为Integer
        private SparseArray<View> mViews;

        // item根view
        private View mConvertView;

        protected Context mContext;

        private static int position;

        public ViewHolder(Context context, ViewGroup parent, int layoutId) {
            this.mViews = new SparseArray ();
            this.mConvertView = LayoutInflater.from (context).inflate (layoutId, parent, false);
            this.mConvertView.setTag (this);
            this.mContext = context;
        }

        /**
         * 获取一个viewHolder
         *
         * @param context     context
         * @param convertView view
         * @param parent      parent view
         * @param layoutId    布局资源id
         * @param position    索引
         * @return
         */
        public static ViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
            ViewHolder.position = position;
            if (convertView == null) {
                return new ViewHolder (context, parent, layoutId);
            }
            return (ViewHolder) convertView.getTag ();
        }

        public int getPosition() {
            return this.position;
        }

        // 通过一个viewId来获取一个view
        public <T extends View> T getView(int viewId) {

            View view = mViews.get (viewId);
            if (view == null) {
                view = mConvertView.findViewById (viewId);
                mViews.put (viewId, view);
            }
            return (T) view;
        }

        // 返回viewHolder的容器类
        public View getConvertView() {
            return this.mConvertView;
        }

        // 给TextView设置文字
        public void setText(int viewId, String text) {
            TextView tv = getView (viewId);
            if (text == null) {
                tv.setText ("");
            } else {
                tv.setText (text);
            }
        }

        // 给TextView设置文字
        public void setText(int viewId, int textRes) {
            TextView tv = getView (viewId);
            tv.setText (textRes);
        }

        public void setTextColor(int viewId, int colorId) {
            TextView tv = getView (viewId);
            tv.setTextColor (colorId);
        }

        public void setBackgroundColor(int viewId, int colorId) {
            View view = getView (viewId);
            view.setBackgroundColor (colorId);
        }

        public void setBackground(int viewId, int resId) {
            View view = getView (viewId);
            view.setBackgroundResource (resId);
        }


        // 给ImageView设置图片资源
        public void setImageResource(int viewId, int resId) {
            ImageView iv = getView (viewId);
            iv.setImageResource (resId);
        }

        public void setVisible(int viewId, boolean visible) {
            View view = getView (viewId);
            view.setVisibility (visible ? View.VISIBLE : View.GONE);
        }

        public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
            View view = getView (viewId);
            view.setOnClickListener (onClickListener);
        }

    }
}
