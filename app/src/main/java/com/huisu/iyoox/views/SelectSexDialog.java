package com.huisu.iyoox.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.application.MyApplication;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.SexBean;
import com.huisu.iyoox.fragment.base.BaseFragment;

import java.io.File;

/**
 * Created by zhaojin on 15/11/16.
 */
public class SelectSexDialog implements View.OnClickListener {
    private Dialog dialog;
    private final TextView manTv, womanTv;
    public static final String MAN_CODE = "1";
    public static final String WOMAN_CODE = "0";
    private SexBean manData, womanData;
    private String sexCode;

    public SelectSexDialog(Activity context, String sexCode) {
        this.sexCode = sexCode;
        dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.layout_select_sex, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        manTv = (TextView) view.findViewById(R.id.select_man_tv);
        womanTv = (TextView) view.findViewById(R.id.select_woman_tv);
        manTv.setOnClickListener(this);
        womanTv.setOnClickListener(this);
        dialog.setContentView(view);
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        view.findViewById(R.id.register_sex_content_layout).startAnimation(ani);
        dialog.show();
        initData();
    }

    private void initData() {
        if (sexCode.equals(MAN_CODE)) {
            manData = new SexBean(MAN_CODE, true);
            womanData = new SexBean(WOMAN_CODE, false);
            manTv.setSelected(true);
            womanTv.setSelected(false);
        } else {
            manData = new SexBean(MAN_CODE, false);
            womanData = new SexBean(WOMAN_CODE, true);
            manTv.setSelected(false);
            womanTv.setSelected(true);
        }
    }


    public void getSexString(String sexString, String sexCode) {

    }

    public String getSexCode() {
        return sexCode;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_man_tv:
                sexCode = MAN_CODE;
                getSexString("男生", sexCode);
                dialog.dismiss();
                break;
            case R.id.select_woman_tv:
                sexCode = WOMAN_CODE;
                getSexString("女生", sexCode);
                dialog.dismiss();
                break;
        }
    }
}
