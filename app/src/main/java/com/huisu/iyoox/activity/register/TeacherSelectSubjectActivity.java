package com.huisu.iyoox.activity.register;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.TeacherSelectSubjectAdapter;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.base.BaseSubjectModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.EbagGridView;

import java.util.ArrayList;
import java.util.List;

public class TeacherSelectSubjectActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private EbagGridView mGridView;
    private List<SubjectModel> models = new ArrayList<>();
    private TeacherSelectSubjectAdapter mAdapter;
    private int subjectId;

    @Override
    protected void initView() {
        mGridView = findViewById(R.id.teacher_select_subject_grid_view);
        mAdapter = new TeacherSelectSubjectAdapter(context, models);
        mGridView.setAdapter(mAdapter);
    }


    @Override
    protected void initData() {
        setTitle("选择科目");
        String gradeId = getIntent().getStringExtra("gradeId");
        subjectId = getIntent().getIntExtra("subjectId", 0);
        postSubjectHttp(gradeId);
    }

    private void postSubjectHttp(String gradeId) {
        RequestCenter.GET_KEMU_BY_GRADE(gradeId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseSubjectModel baseSubjectModel = (BaseSubjectModel) responseObj;
                if (baseSubjectModel.data != null && baseSubjectModel.data.size() > 0) {
                    models.clear();
                    models.addAll(baseSubjectModel.data);
                    mAdapter.setSelectPosition(subjectId);
                    mAdapter.notifyDataSetChanged();
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
        mGridView.setOnItemClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_select_subject;
    }

    public static void start(Context context, int gradeId, int subjectId) {
        Intent intent = new Intent(context, TeacherSelectSubjectActivity.class);
        intent.putExtra("gradeId", gradeId + "");
        intent.putExtra("subjectId", subjectId);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SubjectModel model = models.get(position);
        Intent intent = new Intent();
        intent.putExtra("model", model);
        intent.putExtra("subjectId", model.getKemu_id());
        setResult(RESULT_OK, intent);
        finish();
    }
}
