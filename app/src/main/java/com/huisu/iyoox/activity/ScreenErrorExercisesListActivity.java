package com.huisu.iyoox.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.ScreenTimeAdapter;
import com.huisu.iyoox.adapter.ScreenVersionAdapter;
import com.huisu.iyoox.adapter.ScreenZhiShiDianAdapter;
import com.huisu.iyoox.entity.ScreenSubjectVersionModel;
import com.huisu.iyoox.entity.ScreenZhiShiDianModel;
import com.huisu.iyoox.entity.base.BaseScreenSubjectVersionModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.EbagGridView;

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

    private EbagGridView timeGridView, versionGridView, zhishiGridView;

    private ArrayList<String> timeList = new ArrayList<>();
    private ArrayList<ScreenSubjectVersionModel> subjectList = new ArrayList<>();
    private ArrayList<ScreenZhiShiDianModel> zhiShiList = new ArrayList<>();
    private ScreenTimeAdapter timeAdapter;
    private ScreenVersionAdapter versionAdapter;
    private ScreenZhiShiDianAdapter zhishiAdapter;
    private int setSelectTimePosition, setSelectVersionPosition, setSelectZhiShiPosition;
    private Button submitBt;

    @Override
    protected void initView() {
        setTimeData();
        timeGridView = findViewById(R.id.screen_time_grid_view);
        timeAdapter = new ScreenTimeAdapter(context, timeList);
        timeGridView.setAdapter(timeAdapter);
        versionGridView = findViewById(R.id.screen_stubject_version_grid_view);
        versionAdapter = new ScreenVersionAdapter(context, subjectList);
        versionGridView.setAdapter(versionAdapter);
        zhishiGridView = findViewById(R.id.screen_zhishidian_grid_view);
        zhishiAdapter = new ScreenZhiShiDianAdapter(context, zhiShiList);
        zhishiGridView.setAdapter(zhishiAdapter);
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
        subjectList.clear();
        zhiShiList.clear();
        subjectList.addAll(data);
        zhiShiList.addAll(subjectList.get(0).getZhishidian_list());
        versionAdapter.notifyDataSetChanged();
        zhishiAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setEvent() {
        setBack();
        timeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectTimePosition = position;
                timeAdapter.setSelectPosition(setSelectTimePosition);
                timeAdapter.notifyDataSetChanged();
            }
        });
        versionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectVersionPosition = position;
                versionAdapter.setSelectPosition(setSelectVersionPosition);
                versionAdapter.notifyDataSetChanged();

                //刷新知识点数据
                zhiShiList.clear();
                zhiShiList.addAll(subjectList.get(setSelectVersionPosition).getZhishidian_list());
                setSelectZhiShiPosition = 0;
                zhishiAdapter.setSelectPosition(setSelectZhiShiPosition);
                zhishiAdapter.notifyDataSetChanged();
            }
        });
        zhishiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectZhiShiPosition = position;
                zhishiAdapter.setSelectPosition(setSelectZhiShiPosition);
                zhishiAdapter.notifyDataSetChanged();
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("time", setSelectTimePosition + 1 + "");
                intent.putExtra("version", subjectList.get(setSelectVersionPosition).getJiaocai_id() + "");
                intent.putExtra("zhishi", subjectList.get(setSelectVersionPosition)
                        .getZhishidian_list().get(setSelectZhiShiPosition).getZhishidian_id() + "");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_screen_error_exercises_list;
    }
}
