package com.huisu.iyoox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.huisu.iyoox.swipetoloadlayout.SwipeTrigger;

/**
 * Created by mucll on 2017/9/7.
 */

public class LoadHomePageView extends RelativeLayout implements SwipeLoadMoreTrigger, SwipeTrigger {
    private boolean isRelease;
    private Context context;
    private View view;

    public LoadHomePageView(Context context) {
        this(context, null);
    }

    public LoadHomePageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadHomePageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initview();
    }

    private void initview() {
        view = View.inflate(context, R.layout.item_foot_home_page, null);
        addView(view);
    }

    @Override
    public void onLoadMore() {
        //上啦到一定位置之后调用此方法
//        textView.setText("正在加载");
    }

    @Override
    public void onPrepare() {
        //上拉之前调用此方法
        isRelease = false;
    }

    @Override
    public void onMove(int yScroll, boolean isComplete, boolean b1) {
        // 当上拉到一定位置之后 显示刷新
        if (!isComplete) {
            if (yScroll < getHeight()) {
//                textView.setText("上拉加载");
            } else {
//                textView.setText("松开加载更多");
            }
        }
    }

    @Override
    public void onRelease() {
        //上拉到一定距离之后松开刷新
        isRelease = true;
    }

    @Override
    public void onComplete() {
//        textView.setText("加载完成");
    }

    @Override
    public void onReset() {
        //重置
    }
}