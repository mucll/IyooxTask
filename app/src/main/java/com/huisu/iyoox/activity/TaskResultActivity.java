package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.student.TaskStudentHomeWorkActivity;
import com.huisu.iyoox.adapter.AnswerResultNumberAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.ExercisesResultModel;
import com.huisu.iyoox.entity.TaskResultModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseTaskResultModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.views.EbagGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 答题报告
 * @date: 18/6/28
 */
public class TaskResultActivity extends BaseActivity implements View.OnClickListener {

    private EbagGridView mGridView;
    private AnswerResultNumberAdapter mNumberAdapter;
    private PieChart mPieChart;
    private Button resultAnalysis, refreshBt;
    private ArrayList<ExercisesModel> exercisesModels = new ArrayList<>();
    private String zhishidianName, zhishiId, timeString;
    private User user;
    private TextView zhishidianTv, scoreTv, commentTv, jianyiTv, sortTv, timesTv, exercisesCountTv;
    private RatingBar nanduBar;

    @Override
    protected void initView() {
        mGridView = findViewById(R.id.item_answer_card_grid_view);
        mPieChart = findViewById(R.id.item_answer_total_analysis_chart_view);
        resultAnalysis = findViewById(R.id.result_analysis_bt);
        refreshBt = findViewById(R.id.refresh_bt);

        zhishidianTv = findViewById(R.id.task_result_zhishi_name_tv);
        scoreTv = findViewById(R.id.task_result_score_tv);
        commentTv = findViewById(R.id.task_result_comment_tv);
        jianyiTv = findViewById(R.id.task_result_jianyi);
        sortTv = findViewById(R.id.task_result_sort);
        timesTv = findViewById(R.id.task_result_times);
        exercisesCountTv = findViewById(R.id.task_result_exercises_count_tv);
        nanduBar = findViewById(R.id.task_result_nandu);

        mNumberAdapter = new AnswerResultNumberAdapter(context, exercisesModels);
        mGridView.setAdapter(mNumberAdapter);
    }

    @Override
    protected void initData() {
        user = UserManager.getInstance().getUser();
        exercisesModels.clear();
        setTitle("答题报告");
        ArrayList<ExercisesModel> models = (ArrayList<ExercisesModel>) getIntent().getSerializableExtra("data");
        zhishidianName = getIntent().getStringExtra("zhishidianName");
        zhishiId = getIntent().getStringExtra("zhishiId");
        timeString = getIntent().getStringExtra("time");

        //不是从完成作业界面点击进来的
        if (models != null && models.size() > 0) {
            exercisesModels.addAll(models);
            mNumberAdapter.notifyDataSetChanged();
            if (!TextUtils.isEmpty(zhishiId)) {
                setPostInitData();
            }
        }

    }

    /**
     * 初始化请求参数
     */
    private void setPostInitData() {
        List<ExercisesResultModel> resultModels = new ArrayList<>();
        for (ExercisesModel model : exercisesModels) {
            ExercisesResultModel resultModel = new ExercisesResultModel();
            resultModel.setTimu_id(Integer.parseInt(model.getTimu_id()));
            resultModel.setIs_correct(model.getAnswersModel().isCorrect() ? 1 : 0);
            resultModels.add(resultModel);
        }
        String json = JsonUtils.jsonFromObject(resultModels);
        postResultData(json);
    }

    /**
     * 请求报告数据
     *
     * @param resultJson
     */
    private void postResultData(String resultJson) {
        RequestCenter.getTaskResultData(user.getId(), zhishiId, timeString, resultJson, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseTaskResultModel resultModel = (BaseTaskResultModel) responseObj;
                if (resultModel.data != null) {
                    setResultData(resultModel.data);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 初始化 数据
     *
     * @param data
     */
    private void setResultData(TaskResultModel data) {
        //正确率
        initChart(mPieChart, data.getCorrect_rate());
        //知识点
        zhishidianTv.setText(data.getZhishidian_name());
        //得分比
        scoreTv.setText(data.getCorrect_rate() + "%");
        //难度
        nanduBar.setRating(data.getNanyi_level());
        //掌握
        commentTv.setText(data.getZhangwo_level());
        //学习建议
        jianyiTv.setText(data.getSuggest());
        //排比
        sortTv.setText(data.getSort_rate());
        //时间
        timesTv.setText(data.getTimes());
        //时间
        exercisesCountTv.setText(data.getTimu_count()+"");

    }

    @Override
    protected void setEvent() {
        setBack();
        resultAnalysis.setOnClickListener(this);
        refreshBt.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_task_result;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.result_analysis_bt:
                startAnalysis();
                break;
            case R.id.refresh_bt:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到题目解析
     */
    private void startAnalysis() {
        Intent intent = new Intent(this, TaskStudentHomeWorkActivity.class);
        intent.putExtra("type", Constant.STUDENT_ANALYSIS);
        intent.putExtra("data", exercisesModels);
        intent.putExtra("zhishidianName", zhishidianName);
        startActivity(intent);
    }

    /**
     * 正确率的显示
     *
     * @param chart   view
     * @param avgRate 正确率
     */
    private void initChart(PieChart chart, int avgRate) {
        chart.setVisibility(View.VISIBLE);
        //xVals用来表示每个饼块上的内容
        ArrayList<String> xValues = new ArrayList<String>();
        //yVals用来表示封装每个饼块的实际数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < 2; i++) {
            //饼块上显示成Quarterly1, Quarterly2, Quarterly3,
            xValues.add("");
            if (i == 0) {
                //設置比例
                yValues.add(new Entry(avgRate, i));
            } else if (i == 1) {
                //設置比例
                yValues.add(new Entry(100 - avgRate, i));
            }
        }
        //y轴的集合 /*显示在比例图上*/
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        //设置个饼状图之间的距离
        pieDataSet.setSliceSpace(0f);
        pieDataSet.setDrawValues(false);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(context.getResources().getColor(R.color.ex_right_color));
        colors.add(context.getResources().getColor(R.color.colore9));
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(xValues, pieDataSet);
        //设置数据
        chart.setCenterText(avgRate + "%");
        chart.setCenterTextSize(18);
        chart.setCenterTextColor(context.getResources().getColor(R.color.ex_right_color));
        chart.setHoleColorTransparent(true);
        //里面的白圈半径
        chart.setHoleRadius(92f);
        chart.setDescription("");
        //饼状图中间可以添加文字
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(true);
        // 初始旋转角度
        chart.setRotationAngle(260);
        // 可以手动旋转
        chart.setRotationEnabled(true);
        //显示成百分比
        chart.setUsePercentValues(false);
        chart.setDrawSliceText(false);
        chart.setTouchEnabled(false);
        //设置数据
        chart.setData(pieData);
        //设置比例图
        Legend mLegend = chart.getLegend();
        mLegend.setEnabled(false);
        //设置动画
        chart.animateXY(500, 500);
    }

}
