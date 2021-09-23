package com.cetc.pacong.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
    public static String encode(String s) {
        if (s == null) {
            return Base64.encodeBase64URLSafeString("".getBytes());
        }
        return Base64.encodeBase64URLSafeString(s.getBytes());

    }


    public static String encodeURLSafeString(String s) {
        if (s == null) {
            return Base64.encodeBase64URLSafeString("".getBytes());
        }

        String str = Base64.encodeBase64URLSafeString(s.getBytes());

        return str.length() > 255 ? str.substring(0, 255) : str;
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.encodeURLSafeString("http://baike.baidu.com/view/159811.htm"));
    }
}
