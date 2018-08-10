package com.huisu.iyoox.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Function:
 * Date: 2018/8/10
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
