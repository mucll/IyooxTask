package com.huisu.iyoox.views;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.huisu.iyoox.R;

public class TimeCount extends CountDownTimer {
    private TextView tvCode;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);
        this.tvCode = tv;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        tvCode.setBackgroundResource(R.drawable.select_register_code_bg);
        tvCode.setText(millisUntilFinished / 1000 + "秒");
        tvCode.setEnabled(false);
    }

    @Override
    public void onFinish() {
        tvCode.setBackgroundResource(R.drawable.select_register_code_bg);
        tvCode.setText("重新发送");
        tvCode.setEnabled(true);
    }
}
