package com.huisu.iyoox.alivideo.utils;

import android.text.TextUtils;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.utils.HttpClientUtil;
import com.huisu.iyoox.http.HttpConstants;

import org.json.JSONObject;

/**
 * Function:
 * Date: 2018/9/4
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class PlayAuthUtil {

    private static final String TAG = PlayAuthUtil.class.getSimpleName();

    public static String getPlayAuth(String videoId) {
        try {
            String response = HttpClientUtil.doGet(HttpConstants.ROOT_URL + "/vod/get_play_auth?video_id=" + videoId);
            JSONObject jsonObject = new JSONObject(response);
            String playAuth = jsonObject.getString("data");
            return TextUtils.isEmpty(playAuth) ? "" : playAuth;
        } catch (Exception e) {
            VcPlayerLog.e(TAG, "e = " + e.getMessage());
            return null;
        }

    }
}
