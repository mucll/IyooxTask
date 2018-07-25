package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ClassRoomModel;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/7/25
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherClassFragmentListAdapter extends RecyclerView.Adapter<TeacherClassFragmentListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ClassRoomModel> roomModels;

    public TeacherClassFragmentListAdapter(Context context, ArrayList<ClassRoomModel> roomModels) {
        this.context = context;
        this.roomModels = roomModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_teacher_class_fragment_list_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClassRoomModel model = roomModels.get(position);
        holder.className.setText(model.getName());
        holder.classId.setText("班级ID:" + model.getClassroom_no());
        holder.classNum.setText("班级人数:" + model.getStudent_num() + "人");
        holder.classCreateTime.setText(model.getCreate_date());
    }

    @Override
    public int getItemCount() {
        return roomModels == null ? 0 : roomModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView className;
        private TextView classId;
        private TextView classNum;
        private TextView classCreateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_room_name_tv);
            classId = itemView.findViewById(R.id.class_room_id_tv);
            classNum = itemView.findViewById(R.id.class_room_student_num_tv);
            classCreateTime = itemView.findViewById(R.id.class_room_create_time_tv);
        }
    }
}
