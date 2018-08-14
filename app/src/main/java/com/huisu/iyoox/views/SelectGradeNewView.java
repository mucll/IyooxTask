package com.huisu.iyoox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.SelectGradeAdapter;
import com.huisu.iyoox.entity.GradeListModel;

import java.util.ArrayList;

public class SelectGradeNewView extends FrameLayout implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Context context;

    private View view;
    private View backView;
    private EbagGridView ebagGridView;
    private ArrayList<GradeListModel> grades = new ArrayList<>();
    private SelectGradeAdapter mAdapter;

    public SelectGradeNewView(Context context) {
        this(context, null);
    }

    public SelectGradeNewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectGradeNewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        view = View.inflate(context, R.layout.layout_select_grade_view, this);
        ebagGridView = view.findViewById(R.id.select_grade_grid_view);
        backView = view.findViewById(R.id.btn_view_back);
        mAdapter = new SelectGradeAdapter(context, grades);
        ebagGridView.setAdapter(mAdapter);
        backView.setOnClickListener(this);
        ebagGridView.setOnItemClickListener(this);
    }

    public void setData(ArrayList<GradeListModel> datas, int selectPosition) {
        if (datas != null && datas.size() > 0) {
            grades.clear();
            grades.addAll(datas);
            mAdapter.setSelectPosition(selectPosition);
            mAdapter.notifyDataSetChanged();
        }
    }


    public void selectChanges(int position) {

    }

    public void back() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectChanges(position);
    }

    @Override
    public void onClick(View v) {
        back();
    }
}
