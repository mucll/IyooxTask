package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ScreenSubjectVersionModel;

import java.util.ArrayList;

public class ScreenVersionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ScreenSubjectVersionModel> data;
    private int selectPosition;

    public ScreenVersionAdapter(Context context, ArrayList<ScreenSubjectVersionModel> data) {
        this.context = context;
        this.data = data;
    }

    public void setSelectPosition(int position) {
        this.selectPosition = position;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ScreenSubjectVersionModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_screen_version_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.timeName.setText(data.get(position).getJiaocai_name());
        if (selectPosition == position) {
            holder.timeName.setSelected(true);
        } else {
            holder.timeName.setSelected(false);
        }
        return convertView;
    }

    class ViewHolder {
        TextView timeName;

        ViewHolder(View view) {
            timeName = (TextView) view.findViewById(R.id.item_screen_time_tv);
        }
    }
}
