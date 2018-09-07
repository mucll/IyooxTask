package com.huisu.iyoox.fragment.home;


import android.content.Intent;
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

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.WebActivity;
import com.huisu.iyoox.activity.videoplayer.ALiYunGxYsVideoPlayActivity;
import com.huisu.iyoox.adapter.NewHomePageFragmentAdapter;
import com.huisu.iyoox.entity.GuoxueYishuVodModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.XscVideoModel;
import com.huisu.iyoox.entity.base.BaseGuoxueYishuVodModel;
import com.huisu.iyoox.entity.base.BaseXscVideoModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.util.GlideImageLoader;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 主页fragment
 * @date: 18/6/28
 */
public class NewHomePageFragment extends BaseFragment implements OnLoadMoreListener, RadioGroup.OnCheckedChangeListener, MyOnItemClickListener {

    private View view;
    private ArrayList<Integer> ints = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NewHomePageFragmentAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private boolean init = false;
    private User user;
//    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    /**
     * 轮播图
     */
    private ArrayList<Integer> images = new ArrayList<>();
    private FragmentManager fm;
    private Loading loading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_page_new, container, false);
        }
        user = UserManager.getInstance().getUser();
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

    /**
     * 轮播图
     */
    private void initData() {
        ints.clear();
        images.clear();
        for (int i = 0; i < 8; i++) {
            ints.add(i);
        }
        images.add(R.drawable.home_banner_2);
        images.add(R.drawable.home_banner_3);
        images.add(R.drawable.home_banner_4);
        getModel();
    }


    private void initView() {
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
        adapter = new NewHomePageFragmentAdapter(getContext(), ints) {

            @Override
            public void selectPosition(int position) {
                String id = getVod(position);
                postXscDetailsHttp(id, "小升初专题");
            }

            @Override
            public void setBanner(Banner banner) {
                if (banner == null) return;
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (0 == position) {
                            WebActivity.start(getContext(), "互联网+教育", "http://www.ceeia.cn/news/detail_2702.htm");
                        }
                    }
                });
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
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHead(position) ? manager.getSpanCount() : 1;
            }
        });
    }

    private void postXscDetailsHttp(String id, final String name) {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.get_xiaoshengchu_vedio(user.getUserId(), id, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseGuoxueYishuVodModel baseModel = (BaseGuoxueYishuVodModel) responseObj;
                if (baseModel.data != null) {
                    Intent intent = new Intent(getContext(), ALiYunGxYsVideoPlayActivity.class);
                    intent.putExtra("model", baseModel.data);
                    intent.putExtra("zhangjieName", name);
                    getContext().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }


    @Override
    public void onItemClick(int position, View view) {
        if (position >= 1 && vodModels.size() > 0) {
            GuoxueYishuVodModel model = vodModels.get(position - 1);
            postVodHttp(model.getId() + "", model.getType() + "", model.getName());
        }
    }

    private void postVodHttp(String id, String type, final String name) {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.get_guoxue_yishu_vedio(user.getUserId(), id, type, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseGuoxueYishuVodModel baseModel = (BaseGuoxueYishuVodModel) responseObj;
                if (baseModel.data != null) {
                    Intent intent = new Intent(getContext(), ALiYunGxYsVideoPlayActivity.class);
                    intent.putExtra("model", baseModel.data);
                    intent.putExtra("zhangjieName", name);
                    getContext().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    private XscVideoModel xscVideoModel;

    private void postXSCHttp() {
        RequestCenter.xiaoshengchu(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseXscVideoModel baseModel = (BaseXscVideoModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    NewHomePageFragment.this.xscVideoModel = baseModel.data.get(0);
                }

            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void initFragment() {
//        fm = getActivity().getSupportFragmentManager();
//        fragments.clear();
//        fragments.add(new AboutUsFragment());
//        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
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
    }

    public String getVod(int indexof) {
        switch (indexof) {
            case 0:
                return "19";
            case 1:
                return "12";
            case 2:
                return "82";
            case 3:
                return "52";
            default:
                return "";
        }
    }

    private List<GuoxueYishuVodModel> vodModels = new ArrayList<>();

    private void getModel() {
        vodModels.clear();
        GuoxueYishuVodModel model1 = new GuoxueYishuVodModel();
        model1.setName("钢琴");
        model1.setType(2);
        model1.setId(298);
        GuoxueYishuVodModel model2 = new GuoxueYishuVodModel();
        model2.setName("钢琴");
        model2.setType(2);
        model2.setId(299);
        GuoxueYishuVodModel model3 = new GuoxueYishuVodModel();
        model3.setName("国学");
        model3.setType(1);
        model3.setId(172);
        GuoxueYishuVodModel model4 = new GuoxueYishuVodModel();
        model4.setName("国学");
        model4.setType(1);
        model4.setId(168);
        GuoxueYishuVodModel model5 = new GuoxueYishuVodModel();
        model5.setName("芭蕾");
        model5.setType(2);
        model5.setId(102);
        GuoxueYishuVodModel model6 = new GuoxueYishuVodModel();
        model6.setName("芭蕾");
        model6.setType(2);
        model6.setId(107);
        GuoxueYishuVodModel model7 = new GuoxueYishuVodModel();
        model7.setName("国画");
        model7.setType(2);
        model7.setId(145);
        GuoxueYishuVodModel model8 = new GuoxueYishuVodModel();
        model8.setName("国画");
        model8.setType(2);
        model8.setId(149);

        vodModels.add(model1);
        vodModels.add(model2);
        vodModels.add(model3);
        vodModels.add(model4);
        vodModels.add(model5);
        vodModels.add(model6);
        vodModels.add(model7);
        vodModels.add(model8);
    }


}
