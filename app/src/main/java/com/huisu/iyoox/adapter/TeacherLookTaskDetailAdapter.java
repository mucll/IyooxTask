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

/**
 * Function:
 * Date: 2018/7/31
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherLookTaskDetailAdapter extends RecyclerView.Adapter<TeacherLookTaskDetailAdapter.ViewHolder> {
    private MyOnItemClickListener onItemClickListener;
    private Context context;
    private int type;

    public TeacherLookTaskDetailAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_look_task_detail_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nameTv.setText("学生" + (position + 1));
        switch (type) {
            case Constant.TASK_STUDENT_FINISHED:
                holder.indexOfTv.setText(position + 1 + "");
                break;
            case Constant.TASK_STUDENT_UNFINISH:
                holder.indexOfTv.setVisibility(View.GONE);
                holder.rateTv.setVisibility(View.GONE);
                holder.nextIv.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView indexOfTv;
        private TextView nameTv;
        private TextView rateTv;
        private ImageView nextIv;

        public ViewHolder(View itemView) {
            super(itemView);
            indexOfTv = itemView.findViewById(R.id.student_indexof_tv);
            nameTv = itemView.findViewById(R.id.task_detail_student_name_tv);
            rateTv = itemView.findViewById(R.id.task_detail_rate_tv);
            nextIv = itemView.findViewById(R.id.next_iv);
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
