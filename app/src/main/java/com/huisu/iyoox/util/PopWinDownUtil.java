package com.huisu.iyoox.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.huisu.iyoox.R;

/**
 * Function:
 * Date: 2018/7/27
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class PopWinDownUtil {
    private Context context;
    private View contentView;
    private View relayView;
    private PopupWindow popupWindow;

    public PopWinDownUtil(Context context, View contentView, View relayView) {
        this.context = context;
        this.contentView = contentView;
        this.relayView = relayView;
        init();
    }

    public void init() {
        //内容，高度，宽度
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //动画效果
//        popupWindow.setAnimationStyle(R.style.AnimationTopFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(true);
        //关闭事件
//        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissLisener != null) {
                    onDismissLisener.onDismiss();
                }
            }
        });
    }

    public void show() {
        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(relayView);
        } else {
            int[] location = new int[2];
            relayView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            popupWindow.showAtLocation(relayView, Gravity.NO_GRAVITY, 0, y + relayView.getHeight());
        }
    }

    public void hide() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private OnDismissLisener onDismissLisener;

    public void setOnDismissListener(OnDismissLisener onDismissLisener) {
        this.onDismissLisener = onDismissLisener;
    }

    public interface OnDismissLisener {
        void onDismiss();
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }
}

