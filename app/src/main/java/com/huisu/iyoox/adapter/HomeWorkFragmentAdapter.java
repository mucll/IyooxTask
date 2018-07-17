package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;


public class HomeWorkFragmentAdapter extends BaseAdapter {
    private Context context;

    public HomeWorkFragmentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookListViewAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_work_layout, null);
            holder = new BookListViewAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BookListViewAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView textView;

        public ViewHolder(View view) {
        }
    }
}
