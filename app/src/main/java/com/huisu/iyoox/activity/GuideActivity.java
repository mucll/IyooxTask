package com.huisu.iyoox.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.util.SaveDate;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.views.TagViewPager;

import org.litepal.LitePal;

/**
 * @author: dl
 * @function: 引导界面
 * @date: 18/6/28
 */
public class GuideActivity extends Activity {
    private TagViewPager viewPager;
    private int imageIds[] = {R.drawable.index1, R.drawable.index2, R.drawable.index3, R.drawable
            .index4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                    .LayoutParams.FLAG_FULLSCREEN);//
        }
//        if (StringUtils.isPad(this)) {
//            setRequestedOrientation(ActivityInfo
//                    .SCREEN_ORIENTATION_LANDSCAPE);// 横屏
//        } else {
//            setRequestedOrientation(ActivityInfo
//                    .SCREEN_ORIENTATION_PORTRAIT);//竖屏
//        }
        setContentView(R.layout.activity_guide_layout);
        viewPager = findViewById(R.id.tagViewPager);
        viewPager.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 16,
                8, 2, 60);
        viewPager.setAutoNext(false, 0);
        viewPager.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                View v = View.inflate(GuideActivity.this, R.layout.item_guide, null);
                ImageView iv = v.findViewById(R.id.imageView);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                Button btn = v.findViewById(R.id.button);
                iv.setImageResource(imageIds[position]);
                container.addView(v);
                if (position == imageIds.length - 1) {
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SaveDate.getInstence(GuideActivity.this).setVersion(StringUtils.getCurrentVersion(GuideActivity.this));
                            User user = LitePal.findFirst(User.class);
                            if (user != null) {
                                UserManager.getInstance().setUser(user);
                                MainActivity.start(GuideActivity.this);
                            } else {
                                LoginActivity.start(GuideActivity.this);
                            }
                            GuideActivity.this.finish();
                        }
                    });
                } else {
                    btn.setVisibility(View.GONE);
                }
                return v;
            }
        });
        viewPager.setAdapter(imageIds.length, 0);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }
}