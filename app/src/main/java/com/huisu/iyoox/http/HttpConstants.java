package com.huisu.iyoox.http;

/**
 * @author: qndroid
 * @function: 所有请求相关地址
 * @date: 16/8/12
 */
public class HttpConstants {

    private static final String ROOT_URL = "http://www.sunvke.com/api";

    /**
     * 判断手机号 是否注册
     */
    public static String IS_EXISTPHONE = ROOT_URL + "/user/IsExistphone";

    /**
     * 发送验证码
     */
    public static String SEND_MSG_CODE = ROOT_URL + "/sms/send_validate_code";

    /**
     * 验证 验证码
     */
    public static String CHECK_MSG_CODE = ROOT_URL + "/sms/check_validate_code";

    /**
     * 注册
     */
    public static String REGISTER = ROOT_URL + "/user/register";

    /**
     * 设置用户信息
     */
    public static String SET_USER_INFO = ROOT_URL + "/user/set_user_info";

    /**
     * 登陆
     */
    public static String LOGIN = ROOT_URL + "/user/login";

    /**
     * 获取年级列表
     */
    public static String GET_GRADES = ROOT_URL + "/user/get_grades";

    /**
     * 重置密码
     */
    public static String MODIFY_PASSWORD = ROOT_URL + "/user/ModifyPassword";

    /**
     * 首页 年级 和科目 列表
     */
    public static String INDEX_LIST = ROOT_URL + "/zhishidian/index_load";

    /**
     * 首页 教材版本 章节 知识点
     */
    public static String GET_OPTIONLIST = ROOT_URL + "/zhishidian/get_optionlist";

    /**
     * 首页 课程详情
     */
    public static String GET_SHIPINLIST = ROOT_URL + "/zhishidian/zhishidian_list";

    /**
     * 首页  视频链接地址
     */
    public static String GET_VIDEO_TIMU = ROOT_URL + "/zhishidian/get_video_timu";

    /**
     * 获取有错题的科目
     */
    public static String getErrorSubject = ROOT_URL + "/work/get_kemu_by_cuoti";

    /**
     * 获取科目下的错题题目
     */
    public static String getErrorSubjectDetails = ROOT_URL + "/work/search_cuoti";

    /**
     * 获取错题筛选数据
     */
    public static String getscreenErrorListData = ROOT_URL + "/work/get_jiaocai_zhishidian";

    /**
     * 做完题库题目返回报告
     */
    public static String getTaskResultData = ROOT_URL + "/zhishidian/submit_answer";

    /**
     * 学生作业列表
     */
    public static String getStudentTaskListData = ROOT_URL + "/work/get_work_by_student";

    /**
     * 学生作业详情
     */
    public static String getStudentTaskDetails = ROOT_URL + "/work/get_timu_by_work";
    /**
     * 学生作业报告
     */
    public static String getStudentTaskResult = ROOT_URL + "/work/submit_work_back";
    /**
     * 家庭作业报告
     */
    public static String getStudentTaskBaoGao = ROOT_URL + "/work/work_baogao";
    /**
     * 更换个人头像
     */
    public static String updateAvatar = ROOT_URL + "/user/update_avatar";

}


