package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/13
 */
public class StudentTaskListAdapter extends BaseAdapter {

    private Context context;
    private String taskType;

    public StudentTaskListAdapter(Context context, String taskType) {
        this.context = context;
        this.taskType = taskType;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_student_task_list_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (taskType) {
            case TaskStatus.UNFINISH:
                holder.xzyIcon.setVisibility(View.VISIBLE);
                break;
            case TaskStatus.FINISH:
                holder.startIconTV.setVisibility(View.GONE);
                holder.startTime.setVisibility(View.GONE);
                holder.endIcon.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return convertView;
    }

    static class ViewHolder {

        TextView startIconTV;
        TextView startTime;
        TextView endIconTv;
        TextView endTime;
        ImageView xzyIcon;
        ImageView endIcon;

        public ViewHolder(View view) {
            startIconTV = view.findViewById(R.id.task_start_iv);
            startTime = view.findViewById(R.id.task_start_time_iv);
            endIconTv = view.findViewById(R.id.task_end_iv);
            endTime = view.findViewById(R.id.task_end_icon_iv);
            xzyIcon = view.findViewById(R.id.task_start_xzy_iv);
            endIcon = view.findViewById(R.id.task_finished_icon_iv);
        }
    }
}
