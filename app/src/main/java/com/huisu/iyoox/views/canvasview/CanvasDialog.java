package com.huisu.iyoox.views.canvasview;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;

import com.huisu.iyoox.R;

/**
 * ClassName:CanvasDialog
 */
public class CanvasDialog extends Dialog {
    public final MyCanvasView myCanvasView;
    Context context;

    public CanvasDialog(Context context) {
        super(context, R.style.Transparent4);
        this.context = context;
        myCanvasView = new MyCanvasView(context) {
            @Override
            public void back() {
                dismiss();
            }
        };
        setContentView(myCanvasView);
    }

    @Override
    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.show();
    }
}
