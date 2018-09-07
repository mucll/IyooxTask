package com.huisu.iyoox.fragment.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.videoplayer.ALiYunGxYsVideoPlayActivity;
import com.huisu.iyoox.adapter.ArtBookListAdapter;
import com.huisu.iyoox.complexmenu.SelectFengMianView;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ArtBookModel;
import com.huisu.iyoox.entity.ArtBookZSDModel;
import com.huisu.iyoox.entity.ArtBookZhangJieModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseArtBookModel;
import com.huisu.iyoox.entity.base.BaseArtBookZSDModel;
import com.huisu.iyoox.entity.base.BaseGuoxueYishuVodModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.views.Loading;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ArtBookFragment extends BaseFragment implements OnRefreshListener, MyOnItemClickListener {

    private View view;
    private int kemuId = Constant.ERROR_CODE;
    private SelectFengMianView typeView;
    private List<ArtBookModel> typeModels = new ArrayList<>();
    private List<ArtBookZSDModel> models = new ArrayList<>();
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView mRecyclerView;
    private ArtBookListAdapter mAdapter;
    private Loading loading;
    private View emptyView;
    private int typeId = Constant.ERROR_CODE;
    private ArtBookZhangJieModel selectModel;
    private int type;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            kemuId = bundle.getInt("kemuId", Constant.ERROR_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_art_book, container, false);
        user = UserManager.getInstance().getUser();
        initView();
        setEvent();
        initData();
        return view;
    }

    /**
     * 设置点击事件
     */
    private void setEvent() {
        typeView.setOnMenuSelectDataChangedListener(new SelectFengMianView.OnMenuSelectDataChangedListener() {
            @Override
            public void onOneChanged(ArtBookZhangJieModel model) {
                if (kemuId == Constant.ERROR_CODE) return;
                typeId = typeModels.get(0).getGrade_id();
                selectModel = model;
                swipeToLoadLayout.setRefreshing(true);
            }

            @Override
            public void onTwoChanged(ArtBookZhangJieModel model) {
                if (kemuId == Constant.ERROR_CODE) return;
                typeId = typeModels.get(1).getGrade_id();
                selectModel = model;
                swipeToLoadLayout.setRefreshing(true);
            }

            @Override
            public void onThreeChanged(ArtBookZhangJieModel model) {
                if (kemuId == Constant.ERROR_CODE) return;
                typeId = typeModels.get(2).getGrade_id();
                selectModel = model;
                swipeToLoadLayout.setRefreshing(true);
            }
        });
        mAdapter.setOnItemClickListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
    }

    /**
     * 初始化
     */
    private void initView() {
        emptyView = view.findViewById(R.id.art_fragment_empty_view);
        typeView = view.findViewById(R.id.art_book_type_view);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new ArtBookListAdapter(getContext(), models);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 根据 学科来请求接口
     */
    private void initData() {
        if (kemuId == Constant.ERROR_CODE) return;
        if (kemuId == Constant.SUBJECT_GUOXUE) {
            type = 1;
            postGuoXueHttp();
        } else if (kemuId == Constant.SUBJECT_YISHU) {
            type = 2;
            postYiShuHttp();
        }
    }

    /**
     * 请求国学分类
     */
    private void postGuoXueHttp() {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.artGuoXueList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                typeModels.clear();
                BaseArtBookModel baseModel = (BaseArtBookModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    typeModels.addAll(baseModel.data);
                    //type
                    typeView.setListData(baseModel.data);
                    //设置默认弹框 国学根据年级来
                    typeView.setSelectLayout(HomeFragment.selectModel.getGrade_id());
//                    postGuoXueDetailHttp(typeModels.get(0).getGrade_id() + "", typeModels.get(0).getZhangjie().get(0).getZhangjie_name());
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    /**
     * 根据国学分类 请求国学详情
     */
    private void postGuoXueDetailHttp(String gradeId, String zhangjie) {
        RequestCenter.guoxueDetail(gradeId, zhangjie, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                swipeToLoadLayout.setRefreshing(false);
                models.clear();
                BaseArtBookZSDModel baseModel = (BaseArtBookZSDModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    models.addAll(baseModel.data);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                swipeToLoadLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 请求艺术分类
     */
    private void postYiShuHttp() {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.yishuList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                typeModels.clear();
                BaseArtBookModel baseModel = (BaseArtBookModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    typeModels.addAll(baseModel.data);
                    //type
                    typeView.setListData(baseModel.data);
                    //设置默认弹框
                    typeView.setSelectLayout(0);
//                    postYiShuDetailHttp(typeModels.get(0).getGrade_id() + "", typeModels.get(0).getZhangjie().get(0).getZhangjie_name());
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    /**
     * 根据艺术分类 来请求艺术详情
     */
    private void postYiShuDetailHttp(String gradeId, String zhangjie) {
        RequestCenter.yishuDetail(gradeId, zhangjie, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                swipeToLoadLayout.setRefreshing(false);
                models.clear();
                BaseArtBookZSDModel baseModel = (BaseArtBookZSDModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    models.addAll(baseModel.data);
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                swipeToLoadLayout.setRefreshing(false);
            }
        });
    }


    @Override

    public void onDetach() {

        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");

            childFragmentManager.setAccessible(true);

            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    public void onRefresh() {
        if (kemuId == Constant.ERROR_CODE || typeId == Constant.ERROR_CODE || selectModel == null) {
            swipeToLoadLayout.setRefreshing(false);
            return;
        }
        if (kemuId == Constant.SUBJECT_GUOXUE) {
            postGuoXueDetailHttp(typeId + "", selectModel.getZhangjie_name());
        } else if (kemuId == Constant.SUBJECT_YISHU) {
            postYiShuDetailHttp(typeId + "", selectModel.getZhangjie_name());
        }
    }

    @Override
    public void onItemClick(int position, View view) {
        ArtBookZSDModel model = models.get(position);
        postVodHttp(model);
    }

    private void postVodHttp(ArtBookZSDModel model) {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.get_guoxue_yishu_vedio(user.getUserId(), model.getZhishidian_id() + "", type + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseGuoxueYishuVodModel baseModel = (BaseGuoxueYishuVodModel) responseObj;
                if (baseModel.data != null) {
                    Intent intent = new Intent(getContext(), ALiYunGxYsVideoPlayActivity.class);
                    intent.putExtra("model", baseModel.data);
                    intent.putExtra("zhangjieName", selectModel.getZhangjie_name());
                    getContext().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }
}
