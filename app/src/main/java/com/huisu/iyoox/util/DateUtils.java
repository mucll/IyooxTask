package com.huisu.iyoox.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaojin on 2016/5/31.
 */
public class DateUtils {
    public static SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日");
    public static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
    //默认
    public static SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf6 = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat sdf7 = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat sdf8 = new SimpleDateFormat("mm:ss");
    public static SimpleDateFormat sdf9 = new SimpleDateFormat("ss");
    public static SimpleDateFormat signdateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String format(String from, String to, String time) {
        SimpleDateFormat formatFrom = new SimpleDateFormat(from);
        SimpleDateFormat formatTo = new SimpleDateFormat(to);
        try {
            return formatTo.format(formatFrom.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 时间戳转"yyyy-MM-dd"
     *
     * @param time
     * @return
     */
    public static String formatTime(String time) {
        try {
            long time1 = Long.parseLong(time);
            return sdf4.format(new Date(time1));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 返回yyyy-MM-dd HH:mm
     *
     * @param time
     * @return
     */
    public static String formatTimes(String time) {
        try {
            long time1 = Long.parseLong(time);
            return sdf3.format(new Date(time1));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 返回MM-dd HH:mm
     *
     * @param time
     * @return
     */
    public static String formatTimesMDHm(String time) {
        try {
            Date date = sdf5.parse(time);
            return sdf7.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * "yyyy-MM-dd"字符串返回时间戳
     *
     * @param time
     * @return
     */
    public static String getLong(String time) {
        try {
            return sdf4.parse(time).getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getLong(String time, String timeChuo) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(timeChuo);
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param time
     * @return 返回MM-dd格式，如果为今天就返回HH:mm
     */
    public static String format(long time) {
        try {
            Date date = new Date(time);
            String str1 = sdf4.format(date);
            String str2 = sdf4.format(new Date());
            if (str1.equals(str2)) {
                return "今天 " + sdf2.format(date);
            } else {
                return sdf1.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param time
     * @param to   返回什么格式的字符串
     * @return
     */
    public static String format(long time, String to) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(to);
            return format.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 毫秒转换成时分
     *
     * @param ms
     * @return
     */
    public static String getString(long ms) {
        long hour = ms / (60 * 60 * 1000);
        long minute = (ms - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (ms - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        if (second >= 60) {
            second = second % 60;
            minute += second / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }
        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (second < 10) {
            ss = "0" + String.valueOf(second);
        } else {
            ss = String.valueOf(second);
        }
        return sh + ":" + sm;
    }

    /**
     * 毫秒转换成时分秒
     *
     * @param ms
     * @return
     */
    public static String getStringss(long ms) {
        long hour = ms / (60 * 60 * 1000);
        long minute = (ms - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (ms - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        if (second >= 60) {
            second = second % 60;
            minute += second / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }
        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (second < 10) {
            ss = "0" + String.valueOf(second);
        } else {
            ss = String.valueOf(second);
        }
        return String.format("%s:%s:%s", sh, sm, ss);
    }

    /**
     * 返回MM-dd格式
     *
     * @param time
     * @return
     */
    public static String formatLong(long time) {
        try {
            return sdf6.format(new Date(time));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 时间戳转HH:mm类型
     *
     * @param time
     * @return
     */
    public static String formatHHmm(Long time) {
        try {
            return sdf2.format(new Date(time));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 时间戳转mm:ss类型
     *
     * @param time
     * @return
     */
    public static String formatmmss(Long time) {
        try {
            return sdf8.format(new Date(time));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 时间戳转ss类型
     *
     * @param time
     * @return
     */
    public static String formatss(Long time) {
        try {
            return sdf9.format(new Date(time));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ("yyyy-MM-dd HH:mm:ss")转换为("MM-dd HH:mm"),是今天就显示  今天 HH:mm
     *
     * @param time
     * @return
     */
    public static String formatHHmm(String time) {
        try {
            Date date = sdf5.parse(time);
            String str1 = sdf4.format(date);
            String str2 = sdf4.format(new Date());
            if (str1.equals(str2)) {
                return "今天 " + sdf2.format(date);
            } else {
                return sdf7.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将毫秒数换算成x时x分x秒x毫秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long hour = (ms) / hh;
        long minute = (ms - hour * hh) / mi;
        long second = (ms - hour * hh - minute * mi) / ss;
        long milliSecond = ms - hour * hh - minute * mi - second * ss;
        String strHour = "" + hour;
        String strMinute = "" + minute;
        //        String strSecond = second < 10 ? "0" + second : "" + second;
        //        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
        //        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        return strHour + "小时" + strMinute + "分";
    }

    /**
     * 将毫秒数换算成x天x时x分x秒x毫秒
     *
     * @param ms
     * @return
     */
    public static String formatTimeWithDay(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        String strDay = "" + day;
        String strHour = "" + hour;
        String strMinute = "" + minute;
        //        String strSecond = second < 10 ? "0" + second : "" + second;
        //        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
        //        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        return strDay + "天" + strHour + "小时" + strMinute + "分";
    }

    public static String getPresentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        String date = formatter.format(curDate);
        return date;
    }

    public static String saveCartToPackDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        String date = formatter.format(curDate);
        return date;
    }

    /**
     * a integer to xx:xx:xx秒转为时分秒
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }


    /**
     * 网络时间回调
     *
     * @param hour
     */
    public void getWebTime(int hour) {
    }
}
