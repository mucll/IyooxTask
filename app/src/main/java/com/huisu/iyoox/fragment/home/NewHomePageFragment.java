package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
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
import com.huisu.iyoox.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

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
    private ArrayList<Integer> images = new ArrayList<>();
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
        images.clear();
        for (int i = 0; i < 8; i++) {
            ints.add(i);
        }
        for (int i = 0; i < 4; i++) {
            images.add(R.drawable.home_banner2);
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
//                NewHomePageFragment.this.radioGroup = radioGroup;
//                if (NewHomePageFragment.this.radioGroup != null) {
//                    NewHomePageFragment.this.radioGroup.setOnCheckedChangeListener(NewHomePageFragment.this);
//                    initFragment();
//                }
            }

            @Override
            public void setBanner(Banner banner) {
                if (banner == null) return;
                //设置banner样式
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //设置图片加载器
                GlideImageLoader imageLoader = new GlideImageLoader();
                banner.setImageLoader(imageLoader);
                //设置图片集合
                banner.setImages(images);
                //设置banner动画效果
//                banner.setBannerAnimation(Transformer.DepthPage);
                //设置标题集合（当banner样式有显示title时）
//                banner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(5000);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
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
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.home_page_new_content_ll, fragments.get(0));
//        ft.commit();
    }
}
