package com.huisu.iyoox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.UserBaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 答题报告 题目number
 * @date: 18/6/28
 */
public class ClassRankingListAdapter extends BaseAdapter {
    private Context context;
    private List<UserBaseModel> data;

    public ClassRankingListAdapter(Context context, List<UserBaseModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_class_ranking_list_view_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserBaseModel model = data.get(position);
        holder.ranking.setText(position + 1 + "");
        holder.studentName.setText(TextUtils.isEmpty(model.getStudent_name()) ? "" : model.getStudent_name());

        holder.scoreTv.setText(model.getFenshu() + "%");
        if (model.getFenshu() >= 80) {
            holder.scoreTv.setTextColor(context.getResources().getColor(R.color.main_text_color));
        } else if (model.getFenshu() < 80 && model.getFenshu() >= 60) {
            holder.scoreTv.setTextColor(context.getResources().getColor(R.color.color666));
        } else {
            holder.scoreTv.setTextColor(context.getResources().getColor(R.color.screen_subject_version_text_color));
        }
        return convertView;
    }

    class ViewHolder {
        TextView ranking;
        TextView studentName;
        TextView scoreTv;


        ViewHolder(View view) {
            ranking = view.findViewById(R.id.item_ranking_tv);
            studentName = view.findViewById(R.id.item_student_name);
            scoreTv = view.findViewById(R.id.item_score);
        }
    }
}
