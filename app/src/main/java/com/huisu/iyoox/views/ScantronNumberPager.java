package com.huisu.iyoox.views;
//ScantronResultAdapter

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.ScantronResultAdapter;
import com.huisu.iyoox.entity.ExercisesModel;

import java.util.ArrayList;

/**
 * Created by dl on 2017/2/8.
 * 答题卡布局view
 */

public class ScantronNumberPager implements AdapterView.OnItemClickListener {
    private final View view;
    //单选
    TextView pagerSelectExTv;
    EbagGridView pagerSelectExEgv;
    LinearLayout pagerSelectExLl;
    private Context context;
    private ScantronResultAdapter mSelectAdapter;
    //选择
    private ArrayList<ExercisesModel> selectList = new ArrayList<>();

    public ScantronNumberPager(Context context, ArrayList<ExercisesModel> exercisesData) {
        this.context = context;
        view = View.inflate(context, R.layout.layout_scantron_number_pager, null);
        selectList.clear();
        selectList.addAll(exercisesData);
        initView();
        initData();
    }

    private void initData() {
        //单选没数据 隐藏布局
        if (selectList.size() == 0) {
            pagerSelectExLl.setVisibility(View.GONE);
        } else {
            mSelectAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        //选择题
        pagerSelectExTv = view.findViewById(R.id.pager_select_ex_tv);
        pagerSelectExEgv = view.findViewById(R.id.pager_select_ex_egv);
        pagerSelectExLl = view.findViewById(R.id.pager_select_ex_ll);

        mSelectAdapter = new ScantronResultAdapter(context, selectList);
        pagerSelectExEgv.setAdapter(mSelectAdapter);
        pagerSelectExEgv.setOnItemClickListener(this);

        LinearLayout content = view.findViewById(R.id.content_layout);
        Button submit = view.findViewById(R.id.submit);
        final ImageButton closed = view.findViewById(R.id.image_card_closed);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closed();
            }
        });
    }

    public View getView() {
        return view;
    }

    public void getPositionClick(int position) {

    }

    public void submit() {

    }

    public void closed() {

    }

    @Override

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.pager_select_ex_egv:
                getPositionClick(position);
                break;
        }
    }
}
