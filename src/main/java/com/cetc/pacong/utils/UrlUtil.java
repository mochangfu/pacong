package com.cetc.pacong.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    public static boolean isHttpUrl(String urls) {

        boolean isurl = false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }

    public static String resovleBaseUrl(String url) {
        return url.substring(0, url.lastIndexOf("/")) + "/";
    }


    public static String simpleFileName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
}
