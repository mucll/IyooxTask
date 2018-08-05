package com.huisu.iyoox.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherLookTaskStudentSelectDetailActivity;
import com.huisu.iyoox.adapter.ExercisesNumberViewAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TaskTeacherLookTimuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Date: 2018/7/31
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ExercisesNumberView extends FrameLayout implements AdapterView.OnItemClickListener {
    private Context mContext;
    private View view;
    private EbagGridView mEbagGridView;
    private ExercisesNumberViewAdapter mAdapter;
    private TextView exercisesName;
    private ArrayList<TaskTeacherLookTimuModel> models = new ArrayList<>();
    private int type;
    private int workId;
    private String title;

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
        mEbagGridView.setOnItemClickListener(this);
    }

    public void setData(List<TaskTeacherLookTimuModel> data, int workId, String title, int type) {
        this.workId = workId;
        this.type = type;
        this.title = title;
        models.clear();
        models.addAll(data);
        mAdapter = new ExercisesNumberViewAdapter(mContext, models, type);
        mEbagGridView.setAdapter(mAdapter);
        exercisesName.setFocusable(true);
        exercisesName.setFocusableInTouchMode(true);
        exercisesName.requestFocus();
    }

    public void setData(List<TaskTeacherLookTimuModel> data, int type) {
        this.type = type;
        models.clear();
        models.addAll(data);
        mAdapter = new ExercisesNumberViewAdapter(mContext, models, type);
        mEbagGridView.setAdapter(mAdapter);
        exercisesName.setFocusable(true);
        exercisesName.setFocusableInTouchMode(true);
        exercisesName.requestFocus();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (type) {
            case Constant.NUMBER_RATE:
                TeacherLookTaskStudentSelectDetailActivity.start(mContext, workId, title, position);
                break;
        }
    }
}
