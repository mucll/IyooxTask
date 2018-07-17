package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.GradeModel;
import com.huisu.iyoox.util.LogUtil;

import java.util.List;

public class GradeAdapter extends BaseAdapter {
    private Context context;
    private int selectGrade;
    private List<GradeListModel> gradeModels;

    public GradeAdapter(Context context, List<GradeListModel> gradeModels, int selectGrade) {
        this.context = context;
        this.gradeModels = gradeModels;
        this.selectGrade = selectGrade;
    }

    @Override
    public int getCount() {
        return gradeModels == null ? 0 : gradeModels.size();
    }

    @Override
    public GradeListModel getItem(int position) {
        return gradeModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_grade_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GradeListModel model = getItem(position);
        int resId = getGradeIcon(model.getGrade_id());
        holder.textView.setText(model.getName());
        holder.mIconIv.setImageResource(resId);
        if (selectGrade == position) {
            holder.textView.setSelected(true);
        } else {
            holder.textView.setSelected(false);
        }
        return convertView;
    }

    private int getGradeIcon(int position) {
        switch (position) {
            case 1:
                return R.drawable.reg_grade_1;
            case 2:
                return R.drawable.reg_grade_2;
            case 3:
                return R.drawable.reg_grade_3;
            case 4:
                return R.drawable.reg_grade_4;
            case 5:
                return R.drawable.reg_grade_5;
            case 6:
                return R.drawable.reg_grade_6;
            case 7:
                return R.drawable.reg_grade_7;
            case 8:
                return R.drawable.reg_grade_8;
            case 9:
                return R.drawable.reg_grade_9;
            default:
                return R.drawable.reg_grade_9;
        }
    }

    static class ViewHolder {
        private TextView textView;
        private ImageView mIconIv;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.item_select_grade_tv);
            mIconIv = (ImageView) view.findViewById(R.id.item_grade_icon_iv);
        }
    }

}
