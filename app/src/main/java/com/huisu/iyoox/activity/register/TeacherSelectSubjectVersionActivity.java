package com.huisu.iyoox.activity.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.TeacherSelectSubjectVersionAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.BookEditionModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseBookEditionModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import java.util.ArrayList;
import java.util.List;

/**
 * 老师注册选择授课科目的版本
 */
public class TeacherSelectSubjectVersionActivity extends BaseActivity implements MyOnItemClickListener {

    private RecyclerView recyclerView;
    private List<BookEditionModel> models = new ArrayList<>();
    private TeacherSelectSubjectVersionAdapter mAdapter;
    private String kemuId;
    private String gradeId;
    private int selectVersionId = 0;
    private int versionDetailId = 0;
    private int type;
    private User user;
    private Loading loading;

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
        user = UserManager.getInstance().getUser();
        type = getIntent().getIntExtra("type", Constant.ERROR_CODE);
        switch (type) {
            case Constant.USER_LOOK:
                kemuId = getIntent().getStringExtra("kemuId");
                gradeId = getIntent().getStringExtra("gradeId");
                selectVersionId = getIntent().getIntExtra("versionId", 0);
                versionDetailId = getIntent().getIntExtra("versionDetailId", 0);
                break;
            case Constant.USER_ALTER:
                kemuId = user.getXueke_id() + "";
                gradeId = user.getGrade() + "";
                selectVersionId = user.getJiaocai_id();
                versionDetailId = user.getGrade_detail_id();
                break;
            default:
                break;
        }
        postVersionHttp();
    }

    private void postVersionHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getBookVersion(kemuId, gradeId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseBookEditionModel baseBookEditionModel = (BaseBookEditionModel) responseObj;
                if (baseBookEditionModel.data != null && baseBookEditionModel.data.size() > 0) {
                    models.clear();
                    models.addAll(baseBookEditionModel.data);
                    mAdapter.setSelectVersionId(selectVersionId, versionDetailId);
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
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_select_subject_version;
    }

    public static void start(Context context, int gradeId, int kemuId, int versionId, int versionDetailId, int type) {
        Intent intent = new Intent(context, TeacherSelectSubjectVersionActivity.class);
        intent.putExtra("kemuId", kemuId + "");
        intent.putExtra("versionId", versionId);
        intent.putExtra("gradeId", gradeId + "");
        intent.putExtra("versionDetailId", versionDetailId);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, 2);
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, TeacherSelectSubjectVersionActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(int positions, View view) {
        BookEditionModel editionModel = models.get(positions);
        switch (type) {
            case Constant.USER_LOOK:
                Intent intent = new Intent();
                intent.putExtra("versionModel", editionModel);
                intent.putExtra("versionId", editionModel.getJiaocai_id());
                intent.putExtra("versionDetailId", editionModel.getGrade_detail_id());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case Constant.USER_ALTER:
                postConfigHttp(editionModel);
                break;
            default:
                break;
        }
    }

    private void postConfigHttp(final BookEditionModel model) {
        if (user != null && !TextUtils.isEmpty(user.getUserId())) {
            loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
            RequestCenter.modifyTeacherInfo(user.getUserId(), "", "",
                    model.getJiaocai_id() + "",
                    model.getGrade_detail_id() + "",
                    "", new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            loading.dismiss();
                            TabToast.showMiddleToast(context, "修改教材版本成功");
                            user.setJiaocai_id(model.getJiaocai_id());
                            user.setGrade_detail_id(model.getGrade_detail_id());
                            user.setJiaocai_name(model.getName());
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
