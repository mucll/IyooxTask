package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TaskTeacherListModel;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/7/31
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherLookTaskListAdapter extends RecyclerView.Adapter<TeacherLookTaskListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<TaskTeacherListModel> models;
    private int type;

    public TeacherLookTaskListAdapter(Context mContext, ArrayList<TaskTeacherListModel> models, int type) {
        this.mContext = mContext;
        this.models = models;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_teacher_look_task_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskTeacherListModel model = models.get(position);
        holder.nameTv.setText(model.getName());
        holder.timeTv.setText(model.getCreatedate());
        switch (type) {
            case Constant.TASK_SEND_CODE:
                holder.deleteTv.setVisibility(View.GONE);
                holder.nextIv.setVisibility(View.VISIBLE);
                break;
            case Constant.TASK_UNSEND_CODE:
                holder.deleteTv.setVisibility(View.VISIBLE);
                holder.nextIv.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }


    private MyOnItemClickListener onItemClickListener;


    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTv;
        private TextView timeTv;
        private TextView deleteTv;
        private ImageView nextIv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.task_name_tv);
            timeTv = itemView.findViewById(R.id.task_send_time_tv);
            deleteTv = itemView.findViewById(R.id.teacher_look_task_delete_tv);
            nextIv = itemView.findViewById(R.id.teacher_look_task_next_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}
