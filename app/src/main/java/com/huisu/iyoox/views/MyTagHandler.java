package com.huisu.iyoox.views;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import org.xml.sax.XMLReader;

import java.util.Locale;

public class MyTagHandler implements Html.TagHandler {
    private Context mContext;

    public MyTagHandler(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // 处理标签<img>
        if ("img".equals(tag.toLowerCase(Locale.getDefault()))) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
            String imgURL = images[0].getSource();
            // 使图片可点击并监听点击事件
            output.setSpan(new ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ClickableImage extends ClickableSpan {
        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
//            ArrayList<ImageData> temp = new ArrayList<ImageData>();
//            ImageData data = new ImageData();
//            data.setUrl(url);
//            temp.add(data);
//            PhotosDialog photosDialog = new PhotosDialog(widget.getContext(), false, false, temp,1);
//            photosDialog.showPhotos(0);
        }
    }
}  