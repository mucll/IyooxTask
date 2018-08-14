package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.OtherBookListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.OtherBookModel;
import com.huisu.iyoox.entity.OtherBookZhangJieModel;
import com.huisu.iyoox.fragment.base.BaseFragment;

import java.util.ArrayList;

/**
 *
 */
public class OtherBookDetailFragment extends BaseFragment {

    private View view;
    private OtherBookModel model;
    private ArrayList<OtherBookZhangJieModel> models = new ArrayList<>();
    private int type;
    private ExpandableListView mListView;
    private OtherBookListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            model = (OtherBookModel) args.getSerializable("model");
            type = args.getInt("type", Constant.ERROR_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_other_book_detail, container, false);
        }
        initView();
        setEvent();
        return view;
    }

    private void initView() {
        mListView = view.findViewById(R.id.other_book_list_view);
        models.clear();
        models.addAll(model.getZhangjie());
        mAdapter = new OtherBookListAdapter(getContext(), models);
        mListView.setAdapter(mAdapter);
        for (int i = 0; i < models.size(); i++) {
            mListView.expandGroup(i);
        }
    }

    private void setEvent() {

    }
}
