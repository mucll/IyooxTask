package com.huisu.iyoox.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.huisu.iyoox.application.MyApplication;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.okhttp.ResponseEntityToModule;
import com.huisu.iyoox.okhttp.exception.OkHttpException;
import com.huisu.iyoox.okhttp.listener.DisposeDataHandle;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.okhttp.listener.DisposeHandleCookieListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.TabToast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * @author vision
 * @function 专门处理JSON的回调
 */
public class CommonJsonCallback implements Callback {

    /**
     * the logic layer exception, may alter in different app
     */
    /**
     * 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
     */
    protected final String RESULT_CODE = "ecode";
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    /**
     * decide the server it
     */
    protected final String COOKIE_STORE = "Set-Cookie";
    // can has the value of
    // set-cookie2

    /**
     * the java layer exception, do not same to the logic error
     */
    /**
     * the network relative error
     */
    protected final int NETWORK_ERROR = -1;
    /**
     * the JSON relative error
     */
    protected final int JSON_ERROR = -2;
    /**
     * the unknow error
     */
    protected final int OTHER_ERROR = -3;

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                TabToast.showMiddleToast(MyApplication.getApplication().getApplicationContext(), "网络异常");
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        final ArrayList<String> cookieLists = handleCookie(response.headers());
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
                /**
                 * handle the cookie
                 */
                if (mListener instanceof DisposeHandleCookieListener) {
                    ((DisposeHandleCookieListener) mListener).onCookie(cookieLists);
                }
            }
        });
    }

    private ArrayList<String> handleCookie(Headers headers) {
        ArrayList<String> tempList = new ArrayList<String>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).equalsIgnoreCase(COOKIE_STORE)) {
                tempList.add(headers.value(i));
            }
        }
        return tempList;
    }

    private void handleResponse(Object responseObj) {
        if (responseObj == null || "".equals(responseObj.toString().trim())) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            /**
             * 协议确定后看这里如何修改
             */
            JSONObject result = new JSONObject(responseObj.toString());
            //打印日志 方便查看
            LogUtil.e(responseObj.toString());
            int code = result.getInt("code");
            if (code != Constant.POST_SUCCESS_CODE) {
                mListener.onSuccess(result);
                return;
            }
            if (mClass == null) {
                mListener.onSuccess(result);
            } else {
                Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}