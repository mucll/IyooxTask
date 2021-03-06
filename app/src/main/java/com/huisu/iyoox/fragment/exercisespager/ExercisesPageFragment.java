package com.huisu.iyoox.fragment.exercisespager;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.TaskResultActivity;
import com.huisu.iyoox.activity.student.StudentLearningCardActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.constant.EventBusMsg;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.BaseExercisesView;
import com.huisu.iyoox.views.ScantronDialog;
import com.huisu.iyoox.views.TagViewPager;
import com.huisu.iyoox.views.canvasview.CanvasDialog;
import com.huisu.iyoox.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 题目pager
 * @date: 18/6/28
 */
public class ExercisesPageFragment extends BaseFragment implements TagViewPager.OnSelectedListoner, TagViewPager.OnPrimaryItemListener, View.OnClickListener {

    /**
     * 是否可以滑动翻页
     */
    private boolean isSroll = true;
    /**
     * 是否显示解析
     */
    private int showHelp = View.VISIBLE;
    /**
     * 是否可以选择
     */
    private boolean setEnable = true;

    /**
     * 是否解析
     */
    private boolean showStudentAnswer = false;

    /**
     * 是否做完后显示解析
     */
    private boolean isResultShowAnalysis = false;

    private View view;
    private TagViewPager mViewPager;
    private ArrayList<ExercisesModel> exercisesData = new ArrayList<>();
    private TextView exCount;
    private TextView currentNumber;
    private TextView zhiShiDianNameTv;
    private ProgressBar mProgressBar;
    private View mActionButton;
    private Button submitBT;
    private String zhishidianName;
    private int type;
    private TextView datiCrad;


    private void initType() {
        switch (type) {
            case Constant.STUDENT_DOING:
                isSroll = true;
                setEnable = true;
                showHelp = View.GONE;
                showStudentAnswer = false;
                isResultShowAnalysis = false;
                if (datiCrad != null) {
                    datiCrad.setVisibility(View.VISIBLE);
                }
                break;
            case Constant.STUDENT_ANALYSIS:
                isSroll = true;
                setEnable = false;
                isResultShowAnalysis = false;
                showHelp = View.VISIBLE;
                showStudentAnswer = true;
                if (datiCrad != null) {
                    datiCrad.setVisibility(View.GONE);
                }
                break;
            case Constant.STUDENT_ERROR_DOING:
                isSroll = true;
                setEnable = true;
                isResultShowAnalysis = true;
                showHelp = View.GONE;
                showStudentAnswer = false;
                if (datiCrad != null) {
                    datiCrad.setVisibility(View.GONE);
                }
                break;
            case Constant.STUDENT_HOME_WORK:
                isSroll = true;
                setEnable = true;
                showHelp = View.GONE;
                showStudentAnswer = false;
                isResultShowAnalysis = false;
                if (datiCrad != null) {
                    datiCrad.setVisibility(View.VISIBLE);
                }
                break;
            case Constant.TEACHER_LOOK_TASK:
                isSroll = true;
                setEnable = false;
                showHelp = View.GONE;
                showStudentAnswer = false;
                isResultShowAnalysis = false;
                if (datiCrad != null) {
                    datiCrad.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<ExercisesModel> models = (List<ExercisesModel>) bundle.getSerializable("exercises_models");
            zhishidianName = bundle.getString("zhishidian_name");
            type = bundle.getInt("type");
            if (models != null && models.size() > 0) {
                exercisesData.clear();
                exercisesData.addAll(models);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_exercises_page, container, false);
        }
        initView();
        initType();
        initData();
        initViewPage();
        setEvent();
        return view;
    }

    private void initData() {
        if (!TextUtils.isEmpty(zhishidianName)) {
            zhiShiDianNameTv.setText(zhishidianName);
        }
        exCount.setText(" / " + exercisesData.size());
        currentNumber.setText("1");
        mProgressBar.setMax(exercisesData.size());
        mProgressBar.setProgress(1);
    }

    private void initViewPage() {
        mViewPager.setAutoNext(false, 0);
        mViewPager.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 0,
                4, 2, 100);
        mViewPager.viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //禁止滑动翻页
                return !isSroll;
            }
        });
        //创建题目视图
        mViewPager.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, final int position) {
                ExercisesModel bean = null;
                if (exercisesData != null && exercisesData.size() > 0) {
                    bean = exercisesData.get(position);
                }
                BaseExercisesView view = BaseExercisesView.getInstence(getActivity(), bean.getType() + "");
                BaseExercisesView.ExerciseCallBack callBack = new BaseExercisesView.ExerciseCallBack() {
                    @Override
                    public void selectAnswer() {
                        if (type != Constant.STUDENT_ERROR_DOING) {
                            setPositions(position + 1);
                        }
                    }
                };
                //回调
                view.setCallBack(callBack);
                //答案模式
                view.resultShowAnalysis(isResultShowAnalysis);
                //数据
                view.init(bean);
                //是否显示解析
                view.showHelp(showHelp);
                //是否可作答
                //错题模式,并作答了 不让再作答
                if (isResultShowAnalysis && bean.getAnswersModel() != null) {
                    view.setAnserEnable(false);
                } else {
                    view.setAnserEnable(setEnable);
                }
                //学生作答后,显示答案对比
                if (showStudentAnswer) {
                    view.showStudentAnswer();
                }
                if (type == Constant.TEACHER_LOOK_TASK) {
                    mActionButton.setVisibility(View.GONE);
                    view.showClassDetailLayout();
                }
                container.addView(view);
                return view;
            }
        });
        mViewPager.setAdapter(exercisesData.size(), 0);
        mViewPager.setOnSelectedListoner(this);
        mViewPager.setOnPrimaryItemListener(this);
    }

    private void initView() {
        mActionButton = view.findViewById(R.id.student_home_work_floating_action_bt);
        mViewPager = view.findViewById(R.id.fragment_exercises_pager_view);
        exCount = view.findViewById(R.id.fragment_exercises_count);
        currentNumber = view.findViewById(R.id.fragment_current_number);
        mProgressBar = view.findViewById(R.id.exercises_progress_bar);
        submitBT = view.findViewById(R.id.submit_bt);
        zhiShiDianNameTv = view.findViewById(R.id.zhishidian_name_tv);
        datiCrad = view.findViewById(R.id.btn_card);
    }

    private void setEvent() {
        mActionButton.setOnClickListener(this);
        datiCrad.setOnClickListener(this);
        submitBT.setOnClickListener(this);
    }


    public void setWriteTypeShowHelp(int visibility) {
        this.showHelp = visibility;
        BaseExercisesView view = (BaseExercisesView) mViewPager.getPrimaryItem();
        if (view != null) {
            view.showHelp(showHelp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_home_work_floating_action_bt:
                CanvasDialog canvasDialog = new CanvasDialog(getContext());
                canvasDialog.show();
                break;
            case R.id.submit_bt:
                judgeStudentSelectAllExercises();
                break;
            case R.id.btn_card:
                showScantron();
                break;
            default:
                break;
        }
    }

    /**
     * 显示答题卡
     */
    private void showScantron() {
        ScantronDialog scantronDialog = new ScantronDialog(getActivity(), exercisesData) {
            @Override
            public void getPosition(int position) {
                mViewPager.setCurrentItem(position);
                dismiss();
            }

            @Override
            public void getSubmit() {
                if (listener != null) {
                    listener.studentAnswerResult(exercisesData);
                }
                dismiss();
            }
        };
        scantronDialog.show();
    }

    /**
     * 判断题目是否做完
     */
    private void judgeStudentSelectAllExercises() {
        for (int i = 0; i < exercisesData.size(); i++) {
            final int j = i;
            if (exercisesData.get(i).getAnswersModel() != null) {
                //判断是否是最后一题
                if (i == exercisesData.size() - 1) {
                    listener.studentAnswerResult(exercisesData);
                }
            } else {
                //尚有未做完的题目
                DialogUtil.show("提示", "尚有未做完的题目,是否确定提交作业?", "继续做题", "确认提交", getActivity(),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (j != exercisesData.size() - 1) {
                                    mViewPager.setCurrentItem(j);
                                }
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.studentAnswerResult(exercisesData);
                            }
                        });
                return;
            }
        }
    }

    /**
     * 翻到第几题
     *
     * @param position
     */
    public void setPositions(int position) {
        mViewPager.setCurrentItem(position);
    }


    @Override
    public void onSelected(int position, View view) {
        //当前页码
        currentNumber.setText(position + 1 + "");
        mProgressBar.setProgress(position + 1);
        if (position + 1 == exercisesData.size() && setEnable && type != Constant.STUDENT_ERROR_DOING) {
            submitBT.setVisibility(View.VISIBLE);
        } else {
            submitBT.setVisibility(View.GONE);
        }
        BaseExercisesView exercisesView = (BaseExercisesView) view;
        if (exercisesView != null) {
            exercisesView.showHelp(showHelp);
        }

    }

    @Override
    public void onPrimaryItem(int position, View view) {
    }

    public interface OnStudentAnswerResultListener {
        void studentAnswerResult(ArrayList<ExercisesModel> exercisesData);
    }

    private OnStudentAnswerResultListener listener;

    public void setOnStudentAnswerResultListener(OnStudentAnswerResultListener listener) {
        this.listener = listener;
    }

}
