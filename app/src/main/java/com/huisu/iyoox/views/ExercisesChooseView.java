package com.huisu.iyoox.views;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.ExercisesClassDetailAdapter;
import com.huisu.iyoox.entity.ExercisesChooseModel;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.StudentAnswersModel;
import com.huisu.iyoox.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Function: 单选题展示
 */
public class ExercisesChooseView extends BaseExercisesView {
    Context context;
    View view;
    @Bind(R.id.option_layout)
    LinearLayout option_layout;
    @Bind(R.id.analysis_html_text_view)
    HtmlTextView analysisView;
    @Bind(R.id.daan_html_text_view)
    HtmlTextView daanView;
    @Bind(R.id.help_layout)
    LinearLayout helpLayout;
    @Bind(R.id.content_webview)
    HtmlTextView contentWebview;
    @Bind(R.id.exercises_correct_ll)
    LinearLayout exercisesCorrectLayout;
    @Bind(R.id.exercises_correct_tv)
    TextView exercisesCorrectTv;
    @Bind(R.id.exercises_correct_egv)
    EbagGridView exercisesCorrectEgv;
    @Bind(R.id.exercises_wrong_ll)
    LinearLayout exercisesWrongLayout;
    @Bind(R.id.exercises_wrong_tv)
    TextView exercisesWrongTv;
    @Bind(R.id.exercises_wrong_egv)
    EbagGridView exercisesWrongEgv;
    @Bind(R.id.exercises_class_details_ll)
    LinearLayout classDetailLayout;


    ExercisesModel bean;

    Handler hd = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            selectAnswer();
        }
    };

    private boolean isResultShowAnalysis;

    /**
     * 选项view的集合
     */
    private List<View> chooseLists = new ArrayList<>();
    private ExercisesClassDetailAdapter correctAdapter;
    private ExercisesClassDetailAdapter wrongAdapter;

    public ExercisesChooseView(Context context) {
        this(context, null);
    }

    public ExercisesChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * @param info 习题对象
     */
    @Override
    public void init(final ExercisesModel info) {
        this.bean = info;
        view = View.inflate(context, R.layout.layout_exercise_choose_view, null);
        ButterKnife.bind(this, view);
        //设置题目解析数据
        String analysisContent = "【解析】" + info.getJiexi();
        if (!TextUtils.isEmpty(analysisContent)) {
            analysisView.setHtmlFromString(analysisContent, false);
            if (isImageClick) {
                analysisView.setImageClick();
            }
        } else {
            analysisView.setHtmlFromString("", false);
        }        //设置题目解析数据
        String daanText = "【答案】" + info.getDaan();
        if (!TextUtils.isEmpty(analysisContent)) {
            daanView.setHtmlFromString(daanText, false);
        } else {
            daanView.setHtmlFromString("", false);
        }
        StringBuilder sb = new StringBuilder();
        //自动换行
        sb.append(bean.getTigan());
        String s = sb.toString().replaceAll("style='max-width:100%'", "style='max-width:100%;height:auto;'");
        contentWebview.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(s)) {
            contentWebview.setHtmlFromString(s, false);
        } else {
            contentWebview.setHtmlFromString("", false);
        }
        if (bean.getOption_One() != null) {
            ExercisesChooseModel mData = bean.getOption_One();
//        填充选项
            if (!TextUtils.isEmpty(mData.getA())) {
                setChooseData("A", mData.getA());
            }
            if (!TextUtils.isEmpty(mData.getB())) {
                setChooseData("B", mData.getB());
            }
            if (!TextUtils.isEmpty(mData.getC())) {
                setChooseData("C", mData.getC());
            }
            if (!TextUtils.isEmpty(mData.getD())) {
                setChooseData("D", mData.getD());
            }
        }
        this.addView(view);
    }

    /**
     * 初始化选项
     *
     * @param key
     * @param value
     */
    private void setChooseData(String key, String value) {
        View exercisesView = View.inflate(context, R.layout.item_exercises_choose_view, null);
        AppCompatCheckBox checkBox = exercisesView.findViewById(R.id.select_checkbox);
        HtmlTextView htmlTextView = exercisesView.findViewById(R.id.answer_content);
        View content = exercisesView.findViewById(R.id.item_choose_content_view);
        checkBox.setText(key);
        htmlTextView.setHtmlFromString(value, false);
        //学生做题时,看是否已选择选项
        if (bean.getAnswersModel() != null && key.equals(bean.getAnswersModel().getChooseAnswer())) {
            checkBox.setChecked(true);
            content.setSelected(true);
        }
        chooseLists.add(exercisesView);
        option_layout.addView(exercisesView);
    }

    @Override
    public void showStudentAnswer() {
        StudentAnswersModel model = bean.getAnswersModel();
        if (model == null) {
            return;
        }
        for (int i = 0; i < chooseLists.size(); i++) {
            View view = chooseLists.get(i);
            AppCompatCheckBox checkBox = view.findViewById(R.id.select_checkbox);
            View content = view.findViewById(R.id.item_choose_content_view);
            //判断学生选择的是否正确
            if (getSelectString(i).equals(model.getChooseAnswer())) {
                //正确
                if (model.getChooseAnswer().equals(bean.getDaan())) {
                    checkBox.setText("");
                    checkBox.setBackground(context.getResources().getDrawable(R.drawable.answer_right));
                    content.setBackground(context.getResources().getDrawable(R.drawable.shape_r4_stoke_maincolor));
                } else {
                    //错误
                    checkBox.setText("");
                    checkBox.setBackground(context.getResources().getDrawable(R.drawable.answer_fault));
                    content.setBackground(context.getResources().getDrawable(R.drawable.shape_r4_stoke_error_exercises));
                }
            }

            if (getSelectString(i).equals(bean.getDaan())) {
                checkBox.setText("");
                checkBox.setBackground(context.getResources().getDrawable(R.drawable.answer_right));
                content.setBackground(context.getResources().getDrawable(R.drawable.shape_r4_stoke_maincolor));
            }
        }
    }

    @Override
    public void showClassDetailLayout() {
        classDetailLayout.setVisibility(View.VISIBLE);
        if (bean.getCorrect_list() != null && bean.getCorrect_list().size() > 0) {
            exercisesCorrectLayout.setVisibility(View.VISIBLE);
            exercisesCorrectTv.setText("解答正确的学生(共" + bean.getCorrect_list().size() + "人)");
            correctAdapter = new ExercisesClassDetailAdapter(context, bean.getCorrect_list());
            exercisesCorrectEgv.setAdapter(correctAdapter);
        }
        if (bean.getWrong_list() != null && bean.getWrong_list().size() > 0) {
            exercisesWrongLayout.setVisibility(View.VISIBLE);
            exercisesWrongTv.setText("解答错误的学生(共" + bean.getWrong_list().size() + "人)");
            wrongAdapter = new ExercisesClassDetailAdapter(context, bean.getWrong_list());
            exercisesWrongEgv.setAdapter(wrongAdapter);
        }
    }

    private String getSelectString(int position) {
        switch (position) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            default:
                return "";
        }
    }


    /**
     * 初始化学生的答案
     */
    @Override
    public void initMyAnswer() {
        if (bean == null) {
            return;
        }
//        String stuAnswer = TextUtils.isEmpty(bean.getStudentAnswer()) ? bean.getSelect_answer() :
//                info.getStudentAnswer();//有些接口返回字段StudentAnswer  有些返回Select_answer，所以都取下
//        answerMy.setText(TextUtils.isEmpty(stuAnswer) ? "未作答" : bean
//                .getStudentAnswer());
//        String s = context.getResources().getString(R.string.tea_submit_number_tv);
//        exercisesScore.setText(String.format(s, StringUtils.formatFloat(bean.getStudentScore()) + "分", bean.getScoreTotalStr() + "分"));
//        try {
//            if (bean.getScoreTotal() != 0 && (bean.getStudentScore() == bean.getScoreTotal())) {//全对
//                answerYesOrNo.setImageResource(R.drawable.icon_answer_rigth);
//            } else if (bean.getScoreTotal() > bean.getStudentScore() && bean.getStudentScore() > 0) {
//                answerYesOrNo.setImageResource(R.drawable.icon_answer_rigth_error);
//            } else {
//                answerYesOrNo.setImageResource(R.drawable.icon_answer_error);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            answerYesOrNo.setImageResource(R.drawable.icon_answer_rigth);
//        }
    }

    /**
     * 显示解析
     */
    @Override
    public void showHelp(int visible) {
        helpLayout.setVisibility(visible);
        if (bean.getAnswersModel() != null && isResultShowAnalysis) {
            helpLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void resultShowAnalysis(boolean isResultShowAnalysis) {
        this.isResultShowAnalysis = isResultShowAnalysis;
    }

    /**
     * @param enable 是否可以作答
     */
    @Override
    public void setAnserEnable(boolean enable) {
        if (enable) {
            //遍历选项集合设置点击事件
            for (int i = 0; i < chooseLists.size(); i++) {
                final int j = i;
                View view = chooseLists.get(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //遍历选项集合，先都修改为false然后在根据点击的view设置为true
                        for (int a = 0; a < chooseLists.size(); a++) {
                            AppCompatCheckBox checkBox = chooseLists.get(a).findViewById(R.id
                                    .select_checkbox);
                            View content = chooseLists.get(a).findViewById(R.id
                                    .item_choose_content_view);
                            checkBox.setChecked(false);
                            content.setSelected(false);
                            if (a == j) {
                                checkBox.setChecked(true);
                                content.setSelected(true);
                                //学生作的答按对象
                                StudentAnswersModel answer = new StudentAnswersModel();
                                answer.setTiMuId(bean.getTimu_id());
                                answer.setChooseAnswer(checkBox.getText().toString());
                                bean.setAnswersModel(answer);
                            }
                        }
                        //做错题的时候,显示分析
                        if (isResultShowAnalysis && helpLayout.getVisibility() == View.GONE) {
                            helpLayout.setVisibility(View.VISIBLE);
                        }
                        hd.postDelayed(runnable, 200);
                    }
                });
            }
        }
    }

    /**
     * 选择答案//可以重写监听用开自动翻页到下题
     */
    @Override
    protected void selectAnswer() {
        super.selectAnswer();
    }

    private void showResourceString(int id, String abc, BigDecimal bigDecimal, TextView view) {
        double percent = bigDecimal.doubleValue() * 100;
        String s = StringUtils.formatFloat(percent);
        view.setText(String.format(context.getResources().getString(id), abc, s + "%"));
    }
}
