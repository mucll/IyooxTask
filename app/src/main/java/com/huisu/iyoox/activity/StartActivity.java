package com.huisu.iyoox.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.register.RegisterPhoneActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.util.SaveDate;
import com.huisu.iyoox.util.StringUtils;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.lang.ref.WeakReference;

/**
 * @author: dl
 * @function: 启动界面
 * @date: 18/6/28
 */
public class StartActivity extends Activity {

    private static class MyHandler extends Handler {
        private final WeakReference<StartActivity> mActivity;

        public MyHandler(StartActivity activity) {
            mActivity = new WeakReference<StartActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StartActivity activity = mActivity.get();
            if (activity != null) {
                if (StringUtils.getCurrentVersion(activity) == SaveDate.getInstence(activity)
                        .getVersion()) {
                    User user = LitePal.findFirst(User.class);
                    if (user != null) {
                        UserManager.getInstance().setUser(user);
                        MainActivity.start(activity);
                    } else {
                        LoginActivity.start(activity);
                    }
                } else {
                    GuideActivity.start(activity);
                }
                activity.finish();
            }
        }
    }

    private final MyHandler handler = new MyHandler(this);

    private ImageView iconBottomIv, iconBoxIv, iconPersonIv, iconBookIv, iconBIv, iconGuangIv,
            iconBookTwoIv, iconErrorIv, iconHatIv, iconPenIv, iconrAIv, iconrRubberIv, iconrPercentageIv;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiddenStatusBar();
        setContentView(R.layout.activity_loading_layout);
        //停留2S
        handler.sendEmptyMessageDelayed(0, 3000);
        initView();
        anim();
    }

    private void anim() {
        height = BaseActivity.getScreenHeigth(this);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(iconBottomIv, "scaleX", 1.5f, 1f);
        scaleXAnimator.setDuration(800);
        scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iconBottomIv, "scaleY", 1.5f, 1f);
        scaleYAnimator.setDuration(800);
        scaleYAnimator.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(iconBottomIv, "translationY", height / 2 - 150);
        translationAnimator.setDuration(500);
        translationAnimator.setRepeatMode(ValueAnimator.RESTART);

        animatorSet.play(scaleXAnimator).with(scaleYAnimator).before(translationAnimator);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //停一会 再开启动画
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAnims();
                    }
                }, 200);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void startAnims() {
        iconBoxIv.setVisibility(View.VISIBLE);
        iconGuangIv.setVisibility(View.VISIBLE);

        iconPersonIv.setVisibility(View.VISIBLE);
        startAnim(iconPersonIv, 1000);
        iconBookIv.setVisibility(View.VISIBLE);
        startAnimBook(iconBookIv, 1000, 150f, -100f, 10f);
        iconBIv.setVisibility(View.VISIBLE);
        startAnimBook(iconBIv, 1000, -50f, -50f, 20f);
        iconHatIv.setVisibility(View.VISIBLE);
        startAnimBook(iconHatIv, 1000, 150f, -350f, 10f);
        iconrAIv.setVisibility(View.VISIBLE);
        startAnimBook(iconrAIv, 1000, -80f, -200f, 30f);

        iconBookTwoIv.setVisibility(View.VISIBLE);
        startAnimBook(iconBookTwoIv, 1000, -150f, -160f, 10f);
        iconErrorIv.setVisibility(View.VISIBLE);
        startAnimBook(iconErrorIv, 1000, -200f, -400f, 10f);
        iconPenIv.setVisibility(View.VISIBLE);
        startAnimBook(iconPenIv, 1000, 120f, -140f, 30f);
        iconrRubberIv.setVisibility(View.VISIBLE);
        startAnimBook(iconrRubberIv, 1000, -250f, -100f, 10f);
        iconrPercentageIv.setVisibility(View.VISIBLE);
        startAnimBook(iconrPercentageIv, 1000, 200f, -250f, 30f);
    }

    private void startAnim(View view, int animTime) {
        AnimatorSet animatorSet = new AnimatorSet();
        float curTranslationX = view.getTranslationY();
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(view, "translationY", curTranslationX, -300f);
        translationAnim.setDuration(300);
        translationAnim.setInterpolator(new DecelerateInterpolator());
        translationAnim.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.7f, 1f);
        scaleXAnimator.setDuration(300);
        scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.7f, 1f);
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator translationAnimY = ObjectAnimator.ofFloat(view, "translationY", -300f, -280f, -300f, -320f, -300f);
        translationAnimY.setDuration(animTime);
        translationAnimY.setRepeatMode(ValueAnimator.RESTART);

        animatorSet.play(translationAnim).with(scaleXAnimator).with(scaleYAnimator).before(translationAnimY);
        animatorSet.start();

    }

    private void startAnimBook(View view, int animTime, float translationX, float translationY, float doudong) {
        AnimatorSet animatorSet = new AnimatorSet();
        float curTranslationY = view.getTranslationY();
        float curTranslationX = view.getTranslationX();
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(view, "translationY", curTranslationY, translationY);
        translationAnim.setDuration(300);
        translationAnim.setInterpolator(new DecelerateInterpolator());
        translationAnim.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(view, "translationX", curTranslationX, translationX);
        translationXAnim.setDuration(300);
        translationAnim.setInterpolator(new DecelerateInterpolator());
        translationXAnim.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        scaleXAnimator.setDuration(animTime);
        scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        scaleYAnimator.setDuration(animTime);
        scaleYAnimator.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.9f, 1f);
        alphaAnimator.setDuration(800);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator translationAnimY = ObjectAnimator.ofFloat(view, "translationY", translationY, translationY + doudong, translationY, translationY - doudong, translationY);
        translationAnimY.setDuration(animTime);
        translationAnimY.setRepeatMode(ValueAnimator.RESTART);

        animatorSet.play(translationAnim).with(translationXAnim).with(alphaAnimator).with(scaleXAnimator).with(scaleYAnimator).before(translationAnimY);
        animatorSet.start();
    }

    private void initView() {
        iconBottomIv = (ImageView) findViewById(R.id.start_icon_bottom_iv);
        iconGuangIv = (ImageView) findViewById(R.id.start_icon_guang_iv);
        iconPersonIv = (ImageView) findViewById(R.id.start_icon_person_iv);
        iconBoxIv = (ImageView) findViewById(R.id.start_icon_box_iv);
        iconBookIv = (ImageView) findViewById(R.id.start_icon_book_iv);
        iconBIv = (ImageView) findViewById(R.id.start_icon_b_iv);
        iconBookTwoIv = (ImageView) findViewById(R.id.start_icon_book_two_iv);
        iconErrorIv = (ImageView) findViewById(R.id.start_icon_error_iv);
        iconHatIv = (ImageView) findViewById(R.id.start_icon_hat_iv);
        iconPenIv = (ImageView) findViewById(R.id.start_pen_iv);
        iconrAIv = (ImageView) findViewById(R.id.start_icon_a_iv);
        iconrRubberIv = (ImageView) findViewById(R.id.start_rubber_iv);
        iconrPercentageIv = (ImageView) findViewById(R.id.start_percentage_iv);
    }

    /**
     * 隐藏状态栏
     */
    public void hiddenStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
