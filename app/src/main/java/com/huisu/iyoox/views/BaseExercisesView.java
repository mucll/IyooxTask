package com.huisu.iyoox.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.huisu.iyoox.Interface.ExercisesType;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.ResQuestionListBean;

import java.io.File;
import java.util.List;

/**
 * Function: 题目父类
 * Date: 2016-08-20
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public abstract class BaseExercisesView extends FrameLayout {
    public static String XIAHUAXIAN = "<img src='file:///android_asset/icon_line.png'/>";
    public boolean showNum = true;
    protected ExerciseCallBack callBack;
    //图片是否可以点击
    boolean isImageClick = false;
    Context context;
    View view;

    public BaseExercisesView(Context context) {
        this(context, null);
    }

    public BaseExercisesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public static BaseExercisesView getInstence(Context context, String type) {
        switch (type) {
            case ExercisesType.SINGLECHOOSE:
                return new ExercisesChooseView(context);
            default:
                return new ExercisesChooseView(context);
        }
    }

    public static String getExercisesTypeString(String type) {
        switch (type) {
            case ExercisesType.SINGLECHOOSE:
                return "选择题";
            default:
                return "选择题";
        }
    }

    public boolean isImageClick() {
        return isImageClick;
    }


    public void setCallBack(ExerciseCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * @param info
     */
    public abstract void init(final ExercisesModel info);

    /**
     * 初始化学生的答案
     */
    public abstract void initMyAnswer();

    /**
     * 显示解析
     */
    public abstract void showHelp(int visible);

    public abstract void resultShowAnalysis(boolean isResultShowAnalysis);

    /**
     * @param enable 是否可以作答
     */
    public abstract void setAnserEnable(boolean enable);

    public abstract void showStudentAnswer();

    /**
     * 选择答案//可以重写监听用开自动翻页到下题
     */
    protected void selectAnswer() {
        if (callBack != null) {
            callBack.selectAnswer();
        }
    }

    public void selectImage(List<File> files) {
    }

    public Bitmap getBitmap() {
        return null;
    }

    public void hideTitleType() {
    }

    public void onShow() {
    }

    public interface ExerciseCallBack {
        public void selectAnswer();
    }
}
