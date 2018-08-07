package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseClassRoomModel;
import com.huisu.iyoox.entity.base.BaseSendTaskResultModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DateUtils;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.util.SoftKeyBoardListener;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.DateTimeDialog;
import com.huisu.iyoox.views.DialogSelectClass;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 老师填写作业信息并发布
 */
public class TeacherSendTaskActivity extends BaseActivity implements View.OnClickListener {

    private String timuIds;
    private View contentView;
    private ScrollView scrollView;
    private EditText taskNameView;
    private EditText msgEditView;
    private View startTimeView;
    private TextView startTimeTv;
    private View endTimeView;
    private TextView ednTimeTv;
    private View selectClassView;
    private TextView selectClassTv;
    private TextView submitTv;
    private User user;
    private List<ClassRoomModel> models = new ArrayList<>();
    private String classIds;
    private int zhishidianId;
    private Loading loading;

    @Override
    protected void initView() {
        contentView = findViewById(R.id.send_task_laout);
        scrollView = findViewById(R.id.send_task_scroll_view);
        taskNameView = findViewById(R.id.send_task_name_view);
        msgEditView = findViewById(R.id.send_task_msg);
        startTimeView = findViewById(R.id.task_create_start_time_rl);
        startTimeTv = findViewById(R.id.task_create_start_time_tv);
        endTimeView = findViewById(R.id.task_create_end_time_rl);
        ednTimeTv = findViewById(R.id.task_create_end_time_tv);
        selectClassView = findViewById(R.id.task_create_select_class_rl);
        selectClassTv = findViewById(R.id.task_create_select_class_tv);
        submitTv = findViewById(R.id.teacher_select_exercises_submit_tv);
    }

    @Override
    protected void initData() {
        setTitle("作业信息");
        user = UserManager.getInstance().getUser();
        timuIds = getIntent().getStringExtra("timuIds");
        zhishidianId = getIntent().getIntExtra("zhishidianId", Constant.ERROR_CODE);
        ednTimeTv.setText(DateUtils.sdf3.format(new Date(System.currentTimeMillis() +
                3 * 24 * 60 * 60 * 1000)));
        postClassDataHttp();
    }

    private void postClassDataHttp() {
        RequestCenter.teacherClassroomList(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                models.clear();
                BaseClassRoomModel baseClassRoomModel = (BaseClassRoomModel) responseObj;
                if (baseClassRoomModel.data != null && baseClassRoomModel.data.size() > 0) {
                    String classNames = "";
                    ArrayList<Integer> integers = new ArrayList<>();
                    for (int i = 0; i < baseClassRoomModel.data.size(); i++) {
                        ClassRoomModel model = baseClassRoomModel.data.get(i);
                        model.setSelect(true);
                        integers.add(model.getClassroom_id());
                        if (i != baseClassRoomModel.data.size() - 1) {
                            classNames += model.getName() + ",";
                        } else {
                            classNames += model.getName();
                        }
                    }
                    models.addAll(baseClassRoomModel.data);
                    classIds = JsonUtils.jsonFromObject(integers);
                    selectClassTv.setText(classNames);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        });
    }

    @Override
    protected void setEvent() {
        setBack();
        startTimeView.setOnClickListener(this);
        endTimeView.setOnClickListener(this);
        selectClassView.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        msgEditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    taskNameView.setFocusable(false);
                    taskNameView.setFocusableInTouchMode(false);
                }
            }
        });
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (msgEditView.isFocused()) {
                    int v = StringUtils.dp2px(context, 250);
                    contentView.setPadding(0, 0, 0, v);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }

            @Override
            public void keyBoardHide(int height) {
                contentView.setPadding(0, 0, 0, 0);
                taskNameView.setFocusable(true);
                taskNameView.setFocusableInTouchMode(true);
                taskNameView.requestFocus();
            }
        });
    }

    @Override
    public void onClick(View v) {
        DateTimeDialog dialog;
        switch (v.getId()) {
            case R.id.task_create_start_time_rl:
                dialog = new DateTimeDialog(this, System.currentTimeMillis(), DateUtils.getLong
                        (startTimeTv.getText().toString(), "yyyy-MM-dd HH:mm")) {
                    @Override
                    public void result(String time) {
                        super.result(time);
                        startTimeTv.setText(time);
                    }
                };
                dialog.show();
                break;
            case R.id.task_create_end_time_rl:
                long minTime;
                if (TextUtils.isEmpty(startTimeTv.getText().toString())) {
                    minTime = new Date().getTime() + 300000;
                } else {
                    minTime = DateUtils.getLong(startTimeTv.getText().toString()
                            , "yyyy-MM-dd HH:mm") + 300000;
                }
                dialog = new DateTimeDialog(this, minTime, DateUtils.getLong(ednTimeTv.getText()
                        .toString(), "yyyy-MM-dd HH:mm")) {
                    @Override
                    public void result(String time) {
                        super.result(time);
                        ednTimeTv.setText(time);
                    }
                };
                dialog.show();
                break;
            case R.id.task_create_select_class_rl:
                if (models.size() == 0) {
                    return;
                }
                DialogSelectClass selectClass = new DialogSelectClass(context, models) {
                    @Override
                    public void getSelectClassData(String text, String type) {
                        selectClassTv.setText(text);
                        classIds = type;
                    }
                };
                selectClass.show();
                break;
            case R.id.teacher_select_exercises_submit_tv:
                configSubmit();
                break;
            default:
                break;
        }
    }

    private void configSubmit() {
        if (TextUtils.isEmpty(taskNameView.getText().toString())) {
            TabToast.showMiddleToast(context, "请输入作业名称");
            return;
        }
        if (TextUtils.isEmpty(selectClassTv.getText().toString())) {
            TabToast.showMiddleToast(context, "请选择班级");
            return;
        }
        if (!startTimeTv.getText().toString().contains("布置")) {
            String startTime = startTimeTv.getText().toString();
            String endTime = ednTimeTv.getText().toString();
            String timeType = "yyyy-MM-dd HH:mm";
            if (DateUtils.getLong(startTime, timeType) > DateUtils.getLong(endTime, timeType)) {
                TabToast.showMiddleToast(context, "发布时间晚于结束时间");
                return;
            }
        }
        postSendTaskHttp();
    }

    private void postSendTaskHttp() {
        String startTimeString;
        if (!startTimeTv.getText().toString().contains("布置")) {
            startTimeString = startTimeTv.getText().toString();
        } else {
            startTimeString = DateUtils.sdf3.format(new Date(System.currentTimeMillis()));
        }
        int taskType = getIntent().getIntExtra("taskType", Constant.ERROR_CODE);
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.teacherSendTask(user.getUserId(), taskNameView.getText().toString().trim(), classIds, timuIds
                , startTimeString, ednTimeTv.getText().toString().trim()
                , TextUtils.isEmpty(msgEditView.getText().toString().trim()) ? "" : msgEditView.getText().toString().trim()
                , zhishidianId + "", user.getXueke_id() + "", taskType + ""
                , new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        loading.dismiss();
                        BaseSendTaskResultModel baseModel = (BaseSendTaskResultModel) responseObj;
                        TeacherCreateClassResultActivity.start(context, Constant.CREATE_TASK_RESULT, "", baseModel);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        loading.dismiss();
                    }
                });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_send_task;
    }

    public static void start(Context context, String timuIds, int zhishidianId, int taskType) {
        Intent intent = new Intent(context, TeacherSendTaskActivity.class);
        intent.putExtra("timuIds", timuIds);
        intent.putExtra("zhishidianId", zhishidianId);
        intent.putExtra("taskType", taskType);
        ((Activity) context).startActivityForResult(intent, 1);
    }

}
