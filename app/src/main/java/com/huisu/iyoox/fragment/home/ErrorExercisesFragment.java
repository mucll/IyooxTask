package com.huisu.iyoox.fragment.home;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.student.StudentWriteExercisesErrorActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseSubjectModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.ZoomOutPageTransformer;

import java.util.ArrayList;

/**
 * 学生错题集Fragment
 */
public class ErrorExercisesFragment extends BaseFragment {
    private View view;
    private ViewPager mVp;
    private View emptyView;
    private User user;
    private ArrayList<SubjectModel> models = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_error_exercises, container, false);
        initView();
        initTab();
        return view;
    }

    /**
     * 当前fragment显示
     */
    @Override
    public void onShow() {
        super.onShow();
        if (user == null) {
            user = UserManager.getInstance().getUser();
        }
        if (user != null) {
            postStudentErrorData(user.getUserId());
        }
    }

    /**
     * 请求学生错题情况
     */
    private void postStudentErrorData(String userId) {
        RequestCenter.getErrorSubject(userId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseSubjectModel baseSubjectModel = (BaseSubjectModel) responseObj;
                if (baseSubjectModel.data != null && baseSubjectModel.data.size() > 0) {
                    models.clear();
                    models.addAll(baseSubjectModel.data);
                    emptyView.setVisibility(View.GONE);

                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                mAdapter = new MyAdapter(getContext(), models);
                mVp.setAdapter(mAdapter);
                //设置预加载数量
                mVp.setOffscreenPageLimit(models.size() - 1);
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void initView() {
        user = UserManager.getInstance().getUser();
        mVp = view.findViewById(R.id.vp_gallery_vp);
        mVp.setOverScrollMode(View.OVER_SCROLL_NEVER);
        emptyView = view.findViewById(R.id.empty_view);
        //控制两幅图之间的间距
        mVp.setPageTransformer(true, new ZoomOutPageTransformer());
        //viewPager左右两边滑动无效的处理
        view.findViewById(R.id.ll_gallery_outer).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mVp.dispatchTouchEvent(motionEvent);
            }
        });
    }

    class MyAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<SubjectModel> subjectModels;

        public MyAdapter(Context context, ArrayList<SubjectModel> subjectModels) {
            this.context = context;
            this.subjectModels = subjectModels;
        }

        @Override
        public int getCount() {
            return subjectModels == null ? 0 : subjectModels.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_img, container, false);
            final SubjectModel subjectModel = models.get(position);
            ImageView img = view.findViewById(R.id.img_item_img);
            img.setImageResource(getSubjectResId(subjectModel.getKemu_id()));
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), StudentWriteExercisesErrorActivity.class);
                    intent.putExtra("subjectId", subjectModel.getKemu_id());
                    intent.putExtra("subjectName", subjectModel.getKemu_name() + "错题");
                    getContext().startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    private int getSubjectResId(int subjectId) {
        switch (subjectId) {
            case Constant.SUBJECT_YUWEN:
                return R.drawable.ctj_subject_chinese;
            case Constant.SUBJECT_SHUXUE:
                return R.drawable.ctj_subject_math;
            case Constant.SUBJECT_ENGLISH:
                return R.drawable.ctj_subject_eng;
            case Constant.SUBJECT_WULI:
                return R.drawable.ctj_subject_physics;
            case Constant.SUBJECT_HUAXUE:
                return R.drawable.ctj_subject_chemical;
            default:
                return R.drawable.ctj_subject_chinese;
        }
    }

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

}
