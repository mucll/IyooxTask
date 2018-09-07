package com.huisu.iyoox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.student.StudentWriteExercisesErrorActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.views.canvasview.CardAdapterHelper;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/9/3
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class NewErrorExercisesAdapter extends RecyclerView.Adapter<NewErrorExercisesAdapter.ViewHodler> {

    private Context mContext;
    private ArrayList<SubjectModel> models;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    public NewErrorExercisesAdapter(Context mContext, ArrayList<SubjectModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_error_exercises_layout, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, view);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        final SubjectModel model = models.get(position);
        holder.imgView.setImageResource(getSubjectResId(model.getKemu_id()));
        if (StringUtils.isPad(mContext)) {
            holder.imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            holder.imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StudentWriteExercisesErrorActivity.class);
                intent.putExtra("subjectId", model.getKemu_id());
                intent.putExtra("subjectName", model.getKemu_name() + "错题");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder {

        private ImageView imgView;

        public ViewHodler(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageView);
        }
    }

    private int getSubjectResId(int subjectId) {
        switch (subjectId) {
            case Constant.SUBJECT_YUWEN:
                return R.drawable.ctj_subject_chinese;
            case Constant.SUBJECT_SHUXUE:
                return R.drawable.ctj_subject_math;
            case Constant.SUBJECT_ENGLISH:
                return R.drawable.ctj_subject_eng;
            case Constant.SUBJECT_WULI:
                return R.drawable.ctj_subject_physics;
            case Constant.SUBJECT_HUAXUE:
                return R.drawable.ctj_subject_chemical;
            default:
                return R.drawable.ctj_subject_chinese;
        }
    }
}
