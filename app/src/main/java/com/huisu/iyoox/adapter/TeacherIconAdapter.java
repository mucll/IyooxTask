package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.TeacherModel;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherIconAdapter extends RecyclerView.Adapter<TeacherIconAdapter.ViewHolder> {
    private Context context;
    private List<TeacherModel> teacherModels;

    public TeacherIconAdapter(Context context, List<TeacherModel> teacherModels) {
        this.context = context;
        this.teacherModels = teacherModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_class_teacher_recycler_view_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TeacherModel teacherModel = teacherModels.get(position);
        holder.teacherName.setText(teacherModel.getTeacher_name());
        holder.subjectTv.setText("(" + teacherModel.getXueke_name() + ")");
    }

    @Override
    public int getItemCount() {
        return teacherModels == null ? 0 : teacherModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView teacherName;
        private TextView subjectTv;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_class_teacher_head_icon_view);
            teacherName = itemView.findViewById(R.id.item_class_teacher_name_tv);
            subjectTv = itemView.findViewById(R.id.item_class_teacher_subject_tv);
        }
    }
}
