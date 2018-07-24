package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseAddClassRoomModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.util.TabToast;

/**
 * 添加班级
 */
public class StudentAddClassRoomActivity extends BaseActivity implements View.OnClickListener {

    private TextView tabSubmitTv;
    private EditText editText;
    private User user;

    @Override
    protected void initView() {
        tabSubmitTv = findViewById(R.id.tv_submit);
        tabSubmitTv.setVisibility(View.VISIBLE);
        tabSubmitTv.setText("完成");
        tabSubmitTv.setEnabled(false);
        editText = findViewById(R.id.student_add_class_room_et);
    }

    @Override
    protected void initData() {
        setTitle("添加班级");
    }

    @Override
    protected void setEvent() {
        setBack();
        tabSubmitTv.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {
                    tabSubmitTv.setEnabled(true);
                } else {
                    tabSubmitTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_add_class_room;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentAddClassRoomActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                submitData();
                break;
            default:
                break;
        }
    }

    private void submitData() {
        DialogUtil.show("请确认班级编号ID:" + editText.getText().toString().trim() + "?", "确认后班级将无法更改", "确认", "取消", this,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postAddClassRoomHttp();
                    }
                }, null);
    }

    private void postAddClassRoomHttp() {
        user = UserManager.getInstance().getUser();
        RequestCenter.addClassRoom(user.getUserId(), editText.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseAddClassRoomModel baseAddClassRoomModel = (BaseAddClassRoomModel) responseObj;
                if (baseAddClassRoomModel.data != null) {
                    TabToast.showMiddleToast(StudentAddClassRoomActivity.this, "添加班级成功");
                    user.setClassroom_id(baseAddClassRoomModel.data.getClassroom_id());
                    user.setClassroom_name(baseAddClassRoomModel.data.getClassroom_name());
                    UserManager.getInstance().setUser(user);
                    user.updateAll();
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}
