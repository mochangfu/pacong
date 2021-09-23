package com.cetc.pacong.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Builder {

    protected static Logger logger = LoggerFactory.getLogger(MD5Builder.class);

    public static String build(String origin, String charsetName) {
        if (origin == null)
            return null;

        StringBuilder sb = new StringBuilder();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(" NoSuchAlgorithmException : ", e);
            return null;
        }

        //生成一组length=16的byte数组
        byte[] bs = digest.digest(origin.getBytes(Charset.forName(charsetName)));

        for (int i = 0; i < bs.length; i++) {
            int c = bs[i] & 0xFF; //byte转int为了不丢失符号位， 所以&0xFF
            if (c < 16) { //如果c小于16，就说明，可以只用1位16进制来表示， 那么在前面补一个0
                sb.append("0");
            }
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        String str = MD5Builder.build("hello,world", "UTF-8");

        System.out.println(str);

    }

}


