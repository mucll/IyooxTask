package com.huisu.iyoox.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.ClassRankingListAdapter;
import com.huisu.iyoox.entity.StudentRankingModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.views.WrapContentHeightViewPager;

/**
 * 班级Fragment里面 学生排名
 */
public class ClassRankingFragment extends BaseFragment {


    private int position;
    private WrapContentHeightViewPager mViewPager;

    public ClassRankingFragment() {
        super();
    }

    public ClassRankingFragment(WrapContentHeightViewPager mViewPager, int position) {
        this.position = position;
        this.mViewPager = mViewPager;
    }

    private View view;
    private StudentRankingModel model;
    private ListView mListView;
    private ClassRankingListAdapter mAdapter;
    private TextView zhishi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            model = (StudentRankingModel) bundle.getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class_ranking, container, false);
        mViewPager.setObjectForPosition(view, position);
        initView();
        return view;
    }

    private void initView() {
        if (model == null) {
            return;
        }
        mListView = view.findViewById(R.id.item_class_ranking_listview);
        zhishi = view.findViewById(R.id.class_ranking_zhishidian_name_tv);
        zhishi.setText(TextUtils.isEmpty(model.getZhishidian_name()) ? "" : model.getZhishidian_name());
        mAdapter = new ClassRankingListAdapter(getContext(), model.getStudent_list());
        mListView.setAdapter(mAdapter);
    }

}
