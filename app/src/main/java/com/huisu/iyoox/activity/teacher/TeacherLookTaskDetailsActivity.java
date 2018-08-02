package com.huisu.iyoox.activity.teacher;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.fragment.teacher.TeacherLookTaskDetailFragment;
import com.huisu.iyoox.views.MyFragmentLayout_line;

import java.util.ArrayList;

/**
 * 老师查看一个班的作业详情
 */
public class TeacherLookTaskDetailsActivity extends BaseActivity {

    private MyFragmentLayout_line mFragmentLayout;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private PieChart timelyPC;
    private PieChart correctPC;

    @Override
    protected void initView() {
        mFragmentLayout = findViewById(R.id.teacher_look_task_detail_layout);
        timelyPC = findViewById(R.id.teacher_look_task_delete_timely_pc);
        correctPC = findViewById(R.id.teacher_look_task_delete_correct_pc);
    }

    @Override
    protected void initData() {
        setTitle("作业完成详情");
        initChart(timelyPC, 25, R.color.red64);
        initChart(correctPC, 75, R.color.ex_right_color);
        initFragment();
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_look_task_details;
    }

    private void initFragment() {
        fragments.clear();
        fragments.add(getFragment(Constant.TASK_STUDENT_FINISHED));
        fragments.add(getFragment(Constant.TASK_STUDENT_UNFINISH));
        fragments.add(getFragment(Constant.TASK_EXERCISES_NUMBER));
        mFragmentLayout.setScorllToNext(true);
        mFragmentLayout.setScorll(true);
        mFragmentLayout.setWhereTab(1);
        mFragmentLayout.setTabHeight(4, getResources().getColor(R.color.maincolor), false);
        mFragmentLayout.setOnChangeFragmentListener(new MyFragmentLayout_line.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int positon,
                               View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(R.id.tab_text))
                        .setTextColor(getResources().getColor(R.color.color333));
                ((TextView) currentTabView.findViewById(R.id.tab_text))
                        .setTextColor(getResources().getColor(R.color.main_text_color));
                fragments.get(positon).onShow();
            }
        });
        mFragmentLayout.setAdapter(fragments, R.layout.tablayout_teacher_look_task_detail, 0x203);
    }

    private TeacherLookTaskDetailFragment getFragment(int type) {
        TeacherLookTaskDetailFragment fragment = new TeacherLookTaskDetailFragment();
        Bundle b = new Bundle();
        b.putInt("type", type);
        fragment.setArguments(b);
        return fragment;
    }

    public static void start(Context context, int taskId) {
        Intent intent = new Intent(context, TeacherLookTaskDetailsActivity.class);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    /**
     * 正确率的显示
     *
     * @param chart   view
     * @param avgRate 正确率
     */
    private void initChart(PieChart chart, int avgRate, int color) {
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
        colors.add(context.getResources().getColor(color));
        colors.add(context.getResources().getColor(R.color.coloreeee));
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(xValues, pieDataSet);
        //设置数据
        chart.setCenterText(avgRate + "%");
        chart.setCenterTextSize(18);
        chart.setCenterTextColor(context.getResources().getColor(R.color.color333));
        chart.setHoleColorTransparent(true);
        //里面的白圈半径
        chart.setHoleRadius(85f);
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
