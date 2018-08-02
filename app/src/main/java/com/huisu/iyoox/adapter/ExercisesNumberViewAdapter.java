package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ExercisesModel;

import java.util.ArrayList;

/**
 * ExercisesNumberViewAdapter
 */
public class ExercisesNumberViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ExercisesModel> exercisesModels;

    public ExercisesNumberViewAdapter(Context context, ArrayList<ExercisesModel> exercisesModels) {
        this.context = context;
        this.exercisesModels = exercisesModels;
    }

    @Override
    public int getCount() {
        return 30;
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
        holder.indexOfTv.setText(position + 1 + "");
        return convertView;
    }

    static class ViewHolder {
        TextView indexOfTv;
        TextView answerTv;

        ViewHolder(View view) {
            indexOfTv = (TextView) view.findViewById(R.id.item_exercises_number_tv);
            answerTv = (TextView) view.findViewById(R.id.item_exercises_number_answer_tv);
        }
    }
}
