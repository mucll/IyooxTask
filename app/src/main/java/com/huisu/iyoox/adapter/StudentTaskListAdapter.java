package com.huisu.iyoox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.TaskStudentListModel;
import com.huisu.iyoox.util.DateUtils;
import com.huisu.iyoox.util.StringUtils;

import java.util.ArrayList;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/13
 */
public class StudentTaskListAdapter extends BaseAdapter {

    private Context context;
    private String taskType;
    private ArrayList<TaskStudentListModel> listModels;

    public StudentTaskListAdapter(Context context, ArrayList<TaskStudentListModel> listModels, String taskType) {
        this.context = context;
        this.taskType = taskType;
        this.listModels = listModels;
    }

    @Override
    public int getCount() {
        return listModels == null ? 0 : listModels.size();
    }

    @Override
    public TaskStudentListModel getItem(int position) {
        return listModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        TaskStudentListModel model = getItem(position);
        if (!TextUtils.isEmpty(model.getXueke_name())) {
            holder.subjectIcon.setImageResource(getImgResId(model.getXueke_name()));
            holder.taskName.setText(model.getWork_name());
        }
        switch (taskType) {
            case TaskStatus.UNFINISH:
                holder.startIconTV.setImageResource(R.drawable.homework_icon_time);
                holder.endIcon.setVisibility(View.GONE);
                holder.endIcon.setVisibility(View.GONE);
                holder.startTime.setText(StringUtils.getTimeString(model.getStart_time()) + "-" + StringUtils.getTimeString(model.getEnd_time()));
                break;
            case TaskStatus.FINISH:
                holder.startIconTV.setImageResource(R.drawable.homework_icon_finished);
                holder.endIcon.setVisibility(View.VISIBLE);
                holder.endIcon.setVisibility(View.VISIBLE);
                holder.startTime.setText(StringUtils.getTimeString(model.getEnd_time()));
                break;
            default:
                break;
        }
        return convertView;
    }


    private int getImgResId(String subjectName) {
        switch (subjectName) {
            case "语文":
                return R.drawable.homework_yu;
            case "数学":
                return R.drawable.homework_math;
            case "英语":
                return R.drawable.homework_eng;
            case "物理":
                return R.drawable.homework_physics;
            case "化学":
                return R.drawable.homework_chemistry;
            default:
                return R.drawable.homework_yu;
        }
    }

    static class ViewHolder {

        TextView taskName;
        TextView classId;
        ImageView startIconTV;
        TextView startTime;
        ImageView endIcon;
        ImageView subjectIcon;

        public ViewHolder(View view) {
            taskName = view.findViewById(R.id.item_task_subject_text_tv);
            classId = view.findViewById(R.id.item_task_student_classId);
            startIconTV = view.findViewById(R.id.task_start_iv);
            startTime = view.findViewById(R.id.task_start_time_iv);
            endIcon = view.findViewById(R.id.task_finished_icon_iv);
            subjectIcon = view.findViewById(R.id.item_task_subject_icon_iv);
        }
    }
}
