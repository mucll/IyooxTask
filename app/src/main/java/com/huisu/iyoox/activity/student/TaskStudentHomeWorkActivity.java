package com.huisu.iyoox.activity.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.TaskResultActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.fragment.exercisespager.ExercisesPageFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.views.Loading;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 学生作业界面
 * @date: 18/6/28
 */
public class TaskStudentHomeWorkActivity extends BaseActivity implements ExercisesPageFragment.OnStudentAnswerResultListener {

    private String videoId;
    private Loading loading;
    private String zhishidianName;

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        int type = getIntent().getIntExtra("type", Constant.ERROR_CODE);
        switch (type) {
            case Constant.STUDENT_DOING:
                studentDoing();
                break;
            case Constant.STUDENT_ANALYSIS:
                studentAnalysis();
                break;
            default:
                LogUtil.e("TaskStudentHomeWorkActivity:type is error");
                break;
        }
    }

    /**
     * 做题目
     */
    private void studentDoing() {
        setTitle("课后习题");
        videoId = getIntent().getStringExtra("videoId");
        if (!TextUtils.isEmpty(videoId)) {
            postExercisesData();
        }
    }

    /**
     * 题目分析
     */
    private void studentAnalysis() {
        setTitle("习题分析");
        ArrayList<ExercisesModel> models = (ArrayList<ExercisesModel>) getIntent().getSerializableExtra("data");
        zhishidianName = getIntent().getStringExtra("zhishidianName");
        if (!TextUtils.isEmpty(zhishidianName) && models != null && models.size() > 0) {
            initFragment(models, zhishidianName, Constant.STUDENT_ANALYSIS);
        }
    }

    /**
     * 获取题目信息
     */
    private void postExercisesData() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getVideoTimu(videoId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
                    VideoTimuModel urlModel = baseVideoUrlModel.data;
                    if (urlModel.getTimu_list() != null && urlModel.getTimu_list().size() > 0) {
                        initFragment(urlModel.getTimu_list(), baseVideoUrlModel.data.getShipin_name(), Constant.STUDENT_DOING);
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
     * 初始化Fragment
     *
     * @param timuList   题目信息
     * @param zhishidian 知识点名字
     * @param type       展示类型
     */
    private void initFragment(List<ExercisesModel> timuList, String zhishidian, int type) {
        this.zhishidianName = zhishidian;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ExercisesPageFragment fragment = new ExercisesPageFragment();
        fragment.setOnStudentAnswerResultListener(this);
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putSerializable("exercises_models", (Serializable) timuList);
        b.putString("zhishidian_name", zhishidian);
        fragment.setArguments(b);
        ft.replace(R.id.exercises_content_layout, fragment);
        ft.commit();
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_task_student_home_work;
    }

    @Override
    public void studentAnswerResult(ArrayList<ExercisesModel> exercisesData) {
        judgeExercisesCorrect(exercisesData);
    }

    /**
     * 题目做完后的回调
     *
     * @param exercisesData 题目信息
     */
    private void judgeExercisesCorrect(ArrayList<ExercisesModel> exercisesData) {
        for (ExercisesModel model : exercisesData) {
            if (model.getAnswersModel().getChooseAnswer().equals(model.getDaan())) {
                //正确
                model.getAnswersModel().setCorrect(true);
            } else {
                //错误
                model.getAnswersModel().setCorrect(false);
            }
        }
        Intent intent = new Intent(this, TaskResultActivity.class);
        intent.putExtra("data", exercisesData);
        intent.putExtra("zhishidianName", zhishidianName);
        startActivity(intent);
        finish();
    }
}
