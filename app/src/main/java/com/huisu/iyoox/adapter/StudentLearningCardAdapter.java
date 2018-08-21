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
import com.huisu.iyoox.entity.LearningCardModel;
import com.huisu.iyoox.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Date: 2018/8/9
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class StudentLearningCardAdapter extends RecyclerView.Adapter<StudentLearningCardAdapter.ViewHolder> {
    private MyOnItemClickListener onItemClickListener;
    private Context context;
    private ArrayList<LearningCardModel> models;

    public StudentLearningCardAdapter(Context context, ArrayList<LearningCardModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_learning_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LearningCardModel model = models.get(position);
        holder.videoIcon.setImageResource(StringUtils.getCardResId(model.getId()));
        holder.typeName.setText("尚课啦"+model.getType_name());
        holder.endTimeTv.setText("有效期:" + StringUtils.getTimeStringYMD(model.getEnd_date()));
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView videoIcon;
        private TextView typeName;
        private TextView endTimeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            videoIcon = itemView.findViewById(R.id.item_learning_card_bg_iv);
            typeName = itemView.findViewById(R.id.item_learning_card_type_name);
            endTimeTv = itemView.findViewById(R.id.item_learning_card_end_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
