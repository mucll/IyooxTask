package com.huisu.iyoox.activity.teacher;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.TeacherClassRoomDetailsAdapter;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.entity.StudentModel;
import com.huisu.iyoox.entity.base.BaseStudentModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 老师端-班级详情界面
 */
public class TeacherClassRoomDetailsActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ClassRoomModel classRoomModel;
    private ArrayList<StudentModel> models = new ArrayList<>();
    private TeacherClassRoomDetailsAdapter mAdapter;
    private View manageView;

    @Override
    protected void initView() {
        manageView = findViewById(R.id.title_bar_iv);
        manageView.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.teacher_class_room_details_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherClassRoomDetailsAdapter(context, models);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        classRoomModel = (ClassRoomModel) getIntent().getSerializableExtra("model");
        if (classRoomModel != null) {
            setTitle(classRoomModel.getName());
            postClassRoomDetailsHttp();
        }
    }

    /**
     * 班级详情
     */
    private void postClassRoomDetailsHttp() {
        RequestCenter.teacherClassroomListDeatail(classRoomModel.getClassroom_id() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                models.clear();
                BaseStudentModel baseStudentModel = (BaseStudentModel) responseObj;
                if (baseStudentModel.data != null && baseStudentModel.data.size() > 0) {
                    models.addAll(baseStudentModel.data);
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
        manageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_iv:
                if (classRoomModel != null) {
                    ClassRoomManageTeacherActivity.start(context, classRoomModel);
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_class_room_details;
    }

    public static void start(Context context, ClassRoomModel model) {
        Intent intent = new Intent(context, TeacherClassRoomDetailsActivity.class);
        intent.putExtra("model", (Serializable) model);
        context.startActivity(intent);
    }

}
