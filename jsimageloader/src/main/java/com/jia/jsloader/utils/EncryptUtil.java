package com.jia.jsloader.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hm on 2016/1/13.
 *
 *   这里的类完成的操作是关于数据的简单加密
 *   1、完成数据的MD5加密（不可逆的加密算法）
 *   2、
 *
 */
public class EncryptUtil {

    //=========MD5加密=============================================
    //关于MD5加密的十六进制的文本内容
    private final static char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

//    19D6A  !=  19d6a

//    private final static char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
//            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };



    /**
     * 该方法完成的操作是进行数据的MD5加密
     * @param str    需要加密的字符串内容
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * MD5加密的辅助方法
     * @param b   byte数组
     * @return
     */
    private static String toHexString(byte[] b) {
        // String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    //=======其他加密待续=================================================





}
