package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.OtherBookModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.base.BaseOtherBookModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.MyFragmentLayout_line;

import java.util.ArrayList;

/**
 * 国学艺术
 */
public class OtherBookFragment extends BaseFragment {
    private ArrayList<BaseFragment> fragments = new ArrayList();

    private boolean init;

    private View view;
    private SubjectModel subjectModel;
    private ArrayList<OtherBookModel> otherModels = new ArrayList<>();
    private MyFragmentLayout_line myFragmentLayout;
    private Loading loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            subjectModel = (SubjectModel) args.getSerializable("model");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_other_book, container, false);
        }
        initView();
        return view;
    }

    private void initView() {
        myFragmentLayout = view.findViewById(R.id.other_fragment_layout);
    }

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
            myFragmentLayout.setAdapter(fragments, R.layout.tablayout_other_book, 0x132);
        } else if (subjectModel.getKemu_id() == Constant.SUBJECT_YISHU) {
            myFragmentLayout.setAdapter(fragments, R.layout.tablayout_other_two_book, 0x131);
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

    @Override
    public void onShow() {
        if (!init) {
            initData();
            init = true;
        }
    }

    private void initData() {
        if (subjectModel == null) return;
        if (subjectModel.getKemu_id() == Constant.SUBJECT_GUOXUE) {
            postGuoXueDataHttp();
        } else if (subjectModel.getKemu_id() == Constant.SUBJECT_YISHU) {
            postYiShuDataHttp();
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


}
