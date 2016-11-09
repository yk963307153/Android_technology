package com.team.group.ourlibrary.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串转为MD5值
 * Created by TranGuility on 16/11/9.
 */

public class MD5Utils {
    //另一种写法
//    public  static String getMD5Convert(String str){
//        String password = null;
//        MessageDigest mdEnc;
//        try {
//            mdEnc = MessageDigest.getInstance("MD5");
//            mdEnc.update(str.getBytes(), 0, str.length());
//            str = new BigInteger(1, mdEnc.digest()).toString(16);
//            while (str.length() < 32) {
//                str = "0" + str;
//            }
//            password = str;
//        } catch (NoSuchAlgorithmException e1) {
//            e1.printStackTrace();
//        }
//        return password;
//    }


    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param s 数据源
     * @return
     */
    public static final String getMD5Convert(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte 签名byte数组
     * @return 32位签名字符串
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
