package com.huisu.iyoox.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.GradeAdapter;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.GradeModel;
import com.huisu.iyoox.entity.SexBean;

import java.util.List;

import static com.huisu.iyoox.views.SelectSexDialog.MAN_CODE;

/**
 * Created by zhaojin on 15/11/16.
 */
public class SelectGradeDialog implements AdapterView.OnItemClickListener {
    private Dialog dialog;
    private int selectGrad = 0;
    private final ListView mListView;
    private final GradeAdapter mAdapter;
    private List<GradeListModel> gradeModels;

    public SelectGradeDialog(Context context, List<GradeListModel> gradeModels, int selectGrade) {
        this.selectGrad = selectGrade;
        this.gradeModels = gradeModels;
        dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.layout_select_grade, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        mListView = (ListView) view.findViewById(R.id.select_grade_list_view);
        mAdapter = new GradeAdapter(context, gradeModels, selectGrad);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        view.findViewById(R.id.register_grade_content_layout).startAnimation(ani);
        dialog.show();
    }


    public void getGradeType(GradeListModel model, int selectGrad) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectGrad = position;
        getGradeType(gradeModels.get(position), selectGrad);
        dialog.dismiss();
    }
}
