package com.huisu.iyoox.activity.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.TeacherSelectSubjectVersionAdapter;
import com.huisu.iyoox.entity.BookEditionModel;
import com.huisu.iyoox.entity.base.BaseBookEditionModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherSelectSubjectVersionActivity extends BaseActivity implements MyOnItemClickListener {

    private RecyclerView recyclerView;
    private List<BookEditionModel> models = new ArrayList<>();
    private TeacherSelectSubjectVersionAdapter mAdapter;
    private String kemuId;
    private int selectVersionId = 0;

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.select_subject_version_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherSelectSubjectVersionAdapter(context, models);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        setTitle("选择教材版本");
        kemuId = getIntent().getStringExtra("kemuId");
        selectVersionId = getIntent().getIntExtra("versionId", 0);
        postVersionHttp();
    }

    private void postVersionHttp() {
        RequestCenter.getBookVersion(kemuId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseBookEditionModel baseBookEditionModel = (BaseBookEditionModel) responseObj;
                if (baseBookEditionModel.data != null && baseBookEditionModel.data.size() > 0) {
                    models.clear();
                    models.addAll(baseBookEditionModel.data);
                    mAdapter.setSelectVersionId(selectVersionId);
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
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_select_subject_version;
    }

    public static void start(Context context, int kemuId, int versionId) {
        Intent intent = new Intent(context, TeacherSelectSubjectVersionActivity.class);
        intent.putExtra("kemuId", kemuId + "");
        intent.putExtra("versionId", versionId);
        ((Activity) context).startActivityForResult(intent, 2);
    }

    @Override
    public void onItemClick(int positions, View view) {
        BookEditionModel editionModel = models.get(positions);
        Intent intent = new Intent();
        intent.putExtra("versionModel", editionModel);
        intent.putExtra("versionId", editionModel.getJiaocai_id());
        setResult(RESULT_OK, intent);
        finish();
    }
}
