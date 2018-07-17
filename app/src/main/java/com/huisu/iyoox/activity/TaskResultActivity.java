package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    private Button resultAnalysis;
    private ArrayList<ExercisesModel> exercisesModels = new ArrayList<>();
    private String zhishidianName;

    @Override
    protected void initView() {
        mGridView = findViewById(R.id.item_answer_card_grid_view);
        mPieChart = findViewById(R.id.item_answer_total_analysis_chart_view);
        resultAnalysis = findViewById(R.id.result_analysis_bt);
        mNumberAdapter = new AnswerResultNumberAdapter(context, exercisesModels);
        mGridView.setAdapter(mNumberAdapter);
        initChart(mPieChart, 66);
    }

    @Override
    protected void initData() {
        exercisesModels.clear();
        setTitle("答题报告");
        ArrayList<ExercisesModel> models = (ArrayList<ExercisesModel>) getIntent().getSerializableExtra("data");
        zhishidianName = getIntent().getStringExtra("zhishidianName");
        if (models != null && models.size() > 0) {
            exercisesModels.addAll(models);
        }
        mNumberAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setEvent() {
        setBack();
        resultAnalysis.setOnClickListener(this);
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
