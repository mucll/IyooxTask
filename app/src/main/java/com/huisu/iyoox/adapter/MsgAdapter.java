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
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ExercisesModel> models;

    public MsgAdapter(Context context, ArrayList<ExercisesModel> models) {
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
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private HtmlTextView tiGanView;
        private LinearLayout optionView;

        public ViewHolder(View itemView) {
            super(itemView);
            tiGanView = itemView.findViewById(R.id.content);
            optionView = itemView.findViewById(R.id.option_layout);
        }
    }
}
