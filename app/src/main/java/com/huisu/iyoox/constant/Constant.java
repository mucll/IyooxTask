package com.huisu.iyoox.constant;

import android.Manifest;
import android.os.Environment;

/**
 * @author: vision
 * @function:
 * @date: 16/6/16
 */
public class Constant {

    /**
     * 网络请求成功
     */
    public static final int POST_SUCCESS_CODE = 1;
    /**
     * 一些失败的统一code
     */
    public static final int ERROR_CODE = -1;

//----------------------------------密码界面根据type显示不同的界面---------------------------------------
    /**
     * 注册设置密码
     */
    public static final int PASSWORD_REGISTER = 0;
    /**
     * 登陆输入密码
     */
    public static final int PASSWORD_LOGIN = 1;
    /**
     * 重置密码
     */
    public static final int PASSWORD_RESET = 2;

//----------------------------------发送短信的type---------------------------------------
    /**
     * 注册 请求验证码
     */
    public static final String MSG_CODE_REGISTER = "0";
    /**
     * 忘记密码 请求验证码
     */
    public static final String MSG_CODE_FORGET = "3";


//----------------------------------用户身份---------------------------------------
    /**
     * 学生身份
     */
    public static final int STUDENT_TYPE = 1;
    /**
     * 老师身份
     */
    public static final int TEACHER_TYPE = 2;

//----------------------------------学生做题状态或查看状态---------------------------------------
    /**
     * 学生做题
     */
    public static final int STUDENT_DOING = 1;

    /**
     * 解析查看
     */
    public static final int STUDENT_ANALYSIS = 2;

    /**
     * 错题本
     */
    public static final int STUDENT_ERROR_DOING = 3;

    /**
     * 老师布置作业
     */
    public static final int STUDENT_HOME_WORK = 4;

    /**
     * 学生查看已完成的作业
     */
    public static final int STUDENT_TASK_FINISHED = 5;

//----------------------------------科目ID---------------------------------------

    /**
     * 语文ID
     */
    public static final int SUBJECT_YUWEN = 1;

    /**
     * 数学ID
     */
    public static final int SUBJECT_SHUXUE = 2;

    /**
     * 英语ID
     */
    public static final int SUBJECT_ENGLISH = 3;

    /**
     * 物理ID
     */
    public static final int SUBJECT_WULI = 4;

    /**
     * 化学ID
     */
    public static final int SUBJECT_HUAXUE = 5;
    //----------------------------------班级---------------------------------------

    /**
     * 锁定
     */
    public static final int CLASS_LOCK = 1;

    /**
     * 未锁定
     */
    public static final int CLASS_UNLOCK = 0;

    /**
     * 创建班级
     */
    public static final int CLASS_CREATE = 0;

    /**
     * 添加班级
     */
    public static final int CLASS_ADD = 1;

    //----------------------------------结果界面---------------------------------------

    /**
     * 创建班级
     */
    public static final int CREATE_CLASS_RESULT = 0;

    /**
     * 布置作业
     */
    public static final int CREATE_TASK_RESULT = 1;

    //----------------------------------作业类型---------------------------------------

    /**
     * 一课一练
     */
    public static final int TASK_YIKE_TYPE = 1;

    /**
     * 试卷
     */
    public static final int TASK_SHIJUAN_TYPE = 2;

    /**
     * 权限常量相关
     */
    public static final int WRITE_READ_EXTERNAL_CODE = 0x01;
    public static final String[] WRITE_READ_EXTERNAL_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int HARDWEAR_CAMERA_CODE = 0x02;
    public static final String[] HARDWEAR_CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};

    //整个应用文件下载保存路径
    public static String APP_PHOTO_DIR = Environment.
            getExternalStorageDirectory().getAbsolutePath().
            concat("/imooc_business/photo/");
}
