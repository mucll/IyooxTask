package com.huisu.iyoox.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveDate {
    static SharedPreferences sharedPreferences;
    private static SaveDate SAVEDATE;
    //上下文
    private Context con;

    private SaveDate(Context con) {
        this.con = con;
    }

    /***
     * 得到一个单例对象
     ***/
    public static SaveDate getInstence(Context con) {
        if (SAVEDATE == null) {
            SAVEDATE = new SaveDate(con);
        }
        if (sharedPreferences == null) {
            sharedPreferences = con.getSharedPreferences("saveDate", Context.MODE_PRIVATE);
        }
        return SAVEDATE;
    }
    //#########################get/set方法区 开始################################################

    public boolean getRecordSaved(String uid) {
        return sharedPreferences.getBoolean(String.format("recordsaved%s", uid), false);
    }

    public boolean getRecordRed(String uid) {
        return sharedPreferences.getBoolean(String.format("recordvisible%s", uid), false);
    }

    /**
     * @return 储存版本，版本升级后用来比较版本
     */
    public int getVersion() {
        return sharedPreferences.getInt("version", 0);
    }

    public void setVersion(int version) {
        Editor ed = sharedPreferences.edit();
        ed.putInt("version", version);
        ed.commit();
    }

    public String getUser() {
        return sharedPreferences.getString("user", "");
    }

    public void setUser(String user) {
        Editor ed = sharedPreferences.edit();
        ed.putString("user", user);
        ed.commit();
    }

    public String getPWD() {
        return sharedPreferences.getString("pwd", "");
    }

    public void setPWD(String str) {
        Editor ed = sharedPreferences.edit();
        ed.putString("pwd", str);
        ed.commit();
    }

    /**
     * 是否呼吸查看
     *
     * @param uid
     */
    public void setBreathLook(String uid, boolean isCloseBreathLook) {
        Editor ed = sharedPreferences.edit();
        ed.putBoolean(String.format("breathlook%s", uid), isCloseBreathLook);
        ed.commit();
    }

    public boolean getBreathLook(String uid) {
        return sharedPreferences.getBoolean(String.format("breathlook%s", uid), false);
    }

    public void setUserContent(String uid, String json) {
        Editor ed = sharedPreferences.edit();
        ed.putString(String.format("usercontent%s", uid), json);
        ed.commit();
    }

    public String getUserContent(String uid) {
        return sharedPreferences.getString(String.format("usercontent%s", uid), "");
    }
}
