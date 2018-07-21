package com.huisu.iyoox.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.huisu.iyoox.R;

import java.io.File;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * ClassName:ImageLoader
 * Function:图片加载工具类
 * Date: 2016/8/25
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class ImageLoader {
    public static android.support.v8.renderscript.RenderScript rs;

    /**
     * 普通加载图片
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void load(Context context, ImageView iv, String url) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
    }

    /**
     * 普通加载图片,设置错误图片
     *
     * @param context
     * @param iv
     * @param url
     * @param errorRes 加载错误后的图片
     */
    public static void load(Context context, ImageView iv, String url, int errorRes) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.icon_image_loading)
                .error(errorRes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(iv);
    }

    /**
     * 加载圆形图标,设置错误图片
     *
     * @param context
     * @param iv
     * @param url
     * @param errorRes     加载错误后的图片
     * @param loadingResId 图片加载中的占位符
     */
    public static void load(final Context context, final ImageView iv, String url, int
            errorRes, int loadingResId) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy
                        (DiskCacheStrategy.ALL)
                .placeholder(errorRes)
                .dontAnimate()
                .error(errorRes)
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 加载原型图片，通过ID
     *
     * @param context
     * @param resId
     * @param iv
     */
    public static void loadCircle(final Context context, int resId, final ImageView iv) {
        Glide.with(context)
                .load(resId)
                .asBitmap()
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 加载原型图片
     *
     * @param context
     * @param iv
     */
    public static void loadCircleFromFile(final Context context, File file, final ImageView
            iv) {
        Glide.with(context)
                .load(file)
                .asBitmap()
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 加载file图片
     *
     * @param context
     * @param iv
     */
    public static void loadFromFile(final Context context, File file, final ImageView
            iv) {
        Glide.with(context)
                .load(file)
                .diskCacheStrategy
                        (DiskCacheStrategy.NONE)
                .into(iv);
    }

    /**
     * 从本地文件加载缩略图
     *
     * @param context
     * @param file
     * @param iv
     * @param thum
     */
    public static void loadFromFile(final Context context, File file, final ImageView
            iv, float thum) {
        Glide.with(context)
                .load(file)
                .thumbnail(thum)
                .diskCacheStrategy
                        (DiskCacheStrategy.NONE)
                .into(iv);
    }

    /**
     * @param bitmap
     * @param context
     * @return 模糊图片
     */
    public static Bitmap blurBitmap(Bitmap bitmap, Context context) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap
                .Config.ARGB_8888);
        //Instantiate a new Renderscript
        if (rs == null) {
            rs = RenderScript.create(context);
        }
        //Create an Intrinsic Blur Script using the Renderscript
        android.support.v8.renderscript.ScriptIntrinsicBlur blurScript = android.support.v8
                .renderscript.ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        android.support.v8.renderscript.Allocation allIn = android.support.v8.renderscript
                .Allocation.createFromBitmap(rs, bitmap);
        android.support.v8.renderscript.Allocation allOut = android.support.v8.renderscript
                .Allocation.createFromBitmap(rs, outBitmap);
        //Set the radius of the blur
        blurScript.setRadius(25.f);
        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        //recycle the original bitmap
        bitmap.recycle();
        //After finishing everything, we destroy the Renderscript.
        //        rs.finish();
        //        rs.destroy();
        return outBitmap;
    }

    public static void loadUrlAsBitmap(String url, final ImageView iv, Context context) {
        Glide.with(context)
                .load(url.replace("\\", "/"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                //                .error(R.drawable.icon_image_loading)
                .override(720, ImageLoader.getMaxBitmap())
                .into(iv);
    }

    public static void loadUrlAsBitmap(File file, final ImageView iv, Context context) {
        Glide.with(context)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(720, ImageLoader.getMaxBitmap())
                .into(iv);
    }

    /**
     * @return 获取android设备支持的图片最大值
     */
    public static int getMaxBitmap() {
        int max;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            max = getGLESTextureLimitEqualAboveLollipop();
        } else {
            max = getGLESTextureLimitBelowLollipop();
        }
        if (max > 4200) {
            return 4200;
        } else if (max <= 0) {
            return 3000;
        }
        return max;
    }

    private static int getGLESTextureLimitBelowLollipop() {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private static int getGLESTextureLimitEqualAboveLollipop() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);
        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {// TROUBLE! No config found.
        }
        EGLConfig config = configs[0];
        int[] surfAttr = {
                EGL10.EGL_WIDTH, 64,
                EGL10.EGL_HEIGHT, 64,
                EGL10.EGL_NONE
        };
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;  // missing in EGL10
        int[] ctxAttrib = {
                EGL_CONTEXT_CLIENT_VERSION, 1,
                EGL10.EGL_NONE
        };
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }
}
