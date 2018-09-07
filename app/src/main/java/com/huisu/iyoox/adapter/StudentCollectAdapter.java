package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.CollectModel;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.VideoTitleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Date: 2018/8/2
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class StudentCollectAdapter extends RecyclerView.Adapter<StudentCollectAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CollectModel> models;
    private MyOnItemClickListener onItemClickListener;
    private boolean isConfig = false;

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setConfig(boolean isConfig) {
        this.isConfig = isConfig;
    }

    public StudentCollectAdapter(Context context, ArrayList<CollectModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_collect_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CollectModel titleModel = models.get(position);
        String string =
                (TextUtils.isEmpty(titleModel.getGrade_name()) ? "" : titleModel.getGrade_name()) +
                        (TextUtils.isEmpty(titleModel.getXueke_name()) ? "" : titleModel.getXueke_name()) +
                        (TextUtils.isEmpty(titleModel.getGrade_detail_name()) ? "" : titleModel.getGrade_detail_name());
        holder.subjectTv.setText(string);
        holder.videoNameTv.setText(titleModel.getVedio_name());
        holder.gradeDetailTv.setText(titleModel.getJiaocai_name());
        holder.iconBgIv.setImageResource(getResId(titleModel.getXueke_id()));
        if (isConfig) {
            holder.deleteTv.setVisibility(View.VISIBLE);
        } else {
            holder.deleteTv.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    private int getResId(int kemuId) {
        switch (kemuId) {
            case Constant.SUBJECT_YUWEN:
                return R.drawable.bg_course_chinese2;
            case Constant.SUBJECT_SHUXUE:
                return R.drawable.bg_course_math2;
            case Constant.SUBJECT_ENGLISH:
                return R.drawable.bg_course_english2;
            case Constant.SUBJECT_WULI:
                return R.drawable.bg_course_physics2;
            case Constant.SUBJECT_HUAXUE:
                return R.drawable.bg_course_chemical2;
            default:
                return R.drawable.bg_course_chinese2;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconBgIv;
        TextView videoNameTv;
        TextView gradeDetailTv;
        TextView subjectTv;
        View deleteTv;

        public ViewHolder(View view) {
            super(view);
            iconBgIv = view.findViewById(R.id.collect_bg_iv);
            videoNameTv = view.findViewById(R.id.student_collect_video_name);
            gradeDetailTv = view.findViewById(R.id.video_grade_detail_tv);
            subjectTv = view.findViewById(R.id.collect_subject_name_tv);
            deleteTv = view.findViewById(R.id.is_delete_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}
