package com.huisu.iyoox.util;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.LruCache;
import android.view.Gravity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.huisu.iyoox.activity.base.BaseActivity;

public class UrlImageGetter implements Html.ImageGetter {
    public static LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(5 * 1024 * 1024);
    public Bitmap bitmap;
    Context c;
    TextView container;
    int width;
    ;

    /**
     * @param t
     * @param c
     */
    public UrlImageGetter(TextView t, Context c) {
        this.c = c;
        this.container = t;
        width = c.getResources().getDisplayMetrics().widthPixels;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final UrlDrawable urlDrawable = new UrlDrawable();
        if (cache.get(source) != null) {
            Bitmap caheBitmap = cache.get(source);
            urlDrawable.bitmap = caheBitmap;
            urlDrawable.setBounds(0, 0, (int) (caheBitmap.getWidth()),
                    caheBitmap.getHeight());
            container.invalidate();
            container.setText(container.getText());
            container.setGravity(Gravity.CENTER_VERTICAL);
            UrlImageGetter.this.bitmap = caheBitmap;
            return urlDrawable;
        }
        if(c instanceof Activity&&((Activity) c).isFinishing()){
            return urlDrawable;
        }
        Glide.with(c)
                .load(source)
                //                .asBitmap()//添加了这个之后会导致透明度失效，透明图片变成黑色
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable drawable, String s, Target<GlideDrawable> target,
                                                   boolean b, boolean b1) {
                        if (drawable == null) {
                            return false;
                        }
                        Bitmap resource = drawableToBitmap(drawable);
                        if (resource.getWidth() < BaseActivity.getScreenWidth((
                                (Activity) c)) * 2 / 3
                                && resource
                                .getHeight() > 50) {
                            Matrix matrix = new Matrix();
                            matrix.postScale(2, 2);
                            resource = Bitmap.createBitmap(resource, 0, 0,
                                    resource.getWidth(), resource.getHeight(),
                                    matrix, false);
                        } else if (resource.getHeight() < 50) {
                            Matrix matrix = new Matrix();
                            matrix.postScale(2, 2);
                            resource = Bitmap.createBitmap(resource, 0, 0,
                                    resource.getWidth(), resource.getHeight(),
                                    matrix, false);
                        }
                        if (resource.getWidth() > (BaseActivity.getScreenWidth((
                                (Activity) c)) -BaseActivity.getScreenScale((Activity) c) * 16)) {
                            int maxWidth = (int) (BaseActivity.getScreenWidth((
                                    (Activity) c)) -
                                    BaseActivity
                                            .getScreenScale((Activity) c) * 16);
                            Matrix matrix1 = new Matrix();
                            matrix1.postScale(((float) maxWidth) / resource.getWidth(), (
                                    (float) maxWidth)
                                    / resource
                                    .getWidth());
                            resource = Bitmap.createBitmap(resource, 0, 0,
                                    resource.getWidth(), resource.getHeight(),
                                    matrix1, false);
                        }
                        urlDrawable.bitmap = resource;
                        urlDrawable.setBounds(0, 0, (int) (resource.getWidth()),
                                resource.getHeight());
                        container.invalidate();
                        container.setText(container.getText());
                        container.setGravity(Gravity.CENTER_VERTICAL);
                        UrlImageGetter.this.bitmap = resource;
                        cache.put(source, resource);
                        return false;
                    }
                }).into(ImageLoader.getMaxBitmap(), ImageLoader.getMaxBitmap());
        return urlDrawable;
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}