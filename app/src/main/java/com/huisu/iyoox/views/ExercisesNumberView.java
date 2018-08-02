package com.huisu.iyoox.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.ExercisesNumberViewAdapter;

/**
 * Function:
 * Date: 2018/7/31
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ExercisesNumberView extends FrameLayout {
    private Context mContext;
    private View view;
    private EbagGridView mEbagGridView;
    private ExercisesNumberViewAdapter mAdapter;
    private TextView exercisesName;

    public ExercisesNumberView(Context context) {
        this(context, null);
    }

    public ExercisesNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExercisesNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_exercises_number_view, this, true);
        mEbagGridView = view.findViewById(R.id.exercises_number_grid_view);
        exercisesName = view.findViewById(R.id.exercises_name_tv);
        mAdapter = new ExercisesNumberViewAdapter(mContext, null);
        mEbagGridView.setAdapter(mAdapter);
        exercisesName.setFocusable(true);
        exercisesName.setFocusableInTouchMode(true);
        exercisesName.requestFocus();
    }
}
