package com.huisu.iyoox.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.SerchBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SerchListAdapter extends BaseAdapter {
    private Context context;
    private List<SerchBean> mList;

    public SerchListAdapter(Context context, List<SerchBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_serch_list, null);
            holder = new HolderView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        holder.nameTv.setText(mList.get(position).getTitle());
        return convertView;
    }

    static class HolderView {
        @Bind(R.id.name_tv)
        TextView nameTv;

        HolderView(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
