package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ExercisesChooseModel;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.views.BaseExercisesView;
import com.huisu.iyoox.views.HtmlTextView;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherSelectExercisesAdapter extends RecyclerView.Adapter<TeacherSelectExercisesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ExercisesModel> models;

    public TeacherSelectExercisesAdapter(Context context, ArrayList<ExercisesModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_select_exercises_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ExercisesModel model = models.get(position);
        String type = BaseExercisesView.getExercisesTypeString(model.getType() + "");
        String count = String.format("<font color='#FF5629'>(%s) </font>%s.", type, (position + 1));
        StringBuilder sb = new StringBuilder();
        sb.append(count).append(model.getTigan());
        String s = sb.toString().replaceAll("style='max-width:100%'", "style='max-width:100%;height:auto;'");
        //显示题干
        if (!s.equals(holder.tiGanView.text)) {
            if (!TextUtils.isEmpty(s)) {
                holder.tiGanView.setHtmlFromString(s, false);
            } else {
                holder.tiGanView.setHtmlFromString("", false);
            }
        }
        holder.optionView.removeAllViews();
        if (model.getOption_One() != null) {
            ExercisesChooseModel mData = model.getOption_One();
//        填充选项
            if (!TextUtils.isEmpty(mData.getA())) {
                setChooseData("A", mData.getA(), holder.optionView);
            }
            if (!TextUtils.isEmpty(mData.getB())) {
                setChooseData("B", mData.getB(), holder.optionView);
            }
            if (!TextUtils.isEmpty(mData.getC())) {
                setChooseData("C", mData.getC(), holder.optionView);
            }
            if (!TextUtils.isEmpty(mData.getD())) {
                setChooseData("D", mData.getD(), holder.optionView);
            }
        }
        holder.deteleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                models.remove(position);
                notifyDataSetChanged();
                change();
            }
        });
    }

    public void change() {

    }

    /**
     * 初始化选项
     *
     * @param key
     * @param value
     */
    private void setChooseData(String key, String value, LinearLayout layout) {
        View exercisesView = View.inflate(context, R.layout.item_exercises_choose_other_view, null);
        AppCompatCheckBox checkBox = exercisesView.findViewById(R.id.select_checkbox);
        HtmlTextView htmlTextView = exercisesView.findViewById(R.id.answer_content);
        checkBox.setText(key);
        htmlTextView.setHtmlFromString(value, false);
        //学生做题时,看是否已选择选项
        layout.addView(exercisesView);
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

        private HtmlTextView tiGanView;
        private LinearLayout optionView;
        private View deteleView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView.setOnClickListener(this);
            tiGanView = itemView.findViewById(R.id.content);
            optionView = itemView.findViewById(R.id.option_layout);
            deteleView = itemView.findViewById(R.id.teacher_select_exercises_delete_tv);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}
