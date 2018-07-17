package com.huisu.iyoox.views;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import com.huisu.iyoox.util.LocalLinkMovementMethod;
import com.huisu.iyoox.util.UrlImageGetter;

import java.io.InputStream;

public class HtmlTextView extends android.support.v7.widget.AppCompatTextView {
    public static final String TAG = "HtmlTextView";
    public static final boolean DEBUG = false;
    public boolean linkHit;
    public Html.ImageGetter imgGetter;
    public String text = "";
    boolean dontConsumeNonUrlClicks = true;

    public HtmlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        imgGetter = new UrlImageGetter(this, getContext());
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imgGetter = new UrlImageGetter(this, getContext());
    }

    public HtmlTextView(Context context) {
        super(context);
        imgGetter = new UrlImageGetter(this, getContext());
    }

    /**
     * @param is
     * @return
     */
    static private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        boolean res = super.onTouchEvent(event);
        if (dontConsumeNonUrlClicks)
            return linkHit;
        return res;
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays
     * it in this TextView.
     *
     * @param html String containing HTML, for example: "<b>Hello world!</b>"
     */
    public void setHtmlFromString(String html, boolean useLocalDrawables) {
        // this uses Android's Html class for basic parsing, and HtmlTagHandler
        text = html.replaceAll("<img", " <img").replaceAll("&nbsp;", "   ")
                .replaceAll("<u>( +)</u>", "___________");
        setText(Html.fromHtml(text, imgGetter, new MyTagHandler(getContext())));
        // make links work
        setGravity(Gravity.CLIP_VERTICAL);
        setBackgroundColor(Color.WHITE);
    }

    /**
     * 设置图片可点击放大
     */
    public void setImageClick() {
        setMovementMethod(LocalLinkMovementMethod.getInstance());
    }
}