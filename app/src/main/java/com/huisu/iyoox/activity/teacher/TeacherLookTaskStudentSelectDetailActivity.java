package com.huisu.iyoox.activity.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.base.BaseExercisesModel;
import com.huisu.iyoox.fragment.exercisespager.ExercisesPageFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.Loading;

import java.io.Serializable;
import java.util.List;

public class TeacherLookTaskStudentSelectDetailActivity extends BaseActivity {
    private FragmentManager manager;
    private int workId;
    private int selectPosition;
    private String title;
    private Loading loading;
    private ExercisesPageFragment fragment;

    @Override
    protected void initView() {
        manager = getSupportFragmentManager();
    }

    @Override
    protected void initData() {
        setTitle("题目详情");
        workId = getIntent().getIntExtra("workId", Constant.ERROR_CODE);
        selectPosition = getIntent().getIntExtra("selectPosition", 0);
        title = getIntent().getStringExtra("title");
        if (workId != Constant.ERROR_CODE) {
            postTaskDetailHttp();
        }
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }

    private void postTaskDetailHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.teacherLookTaskTimuDetails(workId + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseExercisesModel baseModel = (BaseExercisesModel) responseObj;
                if (baseModel.code == Constant.POST_SUCCESS_CODE && baseModel.data != null && baseModel.data.size() > 0) {
                    initFragment(baseModel.data, title);
                }
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
    }

    private void initFragment(List<ExercisesModel> timuList, String title) {
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new ExercisesPageFragment();
        Bundle b = new Bundle();
        b.putInt("type", Constant.TEACHER_LOOK_TASK);
        b.putSerializable("exercises_models", (Serializable) timuList);
        b.putString("zhishidian_name", title);
        fragment.setArguments(b);
        transaction.replace(R.id.fragment_content_layout, fragment);
        transaction.commit();
        if (timuList.size() > selectPosition) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragment.setPositions(selectPosition);
                }
            }, 200);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_look_task_student_select_detail;
    }

    public static void start(Context context, int workId, String title, int selectPosition) {
        Intent intent = new Intent(context, TeacherLookTaskStudentSelectDetailActivity.class);
        intent.putExtra("workId", workId);
        intent.putExtra("title", title);
        intent.putExtra("selectPosition", selectPosition);
        context.startActivity(intent);
    }
}
