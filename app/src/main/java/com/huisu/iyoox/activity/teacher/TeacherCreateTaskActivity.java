package com.huisu.iyoox.activity.teacher;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.register.TeacherSelectSubjectVersionActivity;
import com.huisu.iyoox.adapter.TeacherZhiShiDianListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.BookEditionModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoGroupModel;
import com.huisu.iyoox.entity.VideoModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseVideoGroupModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.views.DropdownButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 一课一练
 */
public class TeacherCreateTaskActivity extends BaseActivity implements DropdownButton.OnDropItemSelectListener, View.OnClickListener, ExpandableListView.OnChildClickListener {


    private DropdownButton dropdownBt;
    private ExpandableListView mListView;
    private ArrayList<VideoGroupModel> videoGroupModels = new ArrayList<>();

    private ArrayList<VideoModel> data = new ArrayList<>();
    private TeacherZhiShiDianListAdapter mAdapter;
    private TextView versionView;
    private User user;

    private int gradeCode = 0;
    private int kemu_id = 0;
    private int versionId = 0;
    private int versionDetailId = 0;
    private View emptyView;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        gradeCode = user.getGrade();
        kemu_id = user.getXueke_id();
        versionId = user.getJiaocai_id();
        versionDetailId = user.getGrade_detail_id();

        versionView = findViewById(R.id.tv_submit);
        versionView.setText(user.getVersionName());
        versionView.setVisibility(View.VISIBLE);
        dropdownBt = findViewById(R.id.dropdown_tab_bt);
        emptyView = findViewById(R.id.empty_view);
        mListView = findViewById(R.id.swipe_target);
        mAdapter = new TeacherZhiShiDianListAdapter(context, data);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        setTitle("一课一练");
        postBookDetailHttp();
    }

    private int page = 1;

    private void postBookDetailHttp() {
        RequestCenter.getZhangjieDetail(gradeCode + "", kemu_id + ""
                , versionId + "", versionDetailId + "", page + "", new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        videoGroupModels.clear();
                        data.clear();
                        BaseVideoGroupModel baseVideoGroupModel = (BaseVideoGroupModel) responseObj;
                        if (baseVideoGroupModel.data != null && baseVideoGroupModel.data.size() > 0) {
                            videoGroupModels.addAll(baseVideoGroupModel.data);
                            dropdownBt.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                            dropdownBt.setData(baseVideoGroupModel.data);
                            data.addAll(baseVideoGroupModel.data.get(0).getZhishidian());
                        } else {
                            dropdownBt.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();
                        for (int i = 0; i < data.size(); i++) {
                            mListView.expandGroup(i);
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
    }

    @Override
    protected void setEvent() {
        setBack();
        versionView.setOnClickListener(this);
        dropdownBt.setOnDropItemSelectListener(this);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        mListView.setOnChildClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        VideoTitleModel shipin = data.get(groupPosition).getShipinlist().get(childPosition);
        TeacherSelectExercisesActivity.start(this, shipin,
                getIntent().getIntExtra("taskType", Constant.ERROR_CODE));
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                TeacherSelectSubjectVersionActivity.start(this, gradeCode,
                        kemu_id, versionId, versionDetailId);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_create_task;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            BookEditionModel versionModel = (BookEditionModel) data.getSerializableExtra("versionModel");
            versionId = data.getIntExtra("versionId", 0);
            versionDetailId = data.getIntExtra("versionDetailId", 0);
            versionView.setText(versionModel.getVersionName());
            page = 1;
            postBookDetailHttp();
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, TeacherCreateTaskActivity.class);
        intent.putExtra("taskType", type);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    /**
     * 章节下拉点击监听
     *
     * @param Postion
     */
    @Override
    public void onDropItemSelect(int Postion) {
        data.clear();
        data.addAll(videoGroupModels.get(Postion).getZhishidian());
        mAdapter.notifyDataSetChanged();
        for (int i = 0; i < data.size(); i++) {
            mListView.expandGroup(i);
        }
    }

}
