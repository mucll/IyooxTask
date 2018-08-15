package com.huisu.iyoox.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.SelectGradeNewAdapter;
import com.huisu.iyoox.entity.GradeListModel;

import java.util.ArrayList;

/**
 * ClassName:CanvasDialog
 */
public class GradeNewDialog extends Dialog implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    private Context context;
    private View view;
    private EbagGridView gridView;
    private SelectGradeNewAdapter mAdapter;
    private ArrayList<GradeListModel> xiaoxue = new ArrayList<>();
    private ArrayList<GradeListModel> zhongxue = new ArrayList<>();
    private ArrayList<GradeListModel> base = new ArrayList<>();
    private final RadioGroup radioGroup;

    public GradeNewDialog(Context context, ArrayList<GradeListModel> gradeListModels, GradeListModel selectModel) {
        super(context, R.style.Transparent6);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.layout_select_grade_new_view, null);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        int i = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, context.getResources().getDisplayMetrics());
        view.setPadding(0, i, 0, 0);
        radioGroup = view.findViewById(R.id.radio_group_grade);
        gridView = view.findViewById(R.id.grade_new_grid_view);
        xiaoxue.clear();
        zhongxue.clear();
        for (GradeListModel model : gradeListModels) {
            if (model.getGrade_id() > 6) {
                zhongxue.add(model);
            } else {
                xiaoxue.add(model);
            }
        }
        int initSelect = selectModel.getGrade_id() > 6 ? 1 : 0;
        if (initSelect == 0) {
            base.addAll(xiaoxue);
        } else {
            base.addAll(zhongxue);
        }
        mAdapter = new SelectGradeNewAdapter(context, base);
        mAdapter.setSelectModel(selectModel);
        gridView.setAdapter(mAdapter);
        ((RadioButton) radioGroup.getChildAt(initSelect)).setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        gridView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GradeListModel model = base.get(position);
        getGradePosition(model);
        dismiss();
    }

    public void getGradePosition(GradeListModel position) {

    }

    @Override
    public void show() {
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.show();
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的0.7
        p.gravity = Gravity.TOP;
        getWindow().setAttributes(p);     //设置生效
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int indexOf = group.indexOfChild(group.findViewById(checkedId));
        switch (indexOf) {
            case 0:
                base.clear();
                base.addAll(xiaoxue);
                mAdapter.notifyDataSetChanged();
                break;
            case 1:
                base.clear();
                base.addAll(zhongxue);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

}
