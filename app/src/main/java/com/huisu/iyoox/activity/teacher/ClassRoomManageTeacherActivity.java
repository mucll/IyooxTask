package com.huisu.iyoox.activity.teacher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.ClassRoomManageTeacherAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.entity.TeacherModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseTeacherModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;

import java.util.ArrayList;

public class ClassRoomManageTeacherActivity extends BaseActivity implements MyOnItemClickListener, View.OnClickListener {

    private TextView configView;
    private RecyclerView recyclerView;
    private ClassRoomModel classRoomModel;
    private ArrayList<TeacherModel> teacherModels = new ArrayList<>();
    private ClassRoomManageTeacherAdapter mAdapter;
    private TextView deleteTv;
    private User user;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        configView = findViewById(R.id.tv_submit);
        configView.setText("编辑");
        deleteTv = findViewById(R.id.delete_teacher_tv);
        recyclerView = findViewById(R.id.teacher_manage_class_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ClassRoomManageTeacherAdapter(context, teacherModels, user.getUser_id());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        classRoomModel = (ClassRoomModel) getIntent().getSerializableExtra("model");
        setTitle("教师管理");
        if (classRoomModel != null) {
            postClassRoomTeacherDetailsHttp();
        }
        if (classRoomModel.getIsadmin() == Constant.IS_ADMIE) {
            configView.setVisibility(View.VISIBLE);
        } else {
            configView.setVisibility(View.GONE);
        }
    }

    private void postClassRoomTeacherDetailsHttp() {
        RequestCenter.classroomTeacherList(classRoomModel.getClassroom_id() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                teacherModels.clear();
                BaseTeacherModel baseTeacherModel = (BaseTeacherModel) responseObj;
                if (baseTeacherModel.data != null && baseTeacherModel.data.size() > 0) {
                    teacherModels.addAll(baseTeacherModel.data);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    protected void setEvent() {
        setBack();
        configView.setOnClickListener(this);
        deleteTv.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (teacherModels.size() == 0) {
                    return;
                }
                setBtnType();
                break;
            case R.id.delete_teacher_tv:
                TabToast.showMiddleToast(this, "该功能暂未实现");
                break;
            default:
                break;
        }
    }

    private void setBtnType() {
        mAdapter.setShowCheck(!mAdapter.getShowCheck());
        if (mAdapter.getShowCheck()) {
            configView.setText("完成");
            deleteTv.setVisibility(View.VISIBLE);
            setTeacherDataType();
        } else {
            configView.setText("编辑");
            deleteTv.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setTeacherDataType() {
        for (TeacherModel teacherModel : teacherModels) {
            teacherModel.setSelect(false);
        }
    }

    @Override
    public void onItemClick(int positions, View view) {
        if (!mAdapter.getShowCheck()) {
            return;
        }
        TeacherModel teacherModel = teacherModels.get(positions);
        if (teacherModel.getTeacher_id() == user.getUser_id()) {
            return;
        }
        teacherModel.setSelect(!teacherModel.isSelect());
        int selectCount = 0;
        for (TeacherModel model : teacherModels) {
            if (model.isSelect()) {
                selectCount++;
            }
        }
        if (selectCount > 0) {
            deleteTv.setEnabled(true);
        } else {
            deleteTv.setEnabled(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_class_room_manage_teacher;
    }

    public static void start(Context context, ClassRoomModel model) {
        Intent intent = new Intent(context, ClassRoomManageTeacherActivity.class);
        intent.putExtra("model", model);
        context.startActivity(intent);
    }

}
