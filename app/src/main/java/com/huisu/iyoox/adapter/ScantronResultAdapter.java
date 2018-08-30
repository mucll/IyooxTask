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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 答题卡adapter
 */
public class ScantronResultAdapter extends BaseAdapter {
    Context context;
    private ArrayList<ExercisesModel> mList;

    public ScantronResultAdapter(Context context, ArrayList<ExercisesModel> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_scantron_result, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExercisesModel item = mList.get(position);
        holder.subject_serial_number.setText(position + 1+"");
        holder.subject_serial_number.setTextColor(context.getResources().getColor(R.color.white));
        if (item.getAnswersModel() == null) {
            holder.subject_serial_number.setBackgroundResource(R.drawable.shape_rectangle_gray);
            holder.subject_serial_number.setTextColor(context.getResources().getColor(R.color.color333));
        } else {
            holder.subject_serial_number.setBackgroundResource(R.drawable.shape_rectangle_green);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.subject_serial_number)
        TextView subject_serial_number;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
