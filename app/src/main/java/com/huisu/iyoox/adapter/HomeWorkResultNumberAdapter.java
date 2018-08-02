package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.ExercisesResultModel;

import java.util.ArrayList;

/**
 * @author: dl
 * @function: 答题报告 题目number
 * @date: 18/6/28
 */
public class HomeWorkResultNumberAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ExercisesResultModel> exercisesModels;

    public HomeWorkResultNumberAdapter(Context context, ArrayList<ExercisesResultModel> exercisesModels) {
        this.context = context;
        this.exercisesModels = exercisesModels;
    }

    @Override
    public int getCount() {
        return exercisesModels == null ? 0 : exercisesModels.size();
    }

    @Override
    public ExercisesResultModel getItem(int position) {
        return exercisesModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_answer_number_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExercisesResultModel model = getItem(position);
        holder.exercisesNumberTv.setText(position + 1 + "");
        if (model.getIs_correct() == Constant.ANSWER_CORRECT) {
            holder.exercisesNumberTv.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_exercises_right_color_8dp));
        } else {
            holder.exercisesNumberTv.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_exercises_error_color_8dp));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView exercisesNumberTv;

        ViewHolder(View view) {
            exercisesNumberTv = (TextView) view.findViewById(R.id.item_answer_result_exercises_number_tv);
        }
    }
}
