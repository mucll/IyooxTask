package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookEditionModel;
import com.huisu.iyoox.entity.StudentModel;
import com.huisu.iyoox.util.ImageLoader;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/25
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherClassRoomDetailsAdapter extends RecyclerView.Adapter<TeacherClassRoomDetailsAdapter.ViewHolder> {
    private Context context;
    private List<StudentModel> models;

    public TeacherClassRoomDetailsAdapter(Context context, List<StudentModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_teacher_class_room_details_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StudentModel model = models.get(position);
        holder.number.setText(position + 1 + "");
        holder.name.setText(model.getStudent_name());
        ImageLoader.load(context, holder.head,
                TextUtils.isEmpty(model.getStudent_avatar()) ? "" : model.getStudent_avatar(),
                R.drawable.student_default, 0);
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    private MyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView number;
        private TextView name;
        private ImageView head;

        public ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.teacher_class_details_position_tv);
            head = itemView.findViewById(R.id.teacher_class_details_student_head_iv);
            name = itemView.findViewById(R.id.teacher_class_details_student_name_tv);
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}
