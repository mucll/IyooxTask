package com.huisu.iyoox.fragment.home;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.student.StudentAddClassRoomActivity;
import com.huisu.iyoox.adapter.TeacherIconAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ClassRankingModel;
import com.huisu.iyoox.entity.StudentRankingModel;
import com.huisu.iyoox.entity.StudentScoreModel;
import com.huisu.iyoox.entity.TeacherModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseClassRankingModel;
import com.huisu.iyoox.fragment.ClassRankingFragment;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生端 -班级Fragment
 */
public class ClassFragment extends BaseFragment implements View.OnClickListener {


    private View view;
    private RecyclerView mRecyclerView;
    private TeacherIconAdapter teacherAdapter;
    private LineChart mChart;
    private WrapContentHeightViewPager mViewPager;
    private boolean init;
    private User user;
    private List<TeacherModel> teacherModels = new ArrayList<>();
    private List<StudentScoreModel> scoreModels = new ArrayList<>();
    private List<StudentRankingModel> rankingModels = new ArrayList<>();
    private TextView addClassBt;
    private TextView titleTv;
    private NestedScrollView scrollView;
    private View emptyView;
    private Loading loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class, container, false);
        initTab();
        initView();
        setEvent();
        return view;
    }

    private void initView() {
        user = UserManager.getInstance().getUser();
        scrollView = view.findViewById(R.id.class_scroll_view);
        emptyView = view.findViewById(R.id.empty_view_layout);
        addClassBt = view.findViewById(R.id.add_class_bt);
        titleTv = view.findViewById(R.id.title_bar_tv);

        mChart = view.findViewById(R.id.chart_line);
        mViewPager = view.findViewById(R.id.class_fragment_view_pager);
        mRecyclerView = view.findViewById(R.id.class_teacher_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        teacherAdapter = new TeacherIconAdapter(getContext(), teacherModels);
        mRecyclerView.setAdapter(teacherAdapter);
    }


    private void setEvent() {
        addClassBt.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        mViewPager.resetHeight(0);
    }


    @Override
    public void onShow() {
        super.onShow();
        if (!init) {
            isAddClassRoom();
            init = true;
        }
    }

    private void isAddClassRoom() {
        titleTv.setText("我的班级");
        user = UserManager.getInstance().getUser();
        postClassRankingData();
    }

    /**
     * 请求数据
     */
    private void postClassRankingData() {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.getClassRanking(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseClassRankingModel baseClassRankingModel = (BaseClassRankingModel) responseObj;
                if (baseClassRankingModel.code == Constant.POST_SUCCESS_CODE) {
                    if (baseClassRankingModel.data != null) {
                        setPostData(baseClassRankingModel.data);
                        emptyView.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        titleTv.setText(baseClassRankingModel.data.getClassroom_name());
                    } else {
                        scrollView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    /**
     * 请求到数据后 设置数据
     */
    private void setPostData(ClassRankingModel data) {
        teacherModels.clear();
        teacherModels.addAll(data.getTeacher_list());
        teacherAdapter.notifyDataSetChanged();
        scoreModels.clear();
        scoreModels.addAll(data.getStudent_fenshu_list());
        initChatLine();
        rankingModels.clear();
        rankingModels.addAll(data.getZhishidian_list());
        mViewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
    }

    private ClassRankingFragment getFragment(int position) {
        ClassRankingFragment fragment = new ClassRankingFragment(mViewPager, position);
        Bundle b = new Bundle();
        b.putSerializable("data", rankingModels.get(position));
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_class_bt:
                startAddClassAc();
                break;
        }
    }

    private void startAddClassAc() {
        Intent intent = new Intent(getContext(), StudentAddClassRoomActivity.class);
        getActivity().startActivityForResult(intent, Constant.POST_SUCCESS_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            isAddClassRoom();
        }
    }

    /**
     * 自定义适配器
     */
    class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public ClassRankingFragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return rankingModels.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ClassRankingFragment classRankingFragment = (ClassRankingFragment) super.instantiateItem(container, position);
            return classRankingFragment;
        }
    }

    /**
     * 设置趋势图
     */
    private void initChatLine() {
        //在chart上的右下角加描述
        mChart.setDescription("");
        mChart.setDescriptionTextSize(30);
        //设置Y轴上的单位
        //      mChart.setUnit("%");
        //设置透明度
        //      mChart.setAlpha(0.8f);
        //设置网格底下的那条线的颜色
        //      mChart.setBorderColor(Color.rgb(213, 216, 214));
        //      mChart.setBorderColor(Color.rgb(0, 0, 0));
        //      mChart.setBackgroundColor(Color.rgb(255, 255, 255));
//        mChart.setGridBackgroundColor(Color.rgb(255, 255, 255));
        mChart.setGridBackgroundColor(getContext().getResources().getColor(R.color.colorf7));
        //设置Y轴前后倒置
        //        mChart.setInvertYAxisEnabled(false);
        //        //设置高亮显示
        //        mChart.setHighlightEnabled(true);
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        mChart.setTouchEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBorders(false);
        //设置是否可以拖拽，缩放
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        //设置是否能扩大扩小
        mChart.setPinchZoom(true);
        //设置没有数据时显示的文本
        mChart.setNoDataText("还没有学习轨迹");
        // 设置背景颜色
        //         mChart.setBackgroundColor(Color.GRAY);
        //设置点击chart图对应的数据弹出标注
        //        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        // define an offset to change the original position of the marker
        // (optional)
        //        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
        //        mv.setMinimumHeight(80);
        //        // set the marker to the chart
        //        mChart.setMarkerView(mv);
        //        // enable/disable highlight indicators (the lines that indicate the
        //        // highlighted Entry)
        //        mChart.setHighlightIndicatorEnabled(false);
        XAxis xl = mChart.getXAxis();
        //      xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的数据在底部显示
        xl.setTextSize(10f); // 设置字体大小
        xl.setSpaceBetweenLabels(0); // 设置数据之间的间距'
        xl.setDrawGridLines(false);
        YAxis yl = mChart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //      yl.setAxisMaxValue(220f);
        yl.setTextSize(10f); // s设置字体大小
        yl.setAxisMinValue(0f);
        yl.setAxisMaxValue(100f);
        yl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                return (int) v + "";
            }
        });
        yl.setStartAtZero(true);
        yl.setLabelCount(5); // 设置Y轴最多显示的数据个数
        YAxis yr = mChart.getAxisRight();
        yr.setEnabled(false);
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < scoreModels.size(); i++) {
            StudentScoreModel model = scoreModels.get(i);
            xVals.add(i + 1 + "");
            yVals.add(new Entry(model.getFenshu(), i));
        }
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, null);
        set1.setDrawCubic(true);  //设置曲线为圆滑的线
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                //                return (int) v + "%";
                return "";
            }
        });
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(true);  //设置有圆点
        set1.setDrawCubic(false);
        set1.setDrawCircleHole(false);
        set1.setCubicIntensity(0);
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setColor(Color.rgb(244, 117, 117));    //设置曲线的颜色
        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);
        // set data
        Legend mLegend = mChart.getLegend();
        mLegend.setEnabled(false);

        mChart.setData(data);
        mChart.animateY(800);
    }

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

}
