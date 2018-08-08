package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.NewHomePageFragmentAdapter;
import com.huisu.iyoox.fragment.AboutUsFragment;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;

/**
 * @author: dl
 * @function: 主页fragment
 * @date: 18/6/28
 */
public class NewHomePageFragment extends BaseFragment implements OnLoadMoreListener, RadioGroup.OnCheckedChangeListener {

    private View view;
    private ArrayList<Integer> ints = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NewHomePageFragmentAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private boolean init = false;
    private RadioGroup radioGroup;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private FragmentManager fm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_page_new, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!init) {
            initData();
            initView();
            setEvent();
            init = true;
        }
    }

    private void setEvent() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    private void initData() {
        ints.clear();
        for (int i = 0; i < 8; i++) {
            ints.add(i);
        }
    }


    private void initView() {
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
        adapter = new NewHomePageFragmentAdapter(getContext(), ints) {
            @Override
            public void getRadioGroup(RadioGroup radioGroup) {
                NewHomePageFragment.this.radioGroup = radioGroup;
                if (NewHomePageFragment.this.radioGroup != null) {
                    NewHomePageFragment.this.radioGroup.setOnCheckedChangeListener(NewHomePageFragment.this);
                    initFragment();
                }
            }
        };
        mRecyclerView.setAdapter(adapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHead(position) ? manager.getSpanCount() : 1;
            }
        });
    }

    private void initFragment() {
        fm = getActivity().getSupportFragmentManager();
        fragments.clear();
        fragments.add(new AboutUsFragment());
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    public void onLoadMore() {
        closeLoading();
    }

    private void closeLoading() {
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.home_page_new_content_ll, fragments.get(0));
        ft.commit();
    }
}
