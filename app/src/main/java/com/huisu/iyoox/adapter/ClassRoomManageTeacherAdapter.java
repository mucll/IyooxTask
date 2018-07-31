package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookEditionModel;
import com.huisu.iyoox.entity.TeacherModel;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/25
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ClassRoomManageTeacherAdapter extends RecyclerView.Adapter<ClassRoomManageTeacherAdapter.ViewHolder> {
    private Context context;
    private List<TeacherModel> models;
    private boolean isShowCheck = false;
    private int userId;

    public ClassRoomManageTeacherAdapter(Context context, List<TeacherModel> models, int userId) {
        this.context = context;
        this.models = models;
        this.userId = userId;
    }

    public void setShowCheck(boolean isShowCheck) {
        this.isShowCheck = isShowCheck;
    }


    public boolean getShowCheck() {
        return isShowCheck;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_class_room_manage_teacher_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TeacherModel model = models.get(position);
        holder.teacherName.setText(model.getTeacher_name());
        if (isShowCheck) {
            if (model.getTeacher_id() != userId) {
                holder.checkIv.setVisibility(View.VISIBLE);
                if (model.isSelect()) {
                    holder.checkIv.setSelected(true);
                } else {
                    holder.checkIv.setSelected(false);
                }
            }
        } else {
            holder.checkIv.setVisibility(View.INVISIBLE);
        }
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
        private TextView teacherName;
        private ImageView checkIv;

        public ViewHolder(View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.class_manage_teacher_name_tv);
            checkIv = itemView.findViewById(R.id.is_check_iv);
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
