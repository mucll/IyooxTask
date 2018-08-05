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
import com.huisu.iyoox.entity.DianPingListModel;
import com.huisu.iyoox.entity.TaskTeacherListModel;
import com.huisu.iyoox.util.StringUtils;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/7/31
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherRemarkListAdapter extends RecyclerView.Adapter<TeacherRemarkListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DianPingListModel> models;

    public TeacherRemarkListAdapter(Context mContext, ArrayList<DianPingListModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_teacher_remark_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DianPingListModel model = models.get(position);
        holder.nameTv.setText(model.getName());
        holder.zhengquelv.setText(model.getZhengquelv() + "%");
        holder.timeTv.setText(StringUtils.getTimeString(model.getEnd_time()));
        holder.tiJiaoTv.setText("提交率:" + model.getTijiaolv() + "%");
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
        private TextView zhengquelv;
        private TextView tiJiaoTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.teacher_remark_list_task_name_tv);
            timeTv = itemView.findViewById(R.id.teacher_remark_list_end_time_tv);
            tiJiaoTv = itemView.findViewById(R.id.teacher_remark_list_tijiaolv_tv);
            zhengquelv = itemView.findViewById(R.id.teacher_remark_list_zhengquelv_tv);
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
