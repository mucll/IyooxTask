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
            holder.taskSubjectName.setText(model.getXueke_name());
        }
        if (!TextUtils.isEmpty(model.getClassroom_no())) {
            holder.classId.setText("ID:" + model.getClassroom_no());
        }
        if (!TextUtils.isEmpty(model.getStart_time())) {
            String startTime = model.getStart_time().replace("T", " ");
            String[] startTimes = startTime.split("-");
            holder.startTime.setText(startTimes[1] + "-" + startTimes[2]);
        }
        if (!TextUtils.isEmpty(model.getEnd_time())) {
            String endTime = model.getEnd_time().replace("T", " ");
            String[] endTimes = endTime.split("-");
            holder.endTime.setText(endTimes[1] + "-" + endTimes[2]);
        }
        switch (taskType) {
            case TaskStatus.UNFINISH:
                holder.startIconTV.setVisibility(View.VISIBLE);
                holder.startTime.setVisibility(View.VISIBLE);
                holder.endIcon.setVisibility(View.GONE);
                holder.endIcon.setVisibility(View.GONE);
                holder.xzyIcon.setVisibility(View.VISIBLE);
                holder.endIconTv.setText("止");
                holder.endIconTv.setBackground(context.getResources().getDrawable(R.drawable.shape_item_task_list_icon_bg));
                break;
            case TaskStatus.FINISH:
                holder.startIconTV.setVisibility(View.GONE);
                holder.startTime.setVisibility(View.GONE);
                holder.endIcon.setVisibility(View.VISIBLE);
                holder.endIcon.setVisibility(View.VISIBLE);
                holder.xzyIcon.setVisibility(View.GONE);
                holder.endIconTv.setText("");
                holder.endIconTv.setBackground(context.getResources().getDrawable(R.drawable.homework_icon_finished));
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

        TextView taskSubjectName;
        TextView classId;
        TextView startIconTV;
        TextView startTime;
        TextView endIconTv;
        TextView endTime;
        ImageView xzyIcon;
        ImageView endIcon;
        ImageView subjectIcon;

        public ViewHolder(View view) {
            taskSubjectName = view.findViewById(R.id.item_task_subject_text_tv);
            classId = view.findViewById(R.id.item_task_student_classId);
            startIconTV = view.findViewById(R.id.task_start_iv);
            startTime = view.findViewById(R.id.task_start_time_iv);
            endIconTv = view.findViewById(R.id.task_end_iv);
            endTime = view.findViewById(R.id.task_end_icon_iv);
            xzyIcon = view.findViewById(R.id.task_start_xzy_iv);
            endIcon = view.findViewById(R.id.task_finished_icon_iv);
            subjectIcon = view.findViewById(R.id.item_task_subject_icon_iv);
        }
    }
}
