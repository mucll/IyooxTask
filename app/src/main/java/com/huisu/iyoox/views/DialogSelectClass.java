package com.huisu.iyoox.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.TeacherSendTaskSelectClassAdapter;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.util.TabToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mucll on 2017/12/6.
 */

public class DialogSelectClass extends Dialog implements AdapterView.OnItemClickListener {
    private Context context;
    //文字（A001）、图片（A002）、音频（A003）、视频（A004）
    private List<ClassRoomModel> models;
    private final TextView submit;
    private final TextView finishIv;
    private final EbagListView mListView;
    private final TeacherSendTaskSelectClassAdapter mAdapter;

    public DialogSelectClass(Context context, List<ClassRoomModel> datas) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        this.models = datas;
        setContentView(R.layout.dialog_select_task_classroom_layout);
        submit = findViewById(R.id.dialog_select_class_submit);
        finishIv = findViewById(R.id.dialog_select_cancel);
        mListView = findViewById(R.id.dialog_select_class_list_view);
        setCanceledOnTouchOutside(true);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectMode();
            }
        });
        finishIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mAdapter = new TeacherSendTaskSelectClassAdapter(context, datas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }


    private void getSelectMode() {
        ArrayList<Integer> classIds = new ArrayList<>();
        ArrayList<ClassRoomModel> selectModel = new ArrayList<>();
        String title = "";
        for (int i = 0; i < models.size(); i++) {
            ClassRoomModel model = models.get(i);
            if (model.isSelect()) {
                classIds.add(model.getClassroom_id());
                selectModel.add(model);
            }
        }
        if (classIds.size() == 0) {
            TabToast.showMiddleToast(context, "请最少选择一个班级");
            return;
        }
        for (int i = 0; i < selectModel.size(); i++) {
            ClassRoomModel model = selectModel.get(i);
            if (i == selectModel.size() - 1) {
                title += model.getName();
            } else {
                title += model.getName() + ",";
            }
        }
        getSelectClassData(title, JsonUtils.jsonFromObject(classIds));
        dismiss();
    }

    @Override
    public void show() {
        super.show();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        WindowManager.LayoutParams localLayoutParams = getWindow()
                .getAttributes();
        localLayoutParams.width = width;
        getWindow().setAttributes(localLayoutParams);
    }

    /**
     * 获取选中mode
     *
     * @param text 显示文字
     * @param type 接口参数
     */
    public void getSelectClassData(String text, String type) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (models != null) {
            ClassRoomModel model = models.get(position);
            model.setSelect(!model.isSelect());
            mAdapter.notifyDataSetChanged();
        }
    }
}
