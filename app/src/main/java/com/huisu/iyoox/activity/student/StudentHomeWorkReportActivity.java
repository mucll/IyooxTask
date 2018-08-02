package com.huisu.iyoox.activity.student;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.AnswerResultNumberAdapter;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.views.EbagGridView;

import java.util.ArrayList;

public class StudentHomeWorkReportActivity extends BaseActivity {

    private TextView taskNameTv;
    private TextView startTimeTv;
    private TextView endTimeTv;
    private TextView subjectNameTv;
    private TextView timuCountTv;
    private TextView rateTv;
    private TextView correctTv;
    private TextView classCorrectTv;
    private EbagGridView mGridView;
    private AnswerResultNumberAdapter mNumberAdapter;

    private ArrayList<ExercisesModel> exercisesModels = new ArrayList<>();

    @Override
    protected void initView() {
        taskNameTv = findViewById(R.id.task_name_tv);
        startTimeTv = findViewById(R.id.start_time_tv);
        endTimeTv = findViewById(R.id.end_time_tv);
        subjectNameTv = findViewById(R.id.stubject_name_tv);
        timuCountTv = findViewById(R.id.timu_count_tv);
        rateTv = findViewById(R.id.task_rate_tv);
        correctTv = findViewById(R.id.task_correct_tv);
        classCorrectTv = findViewById(R.id.task_class_correct_tv);
        mGridView = findViewById(R.id.item_task_result_grid_view);
        mNumberAdapter = new AnswerResultNumberAdapter(context, exercisesModels);
        mGridView.setAdapter(mNumberAdapter);
    }

    @Override
    protected void initData() {
        setTitle("教师点评");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_home_work_report;
    }

}
