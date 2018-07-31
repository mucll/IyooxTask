package com.huisu.iyoox.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherCreateClassActivity;
import com.huisu.iyoox.constant.Constant;

/**
 * ClassName:ResMorePopWindow
 * Function:
 * Date: 2017/1/5
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class ResMorePopWindow extends Dialog implements View.OnClickListener {
    Context context;


    /**
     * @param context 上下文
     */
    public ResMorePopWindow(Context context) {
        super(context, R.style.Transparent2);
        this.context = context;

        //dialog布局界面
        View contentView = View.inflate(context, R.layout.layout_respreview_pop, null);
        LinearLayout lin = contentView.findViewById(R.id.lin_content);
        View createView = contentView.findViewById(R.id.dialog_create_class_ll);
        View addView = contentView.findViewById(R.id.dialog_add_class_ll);
        setContentView(contentView);
        //dialog动画
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.fade_scale);
        ani.setInterpolator(new DecelerateInterpolator());
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 添加动画
        lin.startAnimation(ani);
        createView.setOnClickListener(this);
        addView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_create_class_ll:
                TeacherCreateClassActivity.start(context, Constant.CLASS_CREATE);
                dismiss();
                break;
            case R.id.dialog_add_class_ll:
                TeacherCreateClassActivity.start(context, Constant.CLASS_ADD);
                dismiss();
                break;
        }
    }
}
