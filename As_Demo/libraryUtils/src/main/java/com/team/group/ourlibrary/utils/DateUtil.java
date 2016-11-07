package com.team.group.ourlibrary.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间类
 *
 * @Author Ellrien
 * @Date 2015年7月6日下午1:07:41
 * @Version 1.0
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static String MS_TO_TIME1 = "dd天HH时mm分ss秒";
    public static String MS_TO_TIME2 = "HH时mm分ss秒";
    public static String MS_TO_TIME3 = "mm分ss秒";
    public static String DATE_FORMAT_STR1 = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT_STR2 = "MM.dd";
    public static String DATE_FORMAT_STR3 = "yyyyMMdd";
    public static String DATE_FORMAT_STR4 = "yyyy/MM/dd HH:mm:ss";
    public static String DATE_FORMAT_STR5 = "yyyy.MM.dd HH:mm";
    public static String DATE_FORMAT_STR6 = "yyyy-MM-dd";
    public static String DATE_FORMAT_STR7 = "MM.dd HH:mm:ss";
    public static String DATE_FORMAT_STR8 = "MM/dd HH:mm:ss";
    public static String DATE_FORMAT_STR9 = "MM/dd";
    public static String DATE_FORMAT_STR10 = "yyyy/MM/dd";
    public static String DATE_FORMAT_STR11 = "yyyyMMddHHmmss";


    public static String getNewTime() {
        Calendar cal = Calendar.getInstance();
        return (String) DateFormat.format(DATE_FORMAT_STR1, cal);
    }

    public static String getNewTimeStr() {
        Calendar cal = Calendar.getInstance();
        return (String) DateFormat.format(DATE_FORMAT_STR11, cal);
    }

    public static String msToTime(long ms) {
        SimpleDateFormat formatter;
//        if (ms >= 24 * 3600 * 1000) {
//            ms = ms - 24 * 3600 * 1000;
//            formatter = new SimpleDateFormat(MS_TO_TIME1);
//        } else if (ms >= 3600 * 1000) {
//            formatter = new SimpleDateFormat(MS_TO_TIME2);
//        } else {
        formatter = new SimpleDateFormat(MS_TO_TIME3);
//        }

        long s = TimeZone.getDefault().getRawOffset();
        String hms = formatter.format(ms - s * 1000);
        return hms;
    }

    public static String parseTime(String dateStr, String format1, String format2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format1);
        Date date = sdf.parse(dateStr);
        SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
        String datestr2 = sdf2.format(date);
        return datestr2;
    }

    public static Date parseTime(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }

    /**
     * 时间戳(毫秒)----日期转为毫秒
     *
     * @return
     */

    public static long timeStampMS(String date) {
        long timePoor = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parseEnd = df.parse(date);
            Date parseStart = df.parse("1970-01-01");
            timePoor = parseEnd.getTime() - parseStart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePoor;
    }

    public static long timeStampMS(String date, String yType, String mType, String dType) {
        long timePoor = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy" + yType + "MM" + mType + "dd" + dType);
        try {
            Date parseEnd = df.parse(date);
            Date parseStart = df.parse("1970-01-01");
            timePoor = parseEnd.getTime() - parseStart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePoor;
    }


    public static long timeMMddStampMS(String date) {
        long timePoor = 0;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        try {
            Date parseEnd = df.parse(date);
            Date parseStart = df.parse("1970-01-01");
            timePoor = parseEnd.getTime() - parseStart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePoor;
    }

    /**
     * 时间戳(秒)----日期转为秒
     *
     * @return
     */

    public static long timeStampS(String date) {
        long timePoor = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parseEnd = df.parse(date);
            Date parseStart = df.parse("1970-01-01");
            timePoor = parseEnd.getTime() - parseStart.getTime();
            timePoor = timePoor / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePoor;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return Date类型
     */
    public static Date MSStampyyMMddDate(long time) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time);
        String longStr = df.format(aLong);
        try {
            date = df.parse(longStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间戳(秒)----秒转年月日
     *
     * @param time
     * @return Date类型
     */
    public static Date SStampyyMMddDate(long time) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time / 1000);
        String longStr = df.format(aLong);
        try {
            date = df.parse(longStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日 时分
     *
     * @param time
     * @return String类型
     */
    public static String MSStampyyMMddStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    public static String MSStampyyMMddStr(String time) {
        long lon = Long.parseLong(time);
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        date = df.format(new Date(lon));
        return date;
    }


    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampyyMMStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampHHmmStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(秒)----秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String SStampyyMMddStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time / 1000);
        String longStr = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampyMdHmmStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampyMdHmmStrYY(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    public static String MSStampyMdHmmStrYY(long time, String type) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat(type);
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(秒)----秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String SStampyMdHmmStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Long aLong = new Long(time / 1000);
        String longStr = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(秒)----转年月日时分秒
     *
     * @param time
     * @return String类型
     */
    public static String SSmmHH(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.format(time);
    }

    /**
     * 时间戳(秒)----转分秒
     *
     * @param time
     * @return String类型
     */
    public static String HHmmss(long time) {
        if (time <= 0) {
            return "已超时";
        }
        String strHour;
        String strMin;
        String strSec;
        String strDay;
        long day = time / (60 * 60 * 24);
        long hour = time / (60 * 60) % 24;
        long minute = time / (60) % 60;
        long second = time % 60;
        strDay = day / 10 == 0 ? "0" + day : day + "";
        strHour = hour / 10 == 0 ? "0" + hour : hour + "";
        strMin = minute / 10 == 0 ? "0" + minute : minute + "";
        strSec = second / 10 == 0 ? "0" + second : second + "";
        if (strDay.equals("00")) {
            return strHour + ":" + strMin + ":" + strSec;
        } else {
            return strDay + ":" + strHour + ":" + strMin + ":" + strSec;
        }

    }

    /**
     * 时间格式转换 年月日替换-
     *
     * @param date
     * @return
     */
    public static String chageTime(String date) {
        String year, month, day = null;
        if (!StringUtils.isEmpty(date)) {
            year = date.replaceAll("年", "-");
            month = year.replace("月", "-");
            day = month.replaceAll("日", "");
        }
        return day;
    }


}