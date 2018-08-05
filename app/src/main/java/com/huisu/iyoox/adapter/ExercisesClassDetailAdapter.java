package com.huisu.iyoox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesStudentDetailModel;
import com.huisu.iyoox.entity.TaskTeacherLookTimuModel;
import com.huisu.iyoox.views.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * ExercisesNumberViewAdapter
 */
public class ExercisesClassDetailAdapter extends BaseAdapter {
    private Context context;
    private List<ExercisesStudentDetailModel> models;

    public ExercisesClassDetailAdapter(Context context, List<ExercisesStudentDetailModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public ExercisesStudentDetailModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exercises_class_detail_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExercisesStudentDetailModel model = getItem(position);
        holder.userIcon.setHead(model.getStudent_id() + "", "", TextUtils.isEmpty(model.getAvatar()) ? "" : model.getAvatar(), Constant.STUDENT_TYPE);
        holder.userName.setText(model.getStudent_name());
        return convertView;
    }

    static class ViewHolder {
        HeadView userIcon;
        TextView userName;

        ViewHolder(View view) {
            userIcon = view.findViewById(R.id.user_icon);
            userName = view.findViewById(R.id.user_name);
        }
    }
}
