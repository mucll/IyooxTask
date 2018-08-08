package com.huisu.iyoox.fragment.teacher;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherLookStudentTaskDetailActivity;
import com.huisu.iyoox.adapter.TeacherLookTaskDetailAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TaskTeacherLookClassModel;
import com.huisu.iyoox.entity.TaskTeacherLookStudentModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.ExercisesNumberView;
import com.huisu.iyoox.views.Loading;

import java.util.ArrayList;
import java.util.List;

/**
 * 老师查看班级作业详情Fragment
 */
public class TeacherLookTaskDetailFragment extends BaseFragment implements MyOnItemClickListener, View.OnClickListener {

    private View view;
    private RecyclerView mRecyclerView;
    private TeacherLookTaskDetailAdapter mAdapter;
    private int type;
    private int workId;
    private String title;
    private View finishedView;
    private View unfinishView;
    private ScrollView scrollView;
    private TaskTeacherLookClassModel model;
    private ArrayList<TaskTeacherLookStudentModel> childModels = new ArrayList<>();
    private TextView unfinishTv;
    private ExercisesNumberView numberView;
    private View submitTV;
    private Loading loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", Constant.ERROR_CODE);
            workId = bundle.getInt("workId", Constant.ERROR_CODE);
            title = bundle.getString("title");
            model = (TaskTeacherLookClassModel) bundle.getSerializable("model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_look_task_detail, container, false);
        numberView = view.findViewById(R.id.teacher_look_task_number_view);
        submitTV = view.findViewById(R.id.submit_tv);
        finishedView = view.findViewById(R.id.teacher_look_task_student_finished_ll);
        unfinishView = view.findViewById(R.id.teacher_look_task_student_unfinish_ll);
        unfinishTv = view.findViewById(R.id.teacher_look_task_student_unfinish_tv);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        scrollView = view.findViewById(R.id.teacher_look_number_sl);
        childModels.clear();
        switch (type) {
            case Constant.TASK_STUDENT_FINISHED:
                if (model.getYiwancheng() != null && model.getYiwancheng().size() > 0) {
                    childModels.addAll(model.getYiwancheng());
                }
                initView();
                setEvent();
                break;
            case Constant.TASK_STUDENT_UNFINISH:
                if (model.getWeiwancheng() != null && model.getWeiwancheng().size() > 0) {
                    childModels.addAll(model.getWeiwancheng());
                }
                initView();
                setEvent();
                break;
            case Constant.TASK_EXERCISES_NUMBER:
                initNumberView();
                break;
            default:
                break;
        }
        return view;
    }

    private void initView() {
        scrollView.setVisibility(View.GONE);
        if (Constant.TASK_STUDENT_FINISHED == type) {
            finishedView.setVisibility(View.VISIBLE);
            unfinishView.setVisibility(View.GONE);
        } else if (Constant.TASK_STUDENT_UNFINISH == type) {
            finishedView.setVisibility(View.GONE);
            unfinishView.setVisibility(View.VISIBLE);
            unfinishTv.setText("未完成学生:共" + childModels.size() + "人");
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherLookTaskDetailAdapter(getContext(), childModels, type);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initNumberView() {
        scrollView.setVisibility(View.VISIBLE);
        finishedView.setVisibility(View.GONE);
        unfinishView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        if (model.getTongji() != null && model.getTongji().size() > 0) {
            numberView.setData(model.getTongji(), workId, title, Constant.NUMBER_RATE);
        }
    }


    private void setEvent() {
        if (Constant.TASK_STUDENT_FINISHED == type) {
            mAdapter.setOnItemClickListener(this);
        }
        submitTV.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position, View view) {
        TaskTeacherLookStudentModel model = childModels.get(position);
        TeacherLookStudentTaskDetailActivity.start(getContext(), workId, model);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_tv:
                setHttpData();
                break;
            default:
                break;
        }
    }

    private void setHttpData() {
        ArrayList<Integer> ints = new ArrayList<>();
        for (TaskTeacherLookStudentModel model : childModels) {
            ints.add(model.getStudent_id());
        }
        postRemindHomeHttp(JsonUtils.jsonFromObject(ints));
    }

    private void postRemindHomeHttp(String studentIds) {
        List<Integer> workIds = new ArrayList<>();
        workIds.add(workId);
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.notifyParents(studentIds,
                "",
                Constant.MSG_NOTIFICATION + "",
                Constant.NOTIFICATION_REMIND + "",
                JsonUtils.jsonFromObject(workIds), new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        loading.dismiss();
                        TabToast.showMiddleToast(getContext(), "提醒成功");
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        loading.dismiss();
                    }
                });
    }
}
