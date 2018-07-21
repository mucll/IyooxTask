package com.huisu.iyoox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;

/**
 * @ClassName: ErrorView
 * @Description: 用于没有数据或加载错误时显示
 */
public class ExercisesErrorEmptyView extends LinearLayout implements View.OnClickListener {

    private TextView error_tv;

    public ExercisesErrorEmptyView(Context context) {
        super(context);
        initView(context, null);
    }

    public ExercisesErrorEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet object) {
        View view = LayoutInflater.from(context).inflate(R.layout.exercises_error_emptyview_layout, this);
        error_tv = (TextView) view.findViewById(R.id.item_exercises_error_tv);
        error_tv.setOnClickListener(this);
    }

    /**
     * @param msg
     * @Title: setErrorMsg
     * @Description: 设置错误提示语
     * @return: void
     */
    public void setErrorMsg(String msg) {
        error_tv.setText(msg);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEmptyClick(v);
        }
    }

    public void setOnEmptyClick(onEmptyClickListener listener) {
        this.listener = listener;
    }

    private onEmptyClickListener listener;

    public interface onEmptyClickListener {

        void onEmptyClick(View v);
    }

}
