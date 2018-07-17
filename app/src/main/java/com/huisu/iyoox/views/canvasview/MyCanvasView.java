package com.huisu.iyoox.views.canvasview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

import com.huisu.iyoox.R;

public class MyCanvasView extends FrameLayout implements View.OnClickListener {
    private Context context;
    private ImageButton backView;
    private ImageButton deleteView;
    private DrawableView canvasView;
    private View view;

    public MyCanvasView(Context context) {
        this(context, null);
    }

    public MyCanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        view = View.inflate(context, R.layout.layout_my_canvas_view, this);
        canvasView = (DrawableView) view.findViewById(R.id.paintView);
        backView = (ImageButton) view.findViewById(R.id.btn_canvas_view_back);
        deleteView = (ImageButton) view.findViewById(R.id.btn_canvas_view_delete);
        backView.setOnClickListener(this);
        deleteView.setOnClickListener(this);
        canvasView.post(new Runnable() {
            @Override
            public void run() {
                int width = canvasView.getMeasuredWidth();
                int height = canvasView.getMeasuredHeight();
                DrawableViewConfig config = new DrawableViewConfig();
                config.setStrokeColor(getResources().getColor(android.R.color.black));
                config.setStrokeWidth(3.0f);
                //缩放比例都设1.0f就是原图并不让缩放
                config.setMinZoom(1.0f);
                config.setMaxZoom(1.0f);
                config.setShowCanvasBounds(false);
                config.setCanvasHeight(height);
                config.setCanvasWidth(width);
                canvasView.setConfig(config);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_canvas_view_back:
                back();
                break;
            case R.id.btn_canvas_view_delete:
                canvasView.clear();
                invalidate();
                break;
        }
    }

    public void back() {

    }
}
