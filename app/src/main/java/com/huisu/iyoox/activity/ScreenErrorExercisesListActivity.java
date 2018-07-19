package com.huisu.iyoox.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.ScreenTimeAdapter;
import com.huisu.iyoox.adapter.ScreenVersionAdapter;
import com.huisu.iyoox.entity.ScreenSubjectVersionModel;
import com.huisu.iyoox.entity.ScreenZhiShiDianModel;
import com.huisu.iyoox.entity.base.BaseScreenSubjectVersionModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.EbagGridView;
import com.huisu.iyoox.views.FlowViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Function:
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ScreenErrorExercisesListActivity extends BaseActivity {

    private EbagGridView timeGridView, versionGridView;
    private FlowViewGroup zhishiGroupView;
    private ArrayList<String> timeList = new ArrayList<>();
    private ArrayList<ScreenSubjectVersionModel> versionList = new ArrayList<>();
    private ScreenTimeAdapter timeAdapter;
    private ScreenVersionAdapter versionAdapter;
    private int selectTimePosition, selectVersionPosition, selectZhiShiPosition;
    private View submitBt;

    @Override
    protected void initView() {
        setTimeData();
        //时间
        timeGridView = findViewById(R.id.screen_time_grid_view);
        timeAdapter = new ScreenTimeAdapter(context, timeList);
        timeGridView.setAdapter(timeAdapter);
        //版本
        versionGridView = findViewById(R.id.screen_stubject_version_grid_view);
        versionAdapter = new ScreenVersionAdapter(context, versionList);
        versionGridView.setAdapter(versionAdapter);
        //知识点
        zhishiGroupView = findViewById(R.id.screen_zhishidian_grid_view);
        submitBt = findViewById(R.id.submit_bt);
    }

    @Override
    protected void initData() {
        setTitle("筛选");
        String studentId = getIntent().getStringExtra("studentId");
        String subjectId = getIntent().getStringExtra("subjectId");
        if (!TextUtils.isEmpty(studentId) && !TextUtils.isEmpty(subjectId)) {
            postScreenData(studentId, subjectId);
        }
    }

    private void setTimeData() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        timeList.add("全部");
        timeList.add("最近1个月");
        timeList.add("最近3个月");
        timeList.add("最近半年");
        timeList.add(year + "");
        timeList.add(year - 1 + "");
    }

    private void postScreenData(String studentId, String subjectId) {
        RequestCenter.getscreenErrorListData(studentId, subjectId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseScreenSubjectVersionModel baseScreenSubjectVersionModel = (BaseScreenSubjectVersionModel) responseObj;
                if (baseScreenSubjectVersionModel.data != null && baseScreenSubjectVersionModel.data.size() > 0) {
                    setResultData(baseScreenSubjectVersionModel.data);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    private void setResultData(List<ScreenSubjectVersionModel> data) {
        versionList.clear();
        versionList.addAll(data);
        versionAdapter.notifyDataSetChanged();
        addFlow(versionList.get(0).getZhishidian_list(), zhishiGroupView);
    }

    @Override
    protected void setEvent() {
        setBack();
        timeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectTimePosition = position;
                timeAdapter.setSelectPosition(selectTimePosition);
                timeAdapter.notifyDataSetChanged();
            }
        });
        versionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectVersionPosition = position;
                versionAdapter.setSelectPosition(selectVersionPosition);
                versionAdapter.notifyDataSetChanged();

                //刷新知识点数据
                selectZhiShiPosition = 0;
                addFlow(versionList.get(selectVersionPosition).getZhishidian_list(), zhishiGroupView);
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("time", selectTimePosition + 1 + "");
                intent.putExtra("version", versionList.get(selectVersionPosition).getJiaocai_id() + "");
                intent.putExtra("zhishi", versionList.get(selectVersionPosition)
                        .getZhishidian_list().get(selectZhiShiPosition).getZhishidian_id() + "");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 添加小类容
     */
    private void addFlow(final List<ScreenZhiShiDianModel> zhiShiList, final FlowViewGroup layout) {
        layout.removeAllViews();
        for (int i = 0; i < zhiShiList.size(); i++) {
            final int j = i;
            View view = View.inflate(context, R.layout.item_screen_zhishi_layout, null);
            TextView text = view.findViewById(R.id.item_screen_time_tv);
            layout.addView(view);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) text.getLayoutParams();
            lp.bottomMargin = 8;
            lp.topMargin = 8;
            lp.rightMargin = 10;
            lp.leftMargin = 10;
            view.setLayoutParams(lp);
            ScreenZhiShiDianModel model = zhiShiList.get(i);
            text.setText(model.getZhishidian_name());
            if (i == selectZhiShiPosition) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectZhiShiPosition = j;
                    addFlow(zhiShiList, layout);
                }
            });
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_screen_error_exercises_list;
    }
}
