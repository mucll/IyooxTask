package com.huisu.iyoox.views;

import android.content.Context;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.util.DateUtils;
import com.huisu.iyoox.util.LogUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

/**
 * Created by zhaojin on 2016/8/12.
 */
public class DateTimeDialog {
    private Context context;
    private long minTime;
    private long currentTime;

    public DateTimeDialog(Context context, long minTime, long currentTime) {
        this.context = context;
        this.minTime = minTime - 60 * 1000;
        this.currentTime = currentTime;
        LogUtil.e("time", "minTime:" + minTime + ",currentTime:" + currentTime);
    }

    public void show() {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String time = DateUtils.format(millseconds, "yyyy-MM-dd HH:mm");
                        result(time);
                    }
                })
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("时间选择")
                .setCyclic(false)
                .setMinMillseconds(minTime)
                .setCurrentMillseconds(currentTime)
                .setMaxMillseconds(System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000))
                .setThemeColor(context.getResources().getColor(R.color.btn_blue_normal))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(context.getResources().getColor(R.color
                        .timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(context.getResources().getColor(R.color.black))
                .setWheelItemTextSize(12)
                .build();
        mDialogAll.show(((BaseActivity) context).getSupportFragmentManager(), "");
    }

    public void result(String time) {
    }
}