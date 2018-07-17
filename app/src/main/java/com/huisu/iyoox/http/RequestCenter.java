package com.huisu.iyoox.http;

import com.huisu.iyoox.entity.base.BaseBookDetailsModel;
import com.huisu.iyoox.entity.base.BaseCheckMsgCode;
import com.huisu.iyoox.entity.base.BaseExercisesModel;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.entity.base.BasePhoneModel;
import com.huisu.iyoox.entity.base.BaseRegisterResultModel;
import com.huisu.iyoox.entity.base.BaseScreenSubjectVersionModel;
import com.huisu.iyoox.entity.base.BaseSendMsgCodeModel;
import com.huisu.iyoox.entity.base.BaseSubjectModel;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.entity.base.BaseVideoModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.okhttp.CommonOkHttpClient;
import com.huisu.iyoox.okhttp.listener.DisposeDataHandle;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.okhttp.request.CommonRequest;
import com.huisu.iyoox.okhttp.request.RequestParams;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.MD5Util;

/**
 * @author: vision
 * @function:
 * @date: 16/8/12
 */
public class RequestCenter {

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void getRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        //打印请求地址
        LogUtil.e(url);
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 文件上传
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void postUpFile(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createMultiPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 判断手机号 是否注册
     *
     * @param listener
     * @param phone    手机号
     */
    public static void isExistphone(String phone, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        RequestCenter.postRequest(HttpConstants.IS_EXISTPHONE, params, listener, BasePhoneModel.class);
    }

    /**
     * 发送短信验证码
     *
     * @param listener
     * @param phone    手机号
     */
    public static void sendMsgCode(String phone, String source, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("source", source);
        RequestCenter.postRequest(HttpConstants.SEND_MSG_CODE, params, listener, BaseSendMsgCodeModel.class);
    }

    /**
     * 判断验证是否正确
     *
     * @param listener
     * @param phone    手机号
     */
    public static void checkMsgCode(String phone, String code, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("code", code);
        RequestCenter.postRequest(HttpConstants.CHECK_MSG_CODE, params, listener, BaseCheckMsgCode.class);
    }

    /**
     * 用户注册
     *
     * @param listener
     */
    public static void register(String phone, String password, String type, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", password);
        params.put("type", type);
        RequestCenter.postRequest(HttpConstants.REGISTER, params, listener, BaseRegisterResultModel.class);
    }

    /**
     * 设置用户信息
     *
     * @param listener
     */
    public static void setUserInfo(String userId, String name, String sex, String grade, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("name", name);
        params.put("sex", sex);
        params.put("grade", grade);
        RequestCenter.postRequest(HttpConstants.SET_USER_INFO, params, listener, BaseUser.class);
    }

    /**
     * 用户登陆请求
     *
     * @param listener
     */
    public static void login(String phone, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", MD5Util.getMD5(password));
        RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, BaseUser.class);
    }

    /**
     * 年级列表
     *
     * @param listener
     */
    public static void getGrades(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GET_GRADES, null, listener, BaseGradeListModel.class);
    }

    /**
     * 重置密码
     *
     * @param listener
     */
    public static void ModifyPassword(String phone, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", password);
        RequestCenter.postRequest(HttpConstants.MODIFY_PASSWORD, params, listener, null);
    }

    /**
     * 学习界面 年级列表和教材列表
     *
     * @param listener
     */
    public static void indexList(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.INDEX_LIST, null, listener, BaseGradeListModel.class);
    }

    /**
     * 学习界面 章节 和 知识点列表
     *
     * @param listener
     */
    public static void getOptionlist(String grade_id, String kemu_id, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", grade_id);
        params.put("kemu_id", kemu_id);
        RequestCenter.postRequest(HttpConstants.GET_OPTIONLIST, params, listener, BaseBookDetailsModel.class);
    }

    /**
     * 学习界面 章节 和 知识点列表
     *
     * @param listener
     */
    public static void getShipinlist(String grade_id, String kemu_id, String jiaocai_id, String grade_detail_id,
                                     String zhangjie_id, String zhishidian, String page,
                                     DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", grade_id);
        params.put("kemu_id", kemu_id);
        params.put("jiaocai_id", jiaocai_id);
        params.put("grade_detail_id", grade_detail_id);
        params.put("zhangjie_id", zhangjie_id);
        params.put("zhishidian", zhishidian);
        params.put("page_index", page);
        RequestCenter.postRequest(HttpConstants.GET_SHIPINLIST, params, listener, BaseVideoModel.class);
    }

    /**
     * 获取视频列表
     *
     * @param listener
     */
    public static void getVideoTimu(String shipinId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("shipin_id", shipinId);
        RequestCenter.postRequest(HttpConstants.GET_VIDEO_TIMU, params, listener, BaseVideoTimuModel.class);
    }

    /**
     * 获取有错题的科目
     *
     * @param listener
     */
    public static void getErrorSubject(String studentId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        RequestCenter.postRequest(HttpConstants.getErrorSubject, params, listener, BaseSubjectModel.class);
    }

    /**
     * 获取有错题的科目
     *
     * @param listener
     */
    public static void getErrorSubjectDetilas(String studentId, String subjectId, String jiaoCaiId, String zhishidianId
            , String time, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        params.put("kemu_id", subjectId);
        params.put("jiaocao_id", jiaoCaiId);
        params.put("zhishidian_id", zhishidianId);
        params.put("time_id", time);
        RequestCenter.postRequest(HttpConstants.getErrorSubjectDetails, params, listener, BaseExercisesModel.class);
    }

    /**
     * 获取错题筛选数据
     *
     * @param listener
     */
    public static void getscreenErrorListData(String studentId, String subjectId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        params.put("kemu_id", subjectId);
        RequestCenter.postRequest(HttpConstants.getscreenErrorListData, params, listener, BaseScreenSubjectVersionModel.class);
    }
}
