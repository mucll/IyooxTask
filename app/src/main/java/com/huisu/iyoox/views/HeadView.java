package com.huisu.iyoox.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.huisu.iyoox.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ClassName:HeadView
 * Function:用户头像类
 * Date: 2016/9/15
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class HeadView extends FrameLayout {
    @Bind(R.id.head)
    public ImageView head;
    @Bind(R.id.name)
    public TextView name;
    Context context;
    private GlideListener listener;
    private String userNo, nickName, headUrl;

    public HeadView(Context context) {
        this(context, null);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View v = View.inflate(context, R.layout.layout_head_view, null);
        this.addView(v);
        ButterKnife.bind(this, v);
    }

    public void setTextSize(int size) {
        name.setTextSize(size);
    }

    public void setImageResource(int id) {
        name.setVisibility(GONE);
        head.setImageResource(id);
        Glide.with(context)
                .load(id)
                .asBitmap()
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        head.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * @param groupId
     * @param groupName
     * @param url
     */
    public void setGroupHead(String groupId, String groupName, String url) {
        name.setVisibility(GONE);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy
                        (DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (listener != null) {
                            listener.onSucces(resource);
                        }
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        head.setImageDrawable(circularBitmapDrawable);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }

    /**
     * 设置用户
     *
     * @param userId
     * @param nickName
     * @param headUrl
     */
    public void setHead(final String userId, String nickName, String headUrl) {
        userNo = userId;
        this.nickName = nickName;
        this.headUrl = headUrl;
        name.setVisibility(VISIBLE);
        name.setTag(userId);
        head.setImageResource(getResId());
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        Glide.with(context)
                .load(headUrl)
                .asBitmap()
                .placeholder(getResId())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (listener != null) {
                            listener.onSucces(resource);
                        }
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        if (name.getTag().toString().equals(userId)) {
                            head.setImageDrawable(circularBitmapDrawable);
                            name.setVisibility(GONE);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        try {
                            if (name.getTag().toString().equals(userId)) {
                                name.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }

    private int getResId() {
        return R.drawable.student_default;
    }


    /**
     * 获取昵称最后两位
     *
     * @param nick
     * @return
     */
    private String getEndNick(String nick) {
        if (TextUtils.isEmpty(nick)) {
            return "";
        }
        if (nick.length() >= 2) {
            return nick.substring(nick.length() - 2, nick.length());
        } else {
            return nick;
        }
    }

    public void setGlideListener(GlideListener listener) {
        this.listener = listener;
    }

    /**
     * 设置个人头像点击跳转到个人名片
     */
    public void setHeadClick() {
        head.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                PersonalCardActivity.start(context, userNo, nickName, headUrl);
            }
        });
    }

    public static interface GlideListener {
        public void onSucces(Bitmap resource);

        public void onError();
    }
}
