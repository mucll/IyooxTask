package com.huisu.iyoox.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;

/**
 * Created by Mucll on 2016/8/19.
 */
public abstract class EditTextDialog {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_PWD = 2;
    public static final int TYPE_EMAIL = 3;
    public ContainsEmojiEditText editText;
    private Context context;
    private String title;
    private String content;
    private int type;
    private boolean isChina = false;
    private int maxLenght = 0;

    /**
     * @param context
     * @param title   标题
     * @param content 提示内容：请输入XXXX
     * @param type    输入类型
     */
    public EditTextDialog(Context context, String title, String content, int type) {
        this.context = context;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    /**
     * 设置是否只能输入中文
     *
     * @param isChana
     */
    public void isChina(boolean isChana) {
        this.isChina = isChana;
    }

    public int getMaxLenght() {
        return maxLenght;
    }

    /**
     * 设置最大字符，默认没有限制
     *
     * @param maxLenght
     */
    public void setMaxLenght(int maxLenght) {
        this.maxLenght = maxLenght;
    }

    public void show() {
        View view = View.inflate(context, R.layout.dialog_edit_text, null);
        editText = (ContainsEmojiEditText) view.findViewById(R.id.dialog_edit_et);
        if (maxLenght != 0) {//最大输入长度
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLenght)});
        }
        switch (type) {
            case TYPE_PWD:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case TYPE_EMAIL:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
        }
        editText.setHint(content);
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder
                (context, AlertDialog
                        .THEME_HOLO_LIGHT);
        builder.setView(view);
        builder.setTitle(title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = editText.getText().toString().trim();
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(context, title + "不能为空，请重新输入", Toast.LENGTH_SHORT).show();
                    show();
                } else if (isChina && (!StringUtils.isRightName(result))) {
                    TabToast.showTopMsg(context, "姓名以汉子开头,可包括汉子、字母、数字、点号任意组合,且只能是2到10个字符");
                    show();
                } else {
                    result(result);
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        params.width = (int) (BaseActivity.getScreenWidth(((Activity) context)) * 6.0f / 7.0f);
        dialog.getWindow().setAttributes(params);
    }

    public void setText(String text) {
        if (editText != null) {
            editText.setText(text);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        editText.setSelection(editText.getText().length());
                    }
                }
            });
        }
    }

    public abstract void result(String result);
}
