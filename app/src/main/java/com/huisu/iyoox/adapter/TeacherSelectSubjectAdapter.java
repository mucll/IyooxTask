package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.SubjectModel;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/25
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherSelectSubjectAdapter extends BaseAdapter {
    private Context context;
    private List<SubjectModel> models;
    private int selectId;

    public TeacherSelectSubjectAdapter(Context context, List<SubjectModel> models) {
        this.context = context;
        this.models = models;
    }


    public void setSelectPosition(int selectPosition) {
        this.selectId = selectPosition;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public SubjectModel getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher_select_subject_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SubjectModel model = getItem(position);
        holder.name.setText(model.getName());
        if (model.getKemu_id() == selectId) {
            holder.selectIv.setVisibility(View.VISIBLE);
        } else {
            holder.selectIv.setVisibility(View.INVISIBLE);
        }
        holder.icon.setImageResource(getSubjectIconId(model.getKemu_id()));
        return convertView;
    }


    private int getSubjectIconId(int subjectId) {
        switch (subjectId) {
            case Constant.SUBJECT_YUWEN:
                return R.drawable.subject_yu;
            case Constant.SUBJECT_SHUXUE:
                return R.drawable.subject_math;
            case Constant.SUBJECT_ENGLISH:
                return R.drawable.subject_eng;
            case Constant.SUBJECT_WULI:
                return R.drawable.subject_pyhsics;
            case Constant.SUBJECT_HUAXUE:
                return R.drawable.subject_chemistry;
            default:
                return R.drawable.subject_yu;
        }
    }

    static class ViewHolder {
        ImageView icon;
        ImageView selectIv;
        TextView name;

        ViewHolder(View view) {
            icon = view.findViewById(R.id.subject_icon_iv);
            name = view.findViewById(R.id.subject_name_tv);
            selectIv = view.findViewById(R.id.subject_is_select_iv);
        }
    }
}
