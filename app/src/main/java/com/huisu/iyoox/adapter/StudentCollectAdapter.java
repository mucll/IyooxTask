package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.VideoTitleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Date: 2018/8/2
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class StudentCollectAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VideoTitleModel> models;

    public StudentCollectAdapter(Context context, ArrayList<VideoTitleModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public VideoTitleModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student_collect_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VideoTitleModel titleModel = getItem(position);
        holder.zhishidianName.setText(titleModel.getShipin_name());
        return convertView;
    }

    static class ViewHolder {
        TextView zhishidianName;

        ViewHolder(View view) {
            zhishidianName = view.findViewById(R.id.student_collect_zhishidian_name);
        }
    }
}
