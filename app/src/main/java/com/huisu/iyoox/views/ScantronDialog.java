package com.huisu.iyoox.views;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ExercisesModel;

import java.util.ArrayList;


/**
 * Created by dl on 2017/3/9.
 * 答题卡dialog
 */
public class ScantronDialog extends Dialog {
    public ScantronDialog(Context context, ArrayList<ExercisesModel> exercisesData ) {
        super(context, R.style.Transparent4);
        ScantronNumberPager pager = new ScantronNumberPager(context, exercisesData) {
            @Override
            public void getPositionClick(int position) {
                getPosition(position);
            }

            @Override
            public void submit() {
                getSubmit();
            }

            @Override
            public void closed() {
                dismiss();
            }
        };
        View view = pager.getView();
        view.setPadding(0, (int) context.getResources().getDimension(R.dimen.title_height) + 1, 0, 0);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void getPosition(int position) {
    }

    public void getSubmit() {
    }
}
