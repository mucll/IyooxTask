package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
public class StudentCollectAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CollectModel> models;

    public StudentCollectAdapter(Context context, ArrayList<CollectModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public CollectModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student_collect_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectModel titleModel = getItem(position);
        String string = titleModel.getGrade_name() +
                titleModel.getXueke_name() +
                titleModel.getGrade_detail_name();
        holder.subjectTv.setText(string);
        holder.videoNameTv.setText(titleModel.getVedio_name());
        holder.gradeDetailTv.setText(titleModel.getJiaocai_name());
        holder.iconBgIv.setBackgroundResource(getResId(titleModel.getJiaocai_id()));
        return convertView;
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
                return 1;
        }
    }

    static class ViewHolder {
        ImageView iconBgIv;
        TextView videoNameTv;
        TextView gradeDetailTv;
        TextView subjectTv;

        ViewHolder(View view) {
            iconBgIv = view.findViewById(R.id.collect_bg_iv);
            videoNameTv = view.findViewById(R.id.student_collect_video_name);
            gradeDetailTv = view.findViewById(R.id.video_grade_detail_tv);
            subjectTv = view.findViewById(R.id.collect_subject_name_tv);
        }
    }
}
