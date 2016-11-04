package com.team.group.ourlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常见正则
 * Created by TranGuility on 16/11/4.
 */

public class RegularUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    /**
     * 手机号正则
     */
    public static boolean isPhone(String mobiles) {
        //        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(170))\\d{8}$");
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证码正则
     */
    public static boolean isCodes(String codes) {
        Pattern p = Pattern.compile("\\d{6}$");
        Matcher m = p.matcher(codes);
        return m.matches();
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }
}
