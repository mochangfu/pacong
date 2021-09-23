package com.cetc.pacong.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author zhangchao
 */
public final class RegexUtils {
    /**
     * 获得匹配正则表达式的内容
     *
     * @param str               字符串
     * @param reg               正则表达式
     * @param isCaseInsensitive 是否忽略大小写，true忽略大小写，false大小写敏感
     * @return 匹配正则表达式的字符串，组成的List
     */
    public static List<String> getMatchList(final String str, final String reg, final boolean isCaseInsensitive) {
        ArrayList<String> result = new ArrayList<String>();
        Pattern pattern = null;
        if (isCaseInsensitive) {
            //编译正则表达式,忽略大小写
            pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        } else {
            //编译正则表达式,大小写敏感
            pattern = Pattern.compile(reg);
        }
        Matcher matcher = pattern.matcher(str);// 指定要匹配的字符串
        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            result.add(matcher.group());//获取当前匹配的值
        }
        result.trimToSize();
        return result;
    }

    /**
     * 获取第一个匹配正则表达式的子串
     *
     * @param str               完整字符串
     * @param reg               正则表达式
     * @param isCaseInsensitive 是否忽略大小写，true表示忽略，false表示大小写敏感。
     * @return 第一个匹配正则表达式的子串。
     */
    public static String getFirstMatch(final String str, final String reg, final boolean isCaseInsensitive) {
        Pattern pattern = null;
        if (isCaseInsensitive) {
            //编译正则表达式,忽略大小写
            pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        } else {
            //编译正则表达式,大小写敏感
            pattern = Pattern.compile(reg);
        }
        Matcher matcher = pattern.matcher(str);// 指定要匹配的字符串
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}

