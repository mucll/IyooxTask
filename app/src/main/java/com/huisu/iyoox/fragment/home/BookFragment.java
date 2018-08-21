package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.HomeExpandableListAdapter;
import com.huisu.iyoox.complexmenu.SelectMenuView;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.BookChapterModel;
import com.huisu.iyoox.entity.BookDetailsModel;
import com.huisu.iyoox.entity.OtherBookModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.VideoGroupModel;
import com.huisu.iyoox.entity.base.BaseBookDetailsModel;
import com.huisu.iyoox.entity.base.BaseOtherBookModel;
import com.huisu.iyoox.entity.base.BaseVideoModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.MyFragmentLayout_line;

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
    private ArrayList<OtherBookModel> otherModels = new ArrayList<>();
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
    private Loading loading;

    private MyFragmentLayout_line myFragmentLayout;
    private View bookView;
    private FrameLayout artContentView;

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
        return view;
    }


    /**
     * 初始化控件
     */
    private void initView() {
        artContentView = view.findViewById(R.id.book_Fragment_content_layout);
        artContentView.removeAllViews();
        myFragmentLayout = view.findViewById(R.id.other_fragment_layout);
        myFragmentLayout.removeAllViews();
        videoModels.clear();
        bookView = view.findViewById(R.id.book_fragment_rl);
        if (subjectModel == null) return;
        if (subjectModel.getKemu_id() == Constant.SUBJECT_GUOXUE) {
            artContentView.setVisibility(View.VISIBLE);
            myFragmentLayout.setVisibility(View.GONE);
            bookView.setVisibility(View.GONE);
        } else if (subjectModel.getKemu_id() == Constant.SUBJECT_YISHU) {
            artContentView.setVisibility(View.VISIBLE);
            myFragmentLayout.setVisibility(View.GONE);
            bookView.setVisibility(View.GONE);
        } else {
            bookView.setVisibility(View.VISIBLE);
            myFragmentLayout.setVisibility(View.GONE);
            artContentView.setVisibility(View.GONE);
            emptyView = view.findViewById(R.id.book_fragment_empty_view);
            mListView = view.findViewById(R.id.swipe_target);
            bookTypeView = view.findViewById(R.id.book_type_view);
            swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
            mAdapter = new HomeExpandableListAdapter(getContext(), videoModels);
            mListView.setAdapter(mAdapter);
            bookTypeView.dismissAllPopupWindow();
        }
    }

    /**
     * 初始化默认信息
     */
    private void initData() {
        initView();
        if (subjectModel == null) return;
        if (subjectModel.getKemu_id() == Constant.SUBJECT_GUOXUE) {
//            postGuoXueDataHttp();
            setBaseFragment();
        } else if (subjectModel.getKemu_id() == Constant.SUBJECT_YISHU) {
//            postYiShuDataHttp();
            setBaseFragment();
        } else {
            setEvent();
            postBookDetailsData();
        }
    }

    private void setBaseFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.book_Fragment_content_layout, getFragment(subjectModel.getKemu_id()));
        ft.commit();
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
        bookTypeView.setRefresh(editionModel, default_code);
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

    private void postGuoXueDataHttp() {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.guoxueList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                otherModels.clear();
                loading.dismiss();
                BaseOtherBookModel baseModel = (BaseOtherBookModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    otherModels.addAll(baseModel.data);
                    initFragment();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    private void postYiShuDataHttp() {
        loading = Loading.show(null, getContext(), getString(R.string.loading_one_hint_text));
        RequestCenter.yishuList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                otherModels.clear();
                loading.dismiss();
                BaseOtherBookModel baseModel = (BaseOtherBookModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    otherModels.addAll(baseModel.data);
                    initFragment();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    private ArrayList<BaseFragment> fragments = new ArrayList();

    private void initFragment() {
        fragments.clear();
        if (otherModels.get(Constant.other_book_one) != null) {
            fragments.add(getFragment(Constant.other_book_one, otherModels.get(Constant.other_book_one)));
        }
        if (otherModels.get(Constant.other_book_two) != null) {
            fragments.add(getFragment(Constant.other_book_two, otherModels.get(Constant.other_book_two)));
        }
        if (otherModels.get(Constant.other_book_three) != null) {
            fragments.add(getFragment(Constant.other_book_three, otherModels.get(Constant.other_book_three)));
        }
        myFragmentLayout.setScorllToNext(true);
        myFragmentLayout.setScorll(true);
        myFragmentLayout.setWhereTab(1);
        myFragmentLayout.setTabHeight(6, getResources().getColor(R.color.main_text_color), false);
        myFragmentLayout
                .setOnChangeFragmentListener(new MyFragmentLayout_line.ChangeFragmentListener() {
                    @Override
                    public void change(int lastPosition, int positon,
                                       View lastTabView, View currentTabView) {
                        ((TextView) lastTabView.findViewById(R.id.tab_text))
                                .setTextColor(getResources().getColor(R.color.color333));
                        ((TextView) currentTabView.findViewById(R.id.tab_text))
                                .setTextColor(getResources().getColor(R.color.main_text_color));
                        fragments.get(positon).onShow();
                    }
                });
        if (subjectModel.getKemu_id() == Constant.SUBJECT_GUOXUE) {
            myFragmentLayout.setAdapter(fragments, R.layout.tablayout_other_book, 0x253);
        } else if (subjectModel.getKemu_id() == Constant.SUBJECT_YISHU) {
            myFragmentLayout.setAdapter(fragments, R.layout.tablayout_other_two_book, 0x254);
        }

    }

    private OtherBookDetailFragment getFragment(int taskType, OtherBookModel model) {
        OtherBookDetailFragment fragment = new OtherBookDetailFragment();
        Bundle b = new Bundle();
        b.putInt("type", taskType);
        b.putSerializable("model", model);
        fragment.setArguments(b);
        return fragment;
    }

    private ArtBookFragment getFragment(int kemuId) {
        ArtBookFragment fragment = new ArtBookFragment();
        Bundle b = new Bundle();
        b.putInt("kemuId", kemuId);
        fragment.setArguments(b);
        return fragment;
    }
}
