package com.timingbar.android.library.ui.adapter;

import android.content.Context;
import lib.android.timingbar.com.base.adapter.BaseListViewAdapter;

/**
 * ListViewAdapter
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/11/2
 */

public class ListViewAdapter extends BaseListViewAdapter {
    public ListViewAdapter(Context context) {
        super (context);
    }

    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, Object item, int position) {

    }

}
