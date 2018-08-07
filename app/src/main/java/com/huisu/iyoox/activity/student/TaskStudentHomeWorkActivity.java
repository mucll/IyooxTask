package com.huisu.iyoox.activity.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Chronometer;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.TaskResultActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.ExercisesResultModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.base.BaseExercisesModel;
import com.huisu.iyoox.entity.base.BaseTaskResultModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.fragment.exercisespager.ExercisesPageFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DateUtils;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 学生作业界面
 * @date: 18/6/28
 */
public class TaskStudentHomeWorkActivity extends BaseActivity implements ExercisesPageFragment.OnStudentAnswerResultListener {

    private String videoId, zhishiId;
    private Loading loading;
    private String zhishidianName;
    private Chronometer chronometer;
    private int type;
    private String work_id;
    private String homeWorkTitle;

    @Override
    protected void initView() {
        chronometer = findViewById(R.id.tv_time);
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", Constant.ERROR_CODE);
        switch (type) {
            case Constant.STUDENT_DOING:
                studentDoing();
                break;
            case Constant.STUDENT_HOME_WORK:
                studentHomeWork();
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
     * 知识点下的题目
     */
    private void studentDoing() {
        setTitle("课后习题");
        videoId = getIntent().getStringExtra("videoId");
        zhishiId = getIntent().getStringExtra("zhishiId");
        if (!TextUtils.isEmpty(videoId)) {
            postExercisesData();
        }
    }

    /**
     * 老师布置的作业
     */
    private void studentHomeWork() {
        work_id = getIntent().getStringExtra("work_id");
        homeWorkTitle = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(work_id) && !TextUtils.isEmpty(homeWorkTitle)) {
            setTitle(homeWorkTitle);
            postHomeWorkData(work_id, homeWorkTitle);
        }
    }

    /**
     * 学生作业详情
     *
     * @param work_id
     * @param title
     */
    private void postHomeWorkData(String work_id, final String title) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getStudentTaskDetails(work_id, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseExercisesModel baseVideoUrlModel = (BaseExercisesModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE) {
                    if (baseVideoUrlModel.data != null && baseVideoUrlModel.data.size() > 0) {
                        //计时器清零
                        chronometer.setVisibility(View.VISIBLE);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        initFragment(baseVideoUrlModel.data, title, Constant.STUDENT_HOME_WORK);
                    } else {
                        TabToast.showMiddleToast(context, baseVideoUrlModel.msg);
                        EventBus.getDefault().post("home_work");
                        finish();
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
     * 获取知识点作业题目信息
     */
    private void postExercisesData() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getVideoTimu(videoId, zhishiId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
                    VideoTimuModel urlModel = baseVideoUrlModel.data;
                    if (urlModel.getTimu_list() != null && urlModel.getTimu_list().size() > 0) {
                        //计时器清零
                        chronometer.setVisibility(View.VISIBLE);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        initFragment(urlModel.getTimu_list(), baseVideoUrlModel.data.getShipin_name(), Constant.STUDENT_DOING);
                    } else {
                        TabToast.showMiddleToast(context, "暂无习题");
                    }
                } else {
                    TabToast.showMiddleToast(context, "暂无习题");
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

    /**
     * 提交答案的回调
     *
     * @param exercisesData
     */
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
        chronometer.stop();
        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
        String timeString = DateUtils.formatmmss(time);
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
        intent.putExtra("time", timeString);
        intent.putExtra("type", type);
        if (type == Constant.STUDENT_DOING) {
            intent.putExtra("zhishiId", zhishiId);
            intent.putExtra("zhishidianName", zhishidianName);
            startActivity(intent);
            finish();
        } else if (type == Constant.STUDENT_HOME_WORK) {
//            intent.putExtra("workId", work_id);
//            intent.putExtra("homeWorkTitle", homeWorkTitle);
            //发消息刷新作业列表界面
//            startActivity(intent);
            setPostHomeWorkInitData(exercisesData, timeString);
        }

    }


    /**
     * 初始化学生作业报告请求参数
     */
    private void setPostHomeWorkInitData(ArrayList<ExercisesModel> exercisesModels, String time) {
        List<ExercisesResultModel> resultModels = new ArrayList<>();
        for (ExercisesModel model : exercisesModels) {
            ExercisesResultModel resultModel = new ExercisesResultModel();
            resultModel.setTimu_id(Integer.parseInt(model.getTimu_id()));
            resultModel.setIs_correct(model.getAnswersModel().isCorrect() ? Constant.ANSWER_CORRECT : Constant.ANSWER_ERROR);
            resultModel.setChooseanswer(model.getAnswersModel().getChooseAnswer());
            resultModels.add(resultModel);
        }
        String json = JsonUtils.jsonFromObject(resultModels);
        postHomeWorkResultData(json, time);
    }

    /**
     * 学生作业报告
     *
     * @param json
     */
    private void postHomeWorkResultData(String json, String time) {
        User user = UserManager.getInstance().getUser();
        RequestCenter.getStudentTaskResult(user.getUserId(), work_id, time, json, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (code == Constant.POST_SUCCESS_CODE) {
                        EventBus.getDefault().post("home_work");
                        finish();
                    } else {
                        TabToast.showMiddleToast(context, "提交异常,请重新提交");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chronometer.stop();
    }
}
