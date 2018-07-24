package com.huisu.iyoox.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.views.canvasview.MyCanvasView;

import java.util.ArrayList;

/**
 * ClassName:CanvasDialog
 */
public class GradeDialog extends Dialog {
    Context context;
    private final SelectGradeView mGradeView;

    public GradeDialog(Context context, ArrayList<GradeListModel> gradeListModels, int position) {
        super(context, R.style.Transparent5);
        this.context = context;
        mGradeView = new SelectGradeView(context) {
            @Override
            public void selectChanges(int position) {
                getGradePosition(position);
                dismiss();
            }

            @Override
            public void back() {
                dismiss();
            }
        };
        mGradeView.setData(gradeListModels, position);
        setContentView(mGradeView);
    }

    public void getGradePosition(int position) {

    }

    @Override
    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.show();
    }
}
