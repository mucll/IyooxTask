package com.huisu.iyoox.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.widget.TextView;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.application.MyApplication;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.util.PermissionUtil;

import java.io.File;

/**
 * Created by zhaojin on 15/11/16.
 */
public class ChangeHeaderImgDialog implements View.OnClickListener {
    public final int REQUST_CAMARE = 0x02;
    public final int REQUST_PHOTOSELECT = 0x03;
    public final int REQUST_CLIP = 0x04;
    File mOutputFile;
    Bitmap bm;
    Activity context;
    ImageView headerIv;
    private Dialog dialog;
    private final TextView camareTv;
    private final TextView select;
    private boolean cutting = false;

    public ChangeHeaderImgDialog(Activity context, ImageView headerIv) {
        this.headerIv = headerIv;
        this.context = context;
        String sdPath = MyApplication.CACHEPATH;
        File file = new File(sdPath);
        if (!file.exists()) {
            file.mkdir();
        }
        mOutputFile = new File(sdPath, System.currentTimeMillis() + ".jpg");
        dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.layout_select_header, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        camareTv = (TextView) view.findViewById(R.id.camare);
        camareTv.setOnClickListener(this);
        select = (TextView) view.findViewById(R.id.select);
        select.setOnClickListener(this);
        dialog.setContentView(view);
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        view.findViewById(R.id.linearLayout).startAnimation(ani);
        dialog.show();
    }


    // 剪切界面
    private void cropBitmap(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 开启剪裁
        intent.putExtra("crop", "true");
        // 宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
        context.startActivityForResult(intent, REQUST_CLIP);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUST_PHOTOSELECT) {
            try {
                Uri uri = data.getData();
                if (uri != null) {
                    cropBitmap(uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUST_CLIP) {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
            cutting = true;
            if (((BaseActivity) context).hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
                bm = BitmapFactory.decodeFile(mOutputFile.getAbsolutePath());
                if (bm != null && bm.getHeight() > 0) {
                    // postFile(imageData.getUri());
                    getResult(mOutputFile);
                }
            } else {
                ((BaseActivity) context).requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);
            }
        } else if (requestCode == REQUST_CAMARE) {
            Uri uri = Uri.fromFile(mOutputFile);
            if (mOutputFile.length() > 0 && uri != null) {
                cropBitmap(Uri.fromFile(mOutputFile));
            }
        }
    }

    // 做头像上传操作，接口暂未定
    public void getResult(File file) {

    }

    public void onSave(Bundle outState) {
        outState.putSerializable("file", mOutputFile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camare:
                if (((BaseActivity) context).hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doOpenCamera();
                } else {
                    ((BaseActivity) context).requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                }
                break;
            case R.id.select:
                cutting = false;
                if (((BaseActivity) context).hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
                    doWriteSDCard();
                } else {
                    ((BaseActivity) context).requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);
                }
                break;
        }
    }

    public void doWriteSDCard() {
        if (!cutting) {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ChangeHeaderImgDialog.this.context.startActivityForResult(i, REQUST_PHOTOSELECT);
            dialog.dismiss();
        } else {
            bm = BitmapFactory.decodeFile(mOutputFile.getAbsolutePath());
            if (bm != null && bm.getHeight() > 0) {
                // postFile(imageData.getUri());
                getResult(mOutputFile);
            }
        }
    }

    public void doOpenCamera() {
        Intent newIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        newIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutputFile));
        ChangeHeaderImgDialog.this.context.startActivityForResult(newIntent, REQUST_CAMARE);
        dialog.dismiss();
    }
}
