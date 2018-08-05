package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.TeacherSelectExercisesAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseExercisesModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.views.Loading;

import java.util.ArrayList;
import java.util.List;

/**
 * 老师发作业选择题目
 */
public class TeacherSelectExercisesActivity extends BaseActivity implements View.OnClickListener {

    private VideoTitleModel videoTitleModel;
    private RecyclerView recyclerView;
    private ArrayList<ExercisesModel> models = new ArrayList<>();
    private TeacherSelectExercisesAdapter mAdapter;
    private TextView submit;
    private View emptyView;
    private Loading loading;

    @Override
    protected void initView() {
        emptyView = findViewById(R.id.empty_view);
        submit = findViewById(R.id.teacher_select_exercises_submit_tv);
        submit.setEnabled(false);
        recyclerView = findViewById(R.id.teacher_select_exercises_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherSelectExercisesAdapter(this, models) {
            @Override
            public void change() {
                setSubmitEnable();
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        videoTitleModel = (VideoTitleModel) getIntent().getSerializableExtra("model");
        if (videoTitleModel != null) {
            setTitle(videoTitleModel.getShipin_name());
            postExercisesDataHttp();
        }
    }

    private void postExercisesDataHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.teacherSelectExercisesData(videoTitleModel.getShipin_id() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                models.clear();
                BaseExercisesModel baseExercisesModel = (BaseExercisesModel) responseObj;
                if (baseExercisesModel.code == Constant.POST_SUCCESS_CODE) {
                    if (baseExercisesModel.data != null && baseExercisesModel.data.size() > 0) {
                        models.addAll(baseExercisesModel.data);
                        emptyView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
                setSubmitEnable();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    @Override
    protected void setEvent() {
        setBack();
        submit.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_select_exercises;
    }

    private void setSubmitEnable() {
        submit.setText("选好啦(共" + models.size() + "题)");
        if (models.size() > 0) {
            submit.setEnabled(true);
        } else {
            submit.setEnabled(false);
        }
    }

    public static void start(Context context, VideoTitleModel videoTitleModel, int taskType) {
        Intent intent = new Intent(context, TeacherSelectExercisesActivity.class);
        intent.putExtra("model", videoTitleModel);
        intent.putExtra("taskType", taskType);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    @Override
    public void onClick(View v) {
        ArrayList<Integer> jsonlist = new ArrayList<>();
        for (ExercisesModel model : models) {
            jsonlist.add(Integer.parseInt(model.getTimu_id()));
        }
        String timuIds = JsonUtils.jsonFromObject(jsonlist);
        TeacherSendTaskActivity.start(this, timuIds, videoTitleModel.getZhishidian_id()
                , getIntent().getIntExtra("taskType", Constant.ERROR_CODE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
