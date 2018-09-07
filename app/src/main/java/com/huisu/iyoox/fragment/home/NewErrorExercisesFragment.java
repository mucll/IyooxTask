package com.huisu.iyoox.fragment.home;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.NewErrorExercisesAdapter;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseSubjectModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.ZoomOutPageTransformer;
import com.huisu.iyoox.views.canvasview.CardScaleHelper;

import java.util.ArrayList;

public class NewErrorExercisesFragment extends BaseFragment {

    private View view;
    private User user;
    private ArrayList<SubjectModel> models = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private View emptyView;
    private NewErrorExercisesAdapter mAdapter;
    private CardScaleHelper mCardScaleHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_error_exercises, container, false);
        initView();
        initTab();
        return view;
    }

    private void initView() {
        user = UserManager.getInstance().getUser();
        emptyView = view.findViewById(R.id.empty_view);
        mRecyclerView = view.findViewById(R.id.error_fragment_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new NewErrorExercisesAdapter(getContext(), models);
        mRecyclerView.setAdapter(mAdapter);

    }

    private boolean init = false;

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
                mAdapter.notifyDataSetChanged();
                if (models.size() > 0 && !init) {
                    // mRecyclerView绑定scale效果
                    mCardScaleHelper = new CardScaleHelper();
                    mCardScaleHelper.setCurrentItemPos(0);
                    mCardScaleHelper.attachToRecyclerView(mRecyclerView);
                    init = true;
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
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

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

}
