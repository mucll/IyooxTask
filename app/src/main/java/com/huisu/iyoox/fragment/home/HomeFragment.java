package com.huisu.iyoox.fragment.home;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.LocationIndicatorView;
import com.huisu.iyoox.views.SelectGradeDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 主页fragment框架
 * @date: 18/6/28
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private View view;
    private LocationIndicatorView mTabView;
    private ViewPager mViewPager;
    private TextView studentGradeTv;
    private List<SubjectModel> subjectModels = new ArrayList<>();
    private User user;
    private int selectGradeCode;
    private ArrayList<GradeListModel> gradeListModels = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hone, container, false);
        }
        postHomeData();
        initTab();
        return view;
    }

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取年级列表和教材信息
     */
    private void postHomeData() {
        RequestCenter.indexList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseGradeListModel baseGradeListModel = (BaseGradeListModel) responseObj;
                if (baseGradeListModel.data != null && baseGradeListModel.data.size() > 0) {
                    gradeListModels.clear();
                    gradeListModels.addAll(baseGradeListModel.data);
                    getSelectGradeListModel();
                } else {
                    TabToast.showMiddleToast(getContext(), baseGradeListModel.msg);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                TabToast.showMiddleToast(getContext(), "网络错误");
            }
        });
    }

    /**
     * 根据学生年级 默认的选取科目信息
     */
    private void getSelectGradeListModel() {
        user = UserManager.getInstance().getUser();
        initView();
        initData();
        initPage();
        setEvent();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        studentGradeTv = view.findViewById(R.id.student_grade_tv);
        mTabView = view.findViewById(R.id.fragment_home_tab_view);
        mViewPager = view.findViewById(R.id.fragment_home_page);
        //初始化 学生年级
        studentGradeTv.setText(user.getGradeName());
        selectGradeCode = user.getGrade() - 1;
    }

    /**
     * 初始化 数据
     */
    private void initData() {
        subjectModels.clear();
        subjectModels.addAll(gradeListModels.get(selectGradeCode).getKemuArr());
        for (SubjectModel model : subjectModels) {
            model.setSelect(false);
        }
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setName("首页");
        subjectModels.add(0, subjectModel);
        mTabView.init(subjectModels);
    }

    /**
     * 初始化viewpager
     */
    private void initPage() {
        myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setCurrentItem(0);
        if (gradeListModels.get(selectGradeCode).getKemuArr().size() >= 2) {
            mViewPager.setOffscreenPageLimit(gradeListModels.get(selectGradeCode).getKemuArr().size() - 1);
        }
        BaseFragment baseFragment = fragments.get(0);
        baseFragment.onShow();
    }

    /**
     * 初始化点击时间
     */
    private void setEvent() {
        mViewPager.addOnPageChangeListener(this);
        studentGradeTv.setOnClickListener(this);
        mTabView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(mTabView.getSelection());
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mTabView.setSelection(position);
        BaseFragment baseFragment = fragments.get(position);
        baseFragment.onShow();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_grade_tv:
                SelectGradeDialog gradeDialog = new SelectGradeDialog(getContext(), gradeListModels, selectGradeCode) {
                    @Override
                    public void getGradeType(GradeListModel gradeModel, int gradeCode) {
                        studentGradeTv.setText(gradeModel.getName());
                        HomeFragment.this.selectGradeCode = gradeCode;
                        initData();
                        initPage();
                    }
                };
                break;
            default:
                break;
        }
    }

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    /**
     * 自定义适配器
     */
    class MyPagerAdapter extends FragmentPagerAdapter {


        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment bookFragment;
            if (position == 0) {
                bookFragment = new HomePageFragment();
            } else {
                String gradeId = gradeListModels.get(selectGradeCode).getGrade_id() + "";
                SubjectModel subjectModel = gradeListModels.get(selectGradeCode).getKemuArr().get(position - 1);
                bookFragment = getFragment(gradeId, subjectModel);
            }
            fragments.add(bookFragment);
            return bookFragment;
        }

        @Override
        public int getCount() {
            return subjectModels == null ? 0 : subjectModels.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseFragment baseFragment = (BaseFragment) super.instantiateItem(container, position);
            if (position != 0) {
                GradeListModel tab = gradeListModels.get(selectGradeCode);
                baseFragment.updateArguments(tab.getGrade_id() + "", tab.getKemuArr().get(position - 1));
            }
            return baseFragment;
        }

    }

    private BookFragment getFragment(String gradeId, SubjectModel model) {
        BookFragment fragment = new BookFragment();
        Bundle b = new Bundle();
        b.putString("grade_id", gradeId);
        b.putSerializable("model", model);
        fragment.setArguments(b);
        return fragment;
    }


}
