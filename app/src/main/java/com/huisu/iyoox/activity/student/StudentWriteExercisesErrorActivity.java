package com.huisu.iyoox.activity.student;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.ScreenErrorExercisesListActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseExercisesModel;
import com.huisu.iyoox.fragment.exercisespager.ExercisesPageFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.views.Loading;

import java.io.Serializable;
import java.util.List;

/**
 * @author: dl
 * @function: 学生做错题 界面
 * @date: 18/6/28
 */
public class StudentWriteExercisesErrorActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup mRadioGroup;
    private ImageButton screenBt;
    private FragmentManager manager;
    private ExercisesPageFragment fragment;
    private Loading loading;
    private String subjectName;
    private int subjectId;
    private User user;
    private String jiaoCaiId, zhiShiDianId;
    private String time = "1";

    @Override
    protected void initView() {
        mRadioGroup = findViewById(R.id.radio_group_title);
        screenBt = findViewById(R.id.screen_bt);
        manager = getSupportFragmentManager();
    }

    @Override
    protected void initData() {
        user = UserManager.getInstance().getUser();
        subjectName = getIntent().getStringExtra("subjectName");
        subjectId = getIntent().getIntExtra("subjectId", Constant.ERROR_CODE);
        if (subjectId != Constant.ERROR_CODE) {
            postSubjectData();
        }
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    protected void setEvent() {
        setBack();
        mRadioGroup.setOnCheckedChangeListener(this);
        screenBt.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_write_exercises_error;
    }


    private void postSubjectData() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getErrorSubjectDetilas(user.getUserId(), subjectId + "", jiaoCaiId, zhiShiDianId, time, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseExercisesModel baseVideoUrlModel = (BaseExercisesModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
                    if (baseVideoUrlModel.data != null && baseVideoUrlModel.data.size() > 0) {
                        initFragment(baseVideoUrlModel.data, TextUtils.isEmpty(subjectName) ? "" : subjectName);
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    private void initFragment(List<ExercisesModel> timuList, String zhiShiDian) {
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new ExercisesPageFragment();
        Bundle b = new Bundle();
        b.putInt("type", Constant.STUDENT_ERROR_DOING);
        b.putSerializable("exercises_models", (Serializable) timuList);
        b.putString("zhishidian_name", zhiShiDian);
        fragment.setArguments(b);
        transaction.replace(R.id.fragment_content_layout, fragment);
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int indexOf = group.indexOfChild(group.findViewById(checkedId));
        if (fragment != null) {
            fragment.setWriteTypeShowHelp(indexOf == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.screen_bt:
                startScreenActivity();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            time = data.getStringExtra("time");
            jiaoCaiId = data.getStringExtra("version");
            zhiShiDianId = data.getStringExtra("zhishi");
            postSubjectData();
        }
    }

    /**
     * 跳转到筛选界面
     */
    private void startScreenActivity() {
        Intent intent = new Intent(this, ScreenErrorExercisesListActivity.class);
        intent.putExtra("subjectId", subjectId + "");
        intent.putExtra("studentId", user.getUserId() + "");
        startActivityForResult(intent, 100);
    }
}
