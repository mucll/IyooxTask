package com.huisu.iyoox.http;

/**
 * @author: qndroid
 * @function: 所有请求相关地址
 * @date: 16/8/12
 */
public class HttpConstants {
    //外网
//    private static final String ROOT_URL = "http://www.sunvke.com/api";
    //刘栋
    private static final String ROOT_URL = "http://8jbew0j.hk1.mofasuidao.cn/api";
    //王佳浩
//    private static final String ROOT_URL = "http://mpvbsjh.hk1.mofasuidao.cn/api";

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
     * 老师根据年级获取科目列表
     */
    public static String GET_KEMU_BY_GRADE = ROOT_URL + "/user/get_kemu_by_grade";

    /**
     * 老师根据科目获取教材版本列表
     */
    public static String getBookVersion = ROOT_URL + "/user/get_jiocai_by_kemu";

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
    public static String getVideoData = ROOT_URL + "/zhishidian/get_video";
    /**
     * 首页  视频下的题目
     */
    public static String getVideoTimu = ROOT_URL + "/zhishidian/get_timu_by_video";

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
    /**
     * 个人信息
     */
    public static String userInfo = ROOT_URL + "/user/user_info";
    /**
     * 获取班级信息和个人排名
     */
    public static String getClassRanking = ROOT_URL + "/classroom/get_classroom_by_student";

    /**
     * 学生添加班级
     */
    public static String addClassRoom = ROOT_URL + "/classroom/insert_classroom";

    /**
     * 获取激活码
     */
    public static String getJiHuoCode = ROOT_URL + "/jihuoma/get_jihuoma";
    /**
     * 激活 激活码
     */
    public static String getStudentBindCard = ROOT_URL + "/jihuoma/jihuoma_bind_grade";
    /**
     * 老师创建班级
     */
    public static String teacherCreateClassroom = ROOT_URL + "/classroom/create_classroom";
    /**
     * 老师获取班级列表
     */
    public static String teacherClassroomList = ROOT_URL + "/classroom/my_classroom_list";
    /**
     * 老师锁定或解锁班级
     */
    public static String teacherLockClassRoom = ROOT_URL + "/classroom/lock_un_classroom";
    /**
     * 老师获取班级详情
     */
    public static String teacherClassroomListDeatail = ROOT_URL + "/classroom/classroom_deatil";
    /**
     * 班级下的老师列表
     */
    public static String CLASSROOM_TEACHER_LIST = ROOT_URL + "/user/classroom_teacher_list";
    /**
     * 老师布置作业教材章节列表
     */
    public static String getZhangjieDetail = ROOT_URL + "/work/get_zhangjie_detail";

    /**
     * 老师布置作业选择题目
     */
    public static String teacherSelectExercisesData = ROOT_URL + "/Timu/list2";
    /**
     * 老师布置作业
     */
    public static String teacherSendTask = ROOT_URL + "/work/teacher_publish_work";
    /**
     * 老师布置作业成功 提醒家长
     */
    public static String notifyParents = ROOT_URL + "/message/insert_message";
    /**
     * 新消息
     */
    public static String getNewMessageCount = ROOT_URL + "/message/get_new_message_count";
    /**
     * 老师删除班级
     */
    public static String deleteClassroom = ROOT_URL + "/classroom/delete_classroom";
    /**
     * 老师加入班级
     */
    public static String joinClassroom = ROOT_URL + "/classroom/join_classroom";
    /**
     * 老师管理班级移除老师
     */
    public static String classRemoveTeacher = ROOT_URL + "/user/remove_classroom_teachers";
    /**
     * 老师查看班级作业列表
     */
    public static String teacherTaskList = ROOT_URL + "/work/my_published_work";
    /**
     * 老师查看班级作业详情
     */
    public static String teacherTaskDetail = ROOT_URL + "/work/submit_work_detail";
    /**
     * 老师查看班级作业题目详情
     */
    public static String teacherLookTaskTimuDetails = ROOT_URL + "/work/see_work_timu_detail";
    /**
     * 查看某个学生的某个作业的详情
     */
    public static String teacherLookStudentDetails = ROOT_URL + "/work/see_student_work_detail";
    /**
     * 点评某次作业
     */
    public static String dianpingStudentWork = ROOT_URL + "/work/dianping_student_work";
    /**
     * 点评集合
     */
    public static String teacherDianPingList = ROOT_URL + "/work/stu_work_submint_list";
    /**
     * 学生收藏视频
     */
    public static String collectZhishidian = ROOT_URL + "/zhishidian/collect_zhishidian_shipin";

    /**
     * 老师修改用户信息
     */
    public static String modifyTeacherInfo = ROOT_URL + "/user/modify_teacher_info";
    /**
     * 学生收藏列表
     */
    public static String getCollectList = ROOT_URL + "/zhishidian/get_collected_zhishidian_shipin_list";
    /**
     * 消息列表
     */
    public static String getMsgList = ROOT_URL + "/message/get_message_list";

}


