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

    /**
     * 通知消息
     */
    public static final int MSG_NOTIFICATION = 1;
    /**
     * 系统消息
     */
    public static final int MSG_SYSTEM = 2;
    /**
     * 消息-发作业
     */
    public static final int NOTIFICATION_SEND_TASK = 1;
    /**
     * 消息-点评
     */
    public static final int NOTIFICATION_DP = 2;
    /**
     * 消息-提醒交作业
     */
    public static final int NOTIFICATION_REMIND = 3;


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
    /**
     * 注册
     */
    public static final int USER_LOOK = 1;
    /**
     * 修改
     */
    public static final int USER_ALTER = 2;


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

    /**
     * 老师看班级作业详情
     */
    public static final int TEACHER_LOOK_TASK = 6;

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

    /**
     * 不是班级管理员
     */
    public static final int IS_UN_ADMIE = 0;
    /**
     * 是班级管理员
     */
    public static final int IS_ADMIE = 1;


    //----------------------------------结果界面---------------------------------------

    /**
     * 创建班级
     */
    public static final int CREATE_CLASS_RESULT = 0;

    /**
     * 布置作业
     */
    public static final int CREATE_TASK_RESULT = 1;
    /**
     * 题目number比率显示
     */
    public static final int NUMBER_RATE = 1;
    /**
     * 题目number回答选项显示
     */
    public static final int NUMBER_ANSWER = 2;


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
     * 已发布
     */
    public static final int TASK_SEND_CODE = 1;

    /**
     * 待发布
     */
    public static final int TASK_UNSEND_CODE = 0;

    /**
     * 学生已完成
     */
    public static final int TASK_STUDENT_FINISHED = 1;

    /**
     * 学生未完成
     */
    public static final int TASK_STUDENT_UNFINISH = 2;
    /**
     * 题目统计
     */
    public static final int TASK_EXERCISES_NUMBER = 3;
    /**
     * 回答错误
     */
    public static final int ANSWER_ERROR = 0;
    /**
     * 回答正确
     */
    public static final int ANSWER_CORRECT = 1;


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
