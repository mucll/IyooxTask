package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.fragment.home.BookFragment;
import com.huisu.iyoox.fragment.home.ClassFragment;
import com.huisu.iyoox.fragment.home.ErrorExercisesFragment;
import com.huisu.iyoox.fragment.home.HomeFragment;
import com.huisu.iyoox.fragment.home.HomeWorkFragment;
import com.huisu.iyoox.fragment.home.MineFragment;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.MyFragmentLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 主界面
 * @date: 18/6/28
 */
public class MainActivity extends BaseActivity {

    private long exitTime;
    private MyFragmentLayout myFragmentLayout;
    private List<Fragment> mFragmentList = null;
    private final int maxTime = 2000;
    private int tabImages[][] = {
            {R.drawable.tab_icon_learning_selected, R.drawable.tab_icon_learning_regular},
            {R.drawable.tab_icon_ctj_selected, R.drawable.tab_icon_ctj_regular},
            {R.drawable.tab_icon_homework_selected, R.drawable.tab_icon_homework_regular},
            {R.drawable.tab_icon_class_selected, R.drawable.tab_icon_class_regular},
            {R.drawable.tab_icon_user_selected, R.drawable.tab_icon_user_regular}};

    @Override
    protected void initView() {
        StatusBarUtil.transparencyBar(this);
        if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
        } else {
            requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);
        }
        myFragmentLayout = (MyFragmentLayout) findViewById(R.id.main_content_layout);
        mFragmentList = new ArrayList();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new ErrorExercisesFragment());
        mFragmentList.add(new HomeWorkFragment());
        mFragmentList.add(new ClassFragment());
        mFragmentList.add(new MineFragment());
        myFragmentLayout.setScorllToNext(false);
        myFragmentLayout.setScorll(true);
        myFragmentLayout.setWhereTab(0);
        myFragmentLayout.setOnChangeFragmentListener(new MyFragmentLayout.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int positon,
                               View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(R.id.tab_text))
                        .setTextColor(getResources().getColor(R.color.color333));
                ((ImageView) lastTabView.findViewById(R.id.tab_img))
                        .setImageResource(tabImages[lastPosition][1]);
                ((TextView) currentTabView.findViewById(R.id.tab_text))
                        .setTextColor(getResources().getColor(R.color.main_text_color));
                ((ImageView) currentTabView.findViewById(R.id.tab_img))
                        .setImageResource(tabImages[positon][0]);
                ((BaseFragment) mFragmentList.get(positon)).onShow();
            }
        });
        myFragmentLayout.setAdapter(mFragmentList, R.layout.tablayout, 0x101);
        myFragmentLayout.getViewPager().setOffscreenPageLimit(5);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setEvent() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (myFragmentLayout.getCurrentPosition()) {
            case 0:
                mFragmentList.get(0).onActivityResult(requestCode, resultCode, data);
                break;
            case 3:
                mFragmentList.get(3).onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    /**
     * 重写系统返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > maxTime) {
                exitTime = System.currentTimeMillis();
                TabToast.makeText(
                        getResources().getString(R.string.exit_destroy_app),
                        this);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
