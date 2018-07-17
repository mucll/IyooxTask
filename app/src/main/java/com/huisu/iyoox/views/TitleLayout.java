package com.huisu.iyoox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import com.huisu.iyoox.R;

import butterknife.ButterKnife;

/**
 * @author: dl
 * @function: 所有Activity的基类，用来处理一些公共事件，如：数据统计
 * @date: 18/6/28
 */
public class TitleLayout extends LinearLayout {
    //    public TextView backText;
//    @Bind(R.id.back_text)
//    public TextView titleTv;
//    @Bind(R.id.title_tv)
//    ImageButton backBtn;
//    @Bind(R.id.back_btn)
//    @Bind(R.id.textBtn)
//    public TextView textBtn;
//    @Bind(R.id.image_btn)
//    public ImageButton imageBtn;
//    @Bind(R.id.textBtn_left)
//    public TextView textBtnLeft;
    Context context;

    public TitleLayout(Context context) {
        this(context, null);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View.inflate(context, R.layout.layout_title_bar_all, this);
        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
//        titleTv.setText(title);
    }

    /**
     * @param canBank
     * @param listener 可以为空
     */
    public void setBack(boolean canBank, OnClickListener listener) {
//        if (canBank) {
//            backBtn.setVisibility(VISIBLE);
//            if (listener == null) {
//                listener = new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ((Activity) context).finish();
//                    }
//                };
//            }
//            backBtn.setOnClickListener(listener);
//        } else {
//            backBtn.setVisibility(GONE);
//            canBank = false;
//        }
    }

    /**
     * 右侧文字按钮
     *
     * @param listener
     */
    public void setTextBtnListener(String text, OnClickListener listener) {
//        textBtn.setOnClickListener(listener);
//        textBtn.setText(text);
//        textBtn.setVisibility(VISIBLE);
    }
}
