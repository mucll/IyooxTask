package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.fragment.home.ClassFragment;
import com.huisu.iyoox.fragment.home.ErrorExercisesFragment;
import com.huisu.iyoox.fragment.home.HomeFragment;
import com.huisu.iyoox.fragment.home.HomeWorkFragment;
import com.huisu.iyoox.fragment.home.MineFragment;
import com.huisu.iyoox.fragment.teacher.TeacherClassFragment;
import com.huisu.iyoox.fragment.teacher.TeacherRemarkFragment;
import com.huisu.iyoox.fragment.teacher.TeacherCreateTaskFragment;
import com.huisu.iyoox.fragment.teacher.TeacherMineFragment;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.MyFragmentLayout;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

/**
 * @author: dl
 * @function: 主界面
 * @date: 18/6/28
 */
public class MainActivity extends BaseActivity {

    private long exitTime;
    public MyFragmentLayout myFragmentLayout;
    private List<Fragment> mFragmentList = null;
    private final int maxTime = 2000;
    private int tabImages[][] = null;

    @Override
    protected void initView() {
        if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
        } else {
            requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);
        }
        User user = UserManager.getInstance().getUser();
        mFragmentList = new ArrayList();
        //区分 老师 和 学生
        if (Constant.TEACHER_TYPE == user.getType()) {
            //老师
            tabImages = new int[][]{
                    {R.drawable.tab_icon_class_selected, R.drawable.tab_icon_class_regular},
                    {R.drawable.tab_icon_homework_selected_tea, R.drawable.tab_icon_homework_regular_tea},
                    {R.drawable.tab_icon_score_selected_tea, R.drawable.tab_icon_score_regular_tea},
                    {R.drawable.tab_icon_user_selected_tea, R.drawable.tab_icon_user_regular_tea}};
            mFragmentList.add(new TeacherClassFragment());
            mFragmentList.add(new TeacherCreateTaskFragment());
            mFragmentList.add(new TeacherRemarkFragment());
            mFragmentList.add(new TeacherMineFragment());
        } else {
            //学生
            tabImages = new int[][]{
                    {R.drawable.tab_icon_learning_selected, R.drawable.tab_icon_learning_regular},
                    {R.drawable.tab_icon_ctj_selected, R.drawable.tab_icon_ctj_regular},
                    {R.drawable.tab_icon_homework_selected, R.drawable.tab_icon_homework_regular},
                    {R.drawable.tab_icon_class_selected, R.drawable.tab_icon_class_regular},
                    {R.drawable.tab_icon_user_selected, R.drawable.tab_icon_user_regular}};
            mFragmentList.add(new HomeFragment());
            mFragmentList.add(new ErrorExercisesFragment());
            mFragmentList.add(new HomeWorkFragment());
            mFragmentList.add(new ClassFragment());
            mFragmentList.add(new MineFragment());
        }
        myFragmentLayout = findViewById(R.id.main_content_layout);
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
        if (Constant.TEACHER_TYPE == user.getType()) {
            //老师
            myFragmentLayout.setAdapter(mFragmentList, R.layout.tablayout_teacher, 0x101);
        } else {
            //学生
            myFragmentLayout.setAdapter(mFragmentList, R.layout.tablayout_student, 0x101);
        }
        myFragmentLayout.getViewPager().setOffscreenPageLimit(mFragmentList.size());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setEvent() {
        //横屏全屏的实现
        //横向
//        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        //纵向
//        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragmentList.get(myFragmentLayout.getCurrentPosition()).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 重写系统返回键
     *
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
//    @Override
//    public void onBackPressed() {
//        if (JZVideoPlayer.backPress()) {
//            return;
//        }
//        if (System.currentTimeMillis() - exitTime > maxTime) {
//            exitTime = System.currentTimeMillis();
//            TabToast.makeText(
//                    getResources().getString(R.string.exit_destroy_app),
//                    this);
//        } else {
//            finish();
//        }
//        super.onBackPressed();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        JZVideoPlayer.releaseAllVideos();
//    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
