package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseClassRoomModel;
import com.huisu.iyoox.entity.base.BaseClassRoomResultModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.util.Const;

public class TeacherCreateClassActivity extends BaseActivity implements View.OnClickListener {
    public static final int START_CODE = 0x105;

    private TextView gradeNameTv;
    private EditText classNameEt;
    private EditText addClassEt;
    private User user;
    private TextView tabSubmitTv;
    private Loading loading;
    private View createView;
    private View addView;
    private int type;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        type = getIntent().getIntExtra("type", Constant.ERROR_CODE);
        tabSubmitTv = findViewById(R.id.tv_submit);
        tabSubmitTv.setText("完成");
        tabSubmitTv.setVisibility(View.VISIBLE);
        tabSubmitTv.setEnabled(false);
        createView = findViewById(R.id.teacher_create_class_ll);
        addView = findViewById(R.id.teacher_add_class_ll);
        gradeNameTv = findViewById(R.id.teacher_grade_name_tv);
        classNameEt = findViewById(R.id.teacher_create_class_name);
        addClassEt = findViewById(R.id.teacher_add_class_et);
        switch (type) {
            case Constant.CLASS_CREATE:
                setTitle("创建班级");
                createView.setVisibility(View.VISIBLE);
                addView.setVisibility(View.GONE);
                gradeNameTv.setText(user.getGradeName());
                break;
            case Constant.CLASS_ADD:
                setTitle("添加班级");
                createView.setVisibility(View.GONE);
                addView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setEvent() {
        setBack();
        tabSubmitTv.setOnClickListener(this);
        classNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    tabSubmitTv.setEnabled(true);
                } else {
                    tabSubmitTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addClassEt.addTextChangedListener(new TextWatcher() {
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
        return R.layout.activity_teacher_create_class;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (type == Constant.CLASS_CREATE) {
                    DialogUtil.show("提示", "请确认班级名字:" + classNameEt.getText().toString().trim() + "?", "确认", "取消", this,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postCreateClassRoomHttp();
                                }
                            }, null);
                } else if (type == Constant.CLASS_ADD) {
                    DialogUtil.show("提示", "请确认班级编号ID:" + addClassEt.getText().toString().trim() + "?", "确认", "取消", this,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postaddClassRoomHttp();
                                }
                            }, null);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 创建班级
     */
    private void postCreateClassRoomHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.teacherCreateClassroom(user.getUserId(), user.getGrade() + "", classNameEt.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseClassRoomResultModel baseClassRoomModel = (BaseClassRoomResultModel) responseObj;
                if (baseClassRoomModel.data != null) {
                    TeacherCreateClassResultActivity.start(context, Constant.CREATE_CLASS_RESULT, baseClassRoomModel.data.getClassroom_no(), null);
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    /**
     * 添加班级
     */
    private void postaddClassRoomHttp() {
        RequestCenter.joinClassroom(user.getUserId(), addClassEt.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (Constant.POST_SUCCESS_CODE == code) {
                        TabToast.showMiddleToast(context, "班级添加成功");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        String data = jsonObject.getString("data");
                        if (!TextUtils.isEmpty(data)) {
                            TabToast.showMiddleToast(context, data);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, TeacherCreateClassActivity.class);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, START_CODE);
    }
}
