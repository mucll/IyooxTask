package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherRemarkActivity extends BaseActivity implements View.OnClickListener {

    private EditText msgEt;
    private TextView submitTv;
    private int workId;
    private int studentId;

    @Override
    protected void initView() {
        msgEt = findViewById(R.id.teacher_send_msg_et);
        submitTv = findViewById(R.id.submit_tv);
    }

    @Override
    protected void initData() {
        setTitle("教师点评");
        workId = getIntent().getIntExtra("workId", Constant.ERROR_CODE);
        studentId = getIntent().getIntExtra("studentId", Constant.ERROR_CODE);
    }

    @Override
    protected void setEvent() {
        setBack();
        submitTv.setOnClickListener(this);
        msgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    submitTv.setEnabled(true);
                } else {
                    submitTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_remark;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_tv:
                postHttp();
                break;
            default:
                break;
        }
    }

    private void postHttp() {
        if (workId != Constant.ERROR_CODE && studentId != Constant.ERROR_CODE) {
            RequestCenter.dianpingStudentWork(workId + "", studentId + "", msgEt.getText().toString().trim(), new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    JSONObject jsonObject = (JSONObject) responseObj;
                    try {
                        int code = jsonObject.getInt("code");
                        if (code == Constant.POST_SUCCESS_CODE) {
                            TeacherRemarkResultActivity.start(TeacherRemarkActivity.this, studentId,workId, msgEt.getText().toString().trim());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Object reasonObj) {

                }
            });
        } else {
            TabToast.showMiddleToast(context, "数据发送错误");
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public static void start(Context context, int workId, int studentId) {
        Intent intent = new Intent(context, TeacherRemarkActivity.class);
        intent.putExtra("workId", workId);
        intent.putExtra("studentId", studentId);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
