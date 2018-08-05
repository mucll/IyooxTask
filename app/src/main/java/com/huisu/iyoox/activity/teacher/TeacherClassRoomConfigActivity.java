package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherClassRoomConfigActivity extends BaseActivity implements View.OnClickListener {
    public static final int START_CODE = 0x102;

    private ClassRoomModel model;
    private TextView name;
    private TextView id;
    private TextView create;
    private TextView count;
    private TextView delete;
    private SwitchCompat lock;
    private ImageButton backBtn;
    private Loading loading;

    private boolean init = false;
    private User user;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        name = findViewById(R.id.class_name_tv);
        id = findViewById(R.id.class_id_tv);
        create = findViewById(R.id.class_create_time_tv);
        count = findViewById(R.id.class_student_count_tv);
        delete = findViewById(R.id.class_delete_tv);
        lock = findViewById(R.id.class_lock_sc);
        backBtn = findViewById(R.id.menu_back);
    }

    @Override
    protected void initData() {
        setTitle("班级信息");
        model = (ClassRoomModel) getIntent().getSerializableExtra("model");
        if (model != null) {
            name.setText(model.getName());
            id.setText(model.getClassroom_no());
            create.setText(model.getCreate_date());
            count.setText(model.getStudent_num() + "人");
            lock.setChecked(model.getLock() == Constant.CLASS_LOCK ? true : false);
        }
    }

    @Override
    protected void setEvent() {
        delete.setOnClickListener(this);
        lock.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_class_room_config;
    }

    public static void start(Context context, ClassRoomModel model) {
        Intent intent = new Intent(context, TeacherClassRoomConfigActivity.class);
        intent.putExtra("model", model);
        ((Activity) context).startActivityForResult(intent, START_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.class_delete_tv:
                DialogUtil.show("提示", "确认刪除该班级？删除后无法恢复!", "确认", "取消", (Activity) context,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postDeteleClassHttp();
                            }
                        }, null);
                break;
            case R.id.class_lock_sc:
                if (model.getLock() == Constant.CLASS_LOCK) {
                    postLockClassRoomHttp(Constant.CLASS_UNLOCK);
                } else {
                    DialogUtil.show("确认锁定该班级?", "锁定后将不可再接受新学生!", "确认", "取消", (Activity) context,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postLockClassRoomHttp(Constant.CLASS_LOCK);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lock.setChecked(model.getLock() == Constant.CLASS_LOCK ? true : false);
                                }
                            });
                }
                break;
            case R.id.menu_back:
                if (init) {
                    setResult(RESULT_OK);
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (init) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    /**
     * 删除班级
     */
    private void postDeteleClassHttp() {
        loading.show();
        RequestCenter.deleteClassroom(user.getUserId(), model.getClassroom_id() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (Constant.POST_SUCCESS_CODE == code) {
                        TabToast.showMiddleToast(TeacherClassRoomConfigActivity.this, "刪除班級成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }


    /**
     * 锁定班级
     *
     * @param type
     */
    private void postLockClassRoomHttp(final int type) {
        loading = Loading.show(null, context, context.getString(R.string.loading_one_hint_text));
        RequestCenter.teacherLockClassRoom(model.getClassroom_id() + "", type + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (code == Constant.POST_SUCCESS_CODE) {
                        init = true;
                        if (Constant.CLASS_LOCK == type) {
                            TabToast.showMiddleToast(context, "班级已锁定");
                            model.setLock(Constant.CLASS_LOCK);
                        } else {
                            TabToast.showMiddleToast(context, "班级解除锁定");
                            model.setLock(Constant.CLASS_UNLOCK);
                        }
                    } else {
                        lock.setChecked(model.getLock() == Constant.CLASS_LOCK ? true : false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    lock.setChecked(model.getLock() == Constant.CLASS_LOCK ? true : false);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
                lock.setChecked(model.getLock() == Constant.CLASS_LOCK ? true : false);
            }
        });
    }
}
