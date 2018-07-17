package com.huisu.iyoox.views;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.File;

public class ShareDialog {
//                ShareDialog.show(getContext(), "测试分享", "分享内容", "", "http://www.baidu.com/"
//            , R.drawable.ic_launcher);
    /**
     * 分享后不需要任何跳转的
     *
     * @param context
     * @param title
     * @param content
     * @param imageUrl
     * @param shareUrl
     * @param imageId
     */
    public static void show(final Context context, String title, final String content, String imageUrl, final String
            shareUrl, int imageId) {
        UMImage umImage;
        if (imageUrl.startsWith("http")) {
            umImage = new UMImage(context, imageUrl);
        } else if (!TextUtils.isEmpty(imageUrl)) {
            umImage = new UMImage(context, new File(imageUrl));
        } else {
            umImage = new UMImage(context, imageId);
        }
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);//标题
        web.setThumb(umImage);  //缩略图
        web.setDescription(content);//描述
        final ShareAction sa = new ShareAction(((Activity) context));
        sa.withMedia(web);
        sa.setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (share_media == null) {
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context
                                    .CLIPBOARD_SERVICE);
                            cm.setText(shareUrl);
                            Toast.makeText(context, "复制成功", Toast.LENGTH_LONG).show();
                        } else {
                            sa.setPlatform(share_media).setCallback(new UMShareListener() {
                                @Override
                                public void onStart(SHARE_MEDIA share_media) {

                                }

                                @Override
                                public void onResult(SHARE_MEDIA share_media) {
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    if (throwable.getMessage().contains("2008")) {
                                        Toast.makeText(context, "没有安装此应用", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {
                                }
                            }).share();
                        }
                    }
                });
        sa.open();
    }
}
