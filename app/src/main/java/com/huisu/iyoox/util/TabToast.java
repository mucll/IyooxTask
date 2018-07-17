package com.huisu.iyoox.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.huisu.iyoox.R;


/**
 * Created by 垚垚
 * on 15/7/23.
 * Email: www.fangmu@qq.com
 * Phone：18661201018
 * Purpose: 优化提示框
 */

public class TabToast {

    /**
     * Toast字体大小
     */
    static float DEFAULT_TEXT_SIZE = 14;
    /**
     * Toast字体颜色
     */
    static int DEFAULT_TEXT_COLOR = 0xffffffff;
    /**
     * Toast背景颜色
     */
    static int DEFAULT_BG_COLOR = 0xE6f5695a;
    /**
     * Toast的高度(单位dp)
     */
    static final float DEFAULT_TOAST_HEIGHT = 50.0f;

    static Context mContext;
    volatile static TabToast mInstance;
    static Toast mToast;
    static View layout;
    static TextView tv;

    public TabToast(Context context) {
        mContext = context;
    }

    /**
     * 单例模式
     *
     * @param context 传入的上下文
     * @return TabToast实例
     */
    private static TabToast getInstance(Context context) {
        if (mInstance == null) {
            synchronized (TabToast.class) {
                if (mInstance == null) {
                    mInstance = new TabToast(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * @param duration Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    private static void getToast(int duration) {
        if (mToast == null) {
            mToast = new Toast(mContext);
            mToast.setGravity(Gravity.TOP, 0, 0);
            mToast.setDuration(duration == Toast.LENGTH_LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        }
    }

    public static int makeText(String text, Context context) {
        makeText(context, text, Toast.LENGTH_SHORT);
        return 0;
    }

    /**
     * 页面顶部显示toast
     *
     * @param text
     * @param context
     */
    public static void showTopToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 180);
        toast.show();
    }

    /**
     * 页面顶部显示toast
     *
     * @param text
     * @param context
     */
    public static void showMiddleToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 180);
        toast.show();
    }


    public static void makeTextRunUi(final String text, final Activity context) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText(context, text, Toast.LENGTH_LONG);
            }
        });
    }

    public static void makeText(Context context, String text, int duration) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        /*getInstance(context);
        getToast(duration);
        if (mInstance.layout == null || mInstance.tv == null) {
            LinearLayout container = new LinearLayout(mContext);
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.setLayoutParams(rootParams);
            container.setBackgroundColor(DEFAULT_BG_COLOR);
            container.setGravity(Gravity.CENTER);

            mInstance.tv = new TextView(mContext);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    getScreenWidth(mContext), dp2px(DEFAULT_TOAST_HEIGHT));
            mInstance.tv.setLayoutParams(tvParams);
            mInstance.tv.setPadding(dp2px(5), dp2px(2), dp2px(5), dp2px(2));
            mInstance.tv.setGravity(Gravity.CENTER);
            mInstance.tv.setTextColor(DEFAULT_TEXT_COLOR);
            mInstance.tv.setMaxLines(2);
            mInstance.tv.setEllipsize(TextUtils.TruncateAt.END);
            mInstance.tv.setBackgroundColor(DEFAULT_BG_COLOR);
            mInstance.tv.setTextSize(DEFAULT_TEXT_SIZE);

            container.addView(mInstance.tv);

            mInstance.layout = container;

            mToast.setView(mInstance.layout);
        }
        mInstance.tv.setText(text);
        mToast.show();*/
    }


    /**
     * 获得屏幕宽度
     *
     * @param context Context
     * @return px
     */
    public static int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        float density = outMetrics.density;
        return (int) (outMetrics.widthPixels * density);
    }

    /**
     * 顶部toast
     *
     * @param context
     * @param text
     */
    public static void showTopMsg(Context context, String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast_msg, null);
        TextView tv = (TextView) view.findViewById(R.id.top_show_msg_tv);
        if (!TextUtils.isEmpty(text))
            tv.setText(text); //toast内容
        Toast toast = new Toast(context);
        int top = (int) context.getResources().getDimension(R.dimen.title_height);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, top);
        toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

/**
 *  LayoutInflater inflater = LayoutInflater.from(上下文);
 *View toast_view = inflater.inflate(布局,null);
 *Toast toast = new Toast(上下文);
 *toast.setView(toast_view);
 *toast.show();
 */
}
