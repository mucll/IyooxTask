package com.huisu.iyoox.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.manager.ActivityStackManager;
import com.huisu.iyoox.util.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @author: dl
 * @function: 所有Activity的基类，用来处理一些公共事件，如：数据统计
 * @date: 18/6/28
 */
public abstract class BaseActivity extends AppCompatActivity {

    public String TAG;
    protected Context context;
    private static int screenWidth;
    private static int screenHeigth;
    /**
     * px dip比例
     */
    private static float screenScale;
    /**
     * 是否沉浸状态栏
     **/
    private boolean isStatusBar = true;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isScreenRoate = true;
    private FrameLayout contentView;

    /**
     * 初始化界面
     **/
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 绑定事件
     */
    protected abstract void setEvent();

    /**
     * 初始化界面
     **/
    protected abstract int getContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);
        initBaseView();
        LayoutInflater.from(this).inflate(getContentView(), contentView);
        context = this;
        initView();
        setEvent();
        initData();
        initUmeng();
        ActivityStackManager.getActivityStackManager().pushActivity(this);
        if (isStatusBar) {
            steepStatusBar();
        }
        if (!isScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setTitleBarPadding();
    }

    private void setTitleBarPadding() {
        View titleLayout = findViewById(R.id.title_bar_layout);
        if (titleLayout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                titleLayout.setPadding(0, (int) (24 * BaseActivity.getScreenScale(this)), 0, 0);
            }
        }
    }

    private void initBaseView() {
        contentView = findViewById(R.id.base_content_layout);
    }


    /**
     * 初始化友盟统计
     */
    private void initUmeng() {
        TAG = getComponentName().getShortClassName();
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
    }

    /**
     * 退出应用
     * called while exit app.
     */
    public void exitLogic() {
        //remove all activity.
        ActivityStackManager.getActivityStackManager().popAllActivity();
        //system exit.
        System.exit(0);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        TextView tv = (TextView) findViewById(R.id.title_bar_tv);
        if (tv != null && !TextUtils.isEmpty(title)) {
            tv.setText(title);
        }
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param statusBar
     */
    public void setStatusBar(boolean statusBar) {
        this.isStatusBar = statusBar;
    }


    /**
     * [是否设置旋转屏幕]
     *
     * @param screenRoate
     */
    public void setScreenRoate(boolean screenRoate) {
        this.isScreenRoate = screenRoate;
    }

    /**
     * tab返回
     */
    public void setBack() {
        try {
            ImageButton backBtn = (ImageButton) findViewById(R.id.menu_back);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTransBack() {
        try {
            ImageButton backBtn = (ImageButton) findViewById(R.id.trans_menu_back);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getActivityStackManager().popActivity(this);
    }

    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        ActivityCompat.requestPermissions(this, permissions, code);
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(this, permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 处理整个应用用中的SDCard业务
     */
    public void doSDCardPermission() {
    }

    /**
     * 隐藏状态栏
     */
    public void hiddenStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 改变状态栏颜色
     *
     * @param color
     */
    public void changeStatusBarColor(int color) {
        StatusBarUtil.setStatusBarColor(this, color);
    }

    /**
     * 调整状态栏为亮模式，这样状态栏的文字颜色就为深模式了。
     */
    private void steepStatusBar() {
        StatusBarUtil.setStatusBarTranslucent(this, true);
    }


    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getScreenHeigth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        screenHeigth = dm.heightPixels;
        return screenHeigth;
    }

    public static float getScreenScale(Activity context) {
        if (screenScale <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
            screenScale = dm.scaledDensity;
        }
        return screenScale;
    }

}
