package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.HomeWorkResultNumberAdapter;
import com.huisu.iyoox.entity.ExercisesResultModel;
import com.huisu.iyoox.entity.HomeWorkResultModel;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.views.EbagGridView;

import java.util.ArrayList;

/**
 * 学生家庭作业报告
 */
public class StudentHomeWorkReportActivity extends BaseActivity {

    private TextView taskNameTv;
    private TextView startTimeTv;
    private TextView endTimeTv;
    private TextView subjectNameTv;
    private TextView timuCountTv;
    private TextView rateTv;
    private TextView correctTv;
    private TextView classCorrectTv;
    private TextView yijianTv;
    private View yijianView;
    private EbagGridView mGridView;
    private HomeWorkResultNumberAdapter mNumberAdapter;

    private ArrayList<ExercisesResultModel> exercisesModels = new ArrayList<>();
    private HomeWorkResultModel resultModel;
    private ScrollView scrollView;

    @Override
    protected void initView() {
        scrollView = findViewById(R.id.home_work_report_sv);
        taskNameTv = findViewById(R.id.task_name_tv);
        startTimeTv = findViewById(R.id.start_time_tv);
        endTimeTv = findViewById(R.id.end_time_tv);
        subjectNameTv = findViewById(R.id.stubject_name_tv);
        timuCountTv = findViewById(R.id.timu_count_tv);
        rateTv = findViewById(R.id.task_rate_tv);
        correctTv = findViewById(R.id.task_correct_tv);
        classCorrectTv = findViewById(R.id.task_class_correct_tv);
        yijianTv = findViewById(R.id.task_result_jianyi);
        yijianView = findViewById(R.id.task_result_jianyi_ll);
        mGridView = findViewById(R.id.item_task_result_grid_view);
        mNumberAdapter = new HomeWorkResultNumberAdapter(context, exercisesModels);
        mGridView.setAdapter(mNumberAdapter);
        //设置GridView不可点击
        mGridView.setClickable(false);
        mGridView.setPressed(false);
        mGridView.setEnabled(false);
    }

    @Override
    protected void initData() {
        setTitle("教师点评");
        resultModel = (HomeWorkResultModel) getIntent().getSerializableExtra("model");
        exercisesModels.clear();
        if (resultModel != null) {
            taskNameTv.setText(TextUtils.isEmpty(resultModel.getWork_name()) ? "" : resultModel.getWork_name());
            subjectNameTv.setText(TextUtils.isEmpty(resultModel.getZhishidian_name()) ? "" : resultModel.getZhishidian_name());
            timuCountTv.setText(resultModel.getTimu_count() + "题");
            startTimeTv.setText(StringUtils.getTimeString(resultModel.getStart_time()));
            endTimeTv.setText(StringUtils.getTimeString(resultModel.getEnd_time()));
            rateTv.setText("已完成");
            correctTv.setText(resultModel.getZhengquelv() + "%");
            classCorrectTv.setText(resultModel.getZhengquelv_avg() + "%");
            if (TextUtils.isEmpty(resultModel.getDianping())) {
                yijianView.setVisibility(View.GONE);
            } else {
                yijianView.setVisibility(View.VISIBLE);
                yijianTv.setText(resultModel.getDianping());
            }
            if (resultModel.getAnswer_detail() != null && resultModel.getAnswer_detail().size() > 0) {
                exercisesModels.addAll(resultModel.getAnswer_detail());
                mNumberAdapter.notifyDataSetChanged();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
            }
        }
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_home_work_report;
    }

    public static void start(Context context, HomeWorkResultModel model) {
        Intent intent = new Intent(context, StudentHomeWorkReportActivity.class);
        intent.putExtra("model", model);
        context.startActivity(intent);
    }

}
