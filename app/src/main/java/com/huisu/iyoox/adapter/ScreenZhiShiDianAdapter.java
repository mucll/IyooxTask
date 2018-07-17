package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ScreenSubjectVersionModel;
import com.huisu.iyoox.entity.ScreenZhiShiDianModel;

import java.util.ArrayList;

public class ScreenZhiShiDianAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ScreenZhiShiDianModel> data;
    private int selectPosition;

    public ScreenZhiShiDianAdapter(Context context, ArrayList<ScreenZhiShiDianModel> data) {
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
    public ScreenZhiShiDianModel getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_screen_zhishi_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.timeName.setText(data.get(position).getZhishidian_name());
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
