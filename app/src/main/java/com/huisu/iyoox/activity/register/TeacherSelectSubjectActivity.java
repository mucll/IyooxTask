package com.huisu.iyoox.activity.register;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.TeacherSelectSubjectAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseSubjectModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.EbagGridView;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 老师注册选择授课科目
 */
public class TeacherSelectSubjectActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private EbagGridView mGridView;
    private List<SubjectModel> models = new ArrayList<>();
    private TeacherSelectSubjectAdapter mAdapter;
    private int subjectId;
    private int type;
    private User user;
    private Loading loading;

    @Override
    protected void initView() {
        mGridView = findViewById(R.id.teacher_select_subject_grid_view);
        mAdapter = new TeacherSelectSubjectAdapter(context, models);
        mGridView.setAdapter(mAdapter);
    }


    @Override
    protected void initData() {
        setTitle("选择科目");
        user = UserManager.getInstance().getUser();
        String gradeId = getIntent().getStringExtra("gradeId");
        subjectId = getIntent().getIntExtra("subjectId", 0);
        type = getIntent().getIntExtra("type", Constant.ERROR_CODE);
        switch (type) {
            case Constant.USER_LOOK:
                postSubjectHttp(gradeId);
                break;
            case Constant.USER_ALTER:
                postSubjectHttp(user.getGrade() + "");
                break;
            default:
                break;
        }

    }

    private void postSubjectHttp(String gradeId) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.GET_KEMU_BY_GRADE(gradeId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
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
                loading.dismiss();
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

    public static void start(Context context, int gradeId, int subjectId, int type) {
        Intent intent = new Intent(context, TeacherSelectSubjectActivity.class);
        intent.putExtra("gradeId", gradeId + "");
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SubjectModel model = models.get(position);
        switch (type) {
            case Constant.USER_LOOK:
                Intent intent = new Intent();
                intent.putExtra("model", model);
                intent.putExtra("subjectId", model.getKemu_id());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case Constant.USER_ALTER:
                postConfigHttp(model.getKemu_id());
                break;
            default:
                break;
        }
    }

    private void postConfigHttp(final int kemu_id) {
        if (user != null && !TextUtils.isEmpty(user.getUserId())) {
            loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
            RequestCenter.modifyTeacherInfo(user.getUserId(), "", kemu_id + "", "", "", "", new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    loading.dismiss();
                    TabToast.showMiddleToast(context, "修改科目成功");
                    user.setXueke_id(kemu_id);
                    user.updateAll();
                    UserManager.getInstance().setUser(user);
                    finish();
                }

                @Override
                public void onFailure(Object reasonObj) {
                    loading.dismiss();
                }
            });

        }
    }
}
