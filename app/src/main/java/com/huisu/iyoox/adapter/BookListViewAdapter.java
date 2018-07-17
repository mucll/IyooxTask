package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;

/**
 * 教材课本列表adapter
 */

public class BookListViewAdapter extends BaseAdapter {

    private Context context;

    public BookListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 15;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_book_list_view_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        getItem(position);
        holder.videoIcon.setSelected(true);
        holder.videoState.setSelected(true);
        return convertView;
    }

    static class ViewHolder {
        private ImageView videoIcon;
        private TextView videoName;
        private TextView videoState;

        public ViewHolder(View view) {
            videoIcon = view.findViewById(R.id.item_book_video_icon_iv);
            videoName = view.findViewById(R.id.item_video_name_tv);
            videoState = view.findViewById(R.id.item_video_state);
        }
    }
}
