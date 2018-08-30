package com.huisu.iyoox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.TaskTeacherLookTimuModel;

import java.util.ArrayList;

/**
 * ExercisesNumberViewAdapter
 */
public class ExercisesNumberViewAdapter extends BaseAdapter {
    private Context context;
    ArrayList<TaskTeacherLookTimuModel> models;
    private int type;

    public ExercisesNumberViewAdapter(Context context, ArrayList<TaskTeacherLookTimuModel> models, int type) {
        this.context = context;
        this.models = models;
        this.type = type;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exercises_number_view_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskTeacherLookTimuModel model = models.get(position);
        switch (type) {
            case Constant.NUMBER_RATE:
                holder.indexOfTv.setText(model.getTimu_index() + "");
                holder.answerTv.setText(model.getBili() + "%");
                holder.answerTv.setBackgroundResource(R.drawable.shape_oval_exercises_right_color_8dp);
                break;
            case Constant.NUMBER_ANSWER:
                holder.indexOfTv.setText(position + 1 + "");
                holder.answerTv.setText(TextUtils.isEmpty(model.getChooseanswer()) ? "未做" : model.getChooseanswer());
                if (model.getIs_correct() == Constant.ANSWER_ERROR) {
                    holder.answerTv.setBackgroundResource(R.drawable.shape_oval_exercises_error_color_8dp);
                } else {
                    holder.answerTv.setBackgroundResource(R.drawable.shape_oval_exercises_right_color_8dp);
                }
                break;
            default:
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView indexOfTv;
        TextView answerTv;

        ViewHolder(View view) {
            indexOfTv = view.findViewById(R.id.item_exercises_number_tv);
            answerTv = view.findViewById(R.id.item_exercises_number_answer_tv);
        }
    }
}
