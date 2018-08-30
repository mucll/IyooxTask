package com.huisu.iyoox.fragment.student;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.TaskResultActivity;
import com.huisu.iyoox.activity.student.StudentHomeWorkReportActivity;
import com.huisu.iyoox.activity.student.TaskStudentHomeWorkActivity;
import com.huisu.iyoox.adapter.StudentTaskListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TaskStudentListModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseHomeWorkResultModel;
import com.huisu.iyoox.entity.base.BaseTaskResultModel;
import com.huisu.iyoox.entity.base.BaseTaskStudentListModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.TaskEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @author: dl
 * @function: 学生作业列表fragment
 * @date: 18/6/28
 */
public class StudentTaskListFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener, AdapterView.OnItemClickListener {

    private View view;
    private StudentTaskListAdapter mAdapter;
    private ListView mListView;
    private String taskType;
    private boolean init;
    private int page = 1;
    private SwipeToLoadLayout swipeToLoadLayout;
    private User user;
    private View emptyView;
    private ImageView emptyIcon;
    private TextView emptyTv;
    private ArrayList<TaskStudentListModel> listModels = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskType = bundle.getString("task_type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_task_list, container, false);
        initView();
        setEvent();
        EventBus.getDefault().register(this);
        return view;
    }

    private void setEvent() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onShow() {
        if (!init) {
            if (swipeToLoadLayout != null) {
                swipeToLoadLayout.setRefreshing(true);
            }
            init = true;
        }
    }

    /**
     * 获取学生作业列表
     */
    private void postTaskListData() {
        RequestCenter.getStudentTaskListData(user.getUserId(), taskType, page + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                closeLoading();
                if (page == 1) {
                    listModels.clear();
                }
                BaseTaskStudentListModel baseTaskStudentListModel = (BaseTaskStudentListModel) responseObj;
                if (baseTaskStudentListModel.data != null && baseTaskStudentListModel.data.size() > 0) {
                    setEmptyView(View.GONE, taskType);
                    listModels.addAll(baseTaskStudentListModel.data);
                } else {
                    if (page != 1) {
                        page--;
                        TabToast.showMiddleToast(getContext(), "暂无更多数据");
                    } else {
                        setEmptyView(View.VISIBLE, taskType);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                closeLoading();
                setEmptyView(View.VISIBLE, taskType);
            }
        });
    }

    private void closeLoading() {
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setLoadingMore(false);
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    private void initView() {
        user = UserManager.getInstance().getUser();
        mListView = view.findViewById(R.id.swipe_target);
        emptyView = view.findViewById(R.id.task_empty_view);
        emptyIcon = view.findViewById(R.id.task_empty_view_icon);
        emptyTv = view.findViewById(R.id.task_empty_view_tv);
        mAdapter = new StudentTaskListAdapter(getContext(), listModels, taskType);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mListView.setAdapter(mAdapter);
        if (listModels != null && listModels.size() > 0) {
            setEmptyView(View.GONE, taskType);
        } else {
            setEmptyView(View.VISIBLE, taskType);
        }
    }

    /**
     * 上拉更多
     */
    @Override
    public void onLoadMore() {
        page++;
        postTaskListData();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        postTaskListData();
    }

    /**
     * listview点击
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskStudentListModel model = listModels.get(position);
        //未完成
        if (TaskStatus.UNFINISH.equals(taskType)) {
            Intent intent = new Intent(getContext(), TaskStudentHomeWorkActivity.class);
            intent.putExtra("type", Constant.STUDENT_HOME_WORK);
            intent.putExtra("work_id", model.getWork_id() + "");
            intent.putExtra("title", model.getWork_name());
            getContext().startActivity(intent);
        } else if (TaskStatus.FINISH.equals(taskType)) {
            //已完成
            studentHomeWorked(model.getWork_id());
        } else if (TaskStatus.YUQI.equals(taskType)) {
            //已完成
        }
    }

    /**
     * 查看已完成作业报告
     */
    private void studentHomeWorked(int workId) {
        RequestCenter.getStudentTaskBaoGao(user.getUserId(), workId + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseHomeWorkResultModel resultModel = (BaseHomeWorkResultModel) responseObj;
                if (resultModel.data != null) {
                    StudentHomeWorkReportActivity.start(getContext(), resultModel.data);
                } else {
                    TabToast.showMiddleToast(getContext(), "老师暂未点评");
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String type) {
        if (!init) {
            init = true;
        }
        swipeToLoadLayout.setRefreshing(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void setEmptyView(int visibility, String taskType) {
        if (visibility == View.GONE) {
            emptyView.setVisibility(View.GONE);
            return;
        }
        emptyView.setVisibility(View.VISIBLE);
        if (TaskStatus.UNFINISH.equals(taskType)) {
            //未完成
            emptyIcon.setImageResource(R.drawable.homework_pic_wu);
            emptyTv.setText("真棒!作业都写完啦");
        } else if (TaskStatus.FINISH.equals(taskType)) {
            //已完成
            emptyIcon.setImageResource(R.drawable.homework_pic_finished_wu);
            emptyTv.setText("是时候去写作业啦");
        } else if (TaskStatus.YUQI.equals(taskType)) {
            //逾期
            emptyIcon.setImageResource(R.drawable.homework_pic_overdue_wu);
            emptyTv.setText("想在逾期里找到我?不存在的");
        }
    }
}
