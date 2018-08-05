package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.HomeExpandableListAdapter;
import com.huisu.iyoox.complexmenu.SelectMenuView;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.BookChapterModel;
import com.huisu.iyoox.entity.BookDetailsModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.VideoGroupModel;
import com.huisu.iyoox.entity.base.BaseBookDetailsModel;
import com.huisu.iyoox.entity.base.BaseVideoModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生首页教材Fragment
 */
public class BookFragment extends BaseFragment implements SelectMenuView.OnMenuSelectDataChangedListener, OnRefreshListener {

    private View view;
    private SubjectModel subjectModel;
    private ExpandableListView mListView;
    private List<BookDetailsModel> bookDetailsModels = new ArrayList<>();
    private HomeExpandableListAdapter mAdapter;
    private SelectMenuView bookTypeView;
    private SwipeToLoadLayout swipeToLoadLayout;

    private String gradeId;
    private String gradeDetailId;
    private final int default_code = 0;
    private ArrayList<VideoGroupModel> videoModels = new ArrayList<>();
    /**
     * 做了调整不做分页 但是参数还是要传
     */
    private int page = 1;
    private View emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            gradeId = args.getString("gradeId");
            gradeDetailId = args.getString("gradeDetailId");
            subjectModel = (SubjectModel) args.getSerializable("model");
        }

    }

    /**
     * 刷新fragment数据
     *
     * @param gradeId 年级ID
     * @param model   科目
     */
    public void updateArguments(String gradeId, String gradeDetailId, SubjectModel model) {
        this.gradeId = gradeId;
        this.gradeDetailId = gradeDetailId;
        this.subjectModel = model;
        this.init = false;
        Bundle args = getArguments();
        if (args != null) {
            args.putString("gradeId", gradeId);
            args.putString("gradeDetailId", gradeDetailId);
            args.putSerializable("model", model);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_book, container, false);
        }
        initView();
        setEvent();
        return view;
    }


    /**
     * 初始化控件
     */
    private void initView() {
        emptyView = view.findViewById(R.id.book_fragment_empty_view);
        mListView = view.findViewById(R.id.swipe_target);
        bookTypeView = view.findViewById(R.id.book_type_view);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mAdapter = new HomeExpandableListAdapter(getContext(), videoModels);
        mListView.setAdapter(mAdapter);
        bookTypeView.dismissAllPopupWindow();
    }

    /**
     * 初始化默认信息
     */
    private void initData() {
        postBookDetailsData();
    }

    /**
     * 请求课本 版本 章节 知识点
     */
    private void postBookDetailsData() {
        RequestCenter.getOptionlist(gradeId, gradeDetailId, subjectModel.getKemu_id() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseBookDetailsModel baseBookDetailsModel = (BaseBookDetailsModel) responseObj;
                if (baseBookDetailsModel.code == Constant.POST_SUCCESS_CODE
                        && baseBookDetailsModel.data != null && baseBookDetailsModel.data.size() > 0) {
                    bookDetailsModels.clear();
                    bookDetailsModels.addAll(baseBookDetailsModel.data);
                    bookTypeView.setEditionData(bookDetailsModels, default_code);
                    swipeToLoadLayout.setRefreshing(true);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        });
    }

    /**
     * 根据 教材 章节  知识点  请求课程详情信息
     *
     * @param jiaoCaiId      教材ID
     * @param gradeDetailsId 上下册 没有传空
     * @param zhangjieId     章节ID
     * @param zhishidian     知识点
     */
    private void postDetailsData(int jiaoCaiId, int gradeDetailsId, int zhangjieId, String zhishidian) {
        mListView.setSelection(0);
        String zhishiId = "全部知识点".equals(zhishidian) ? "" : zhishidian;
        RequestCenter.getShipinlist(gradeId, subjectModel.getKemu_id() + "", jiaoCaiId + "",
                gradeDetailsId + "", zhangjieId + "", zhishiId, page + "",
                new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        swipeToLoadLayout.setLoadingMore(false);
                        swipeToLoadLayout.setRefreshing(false);
                        BaseVideoModel baseVideoModel = (BaseVideoModel) responseObj;
                        if (baseVideoModel.code == Constant.POST_SUCCESS_CODE) {
                            videoModels.clear();
                            if (baseVideoModel.data != null && baseVideoModel.data.size() > 0) {
                                videoModels.addAll(baseVideoModel.data);
                                emptyView.setVisibility(View.GONE);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            mAdapter.notifyDataSetChanged();
                            for (int i = 0; i < videoModels.size(); i++) {
                                mListView.expandGroup(i);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        swipeToLoadLayout.setLoadingMore(false);
                        swipeToLoadLayout.setRefreshing(false);
                    }
                });
    }


    private void setEvent() {
        bookTypeView.setOnMenuSelectDataChangedListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    /**
     * 切换教材版本
     */
    @Override
    public void onEditonChanged(BookDetailsModel editionModel) {
        page = 1;
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(true);
    }

    /**
     * 切换章节
     */
    @Override
    public void onChapterChanged(BookChapterModel chapterModel) {
        page = 1;
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(true);
    }

    /**
     * 切换知识点
     */
    @Override
    public void onKnowledgeChanged(String knowledgeModel) {
        page = 1;
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(true);
    }

    /**
     * viewpager滑动的时候 隐藏SelectMenuView
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (bookTypeView != null) {
                bookTypeView.dismissAllPopupWindow();
            }
            if (mAdapter != null) {
                mAdapter.refreshSelectCode();
            }
        }
    }

    private boolean init = false;

    @Override
    public void onShow() {
        super.onShow();
        if (!init) {
            initData();
            init = true;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        BookDetailsModel editionModel = bookTypeView.getSelectJiaoCaiData();
        BookChapterModel chapterModel = bookTypeView.getSelectZhangJieData();
        if (editionModel == null || chapterModel == null) {
            initData();
        } else {
            postDetailsData(editionModel.getJiaocai_id(), editionModel.getGrade_detail_id(),
                    chapterModel.getZhangjie_id(), bookTypeView.getSelectZhiShiDianData());
        }
    }
}
