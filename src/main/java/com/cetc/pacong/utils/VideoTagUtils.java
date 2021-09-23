package com.cetc.pacong.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理视频HTML标签的工具类
 *
 * @author zhangchao
 */
public final class VideoTagUtils {

    /**
     * 从html代码中，获得指定标签的指定属性的取值
     *
     * @param html         HTML代码
     * @param tagName      指定的标签名称
     * @param propertyName 指定的属性名称
     * @return
     */
    public static List<String> listTagPropertyValue(final String html, final String tagName, final String propertyName) {
        // 结果集合
        ArrayList<String> result = new ArrayList<>();
        // 找出HTML代码中所有的tagName标签
        List<String> list = RegexUtils.getMatchList(html, "<" + tagName + "[^>]*>", true);
        // 循环遍历每个标签字符串，找出其中的属性字符串,比如 src=....
        for (String tagStr : list) {
            // 去掉标签结尾的/>，方便后面 src 属性的正则表达式。
            // 这样可以适应  <video src=http://www.yourhost.com/xxx/>  这样的标签
            if (tagStr.endsWith("/>")) {
                tagStr = tagStr.substring(0, tagStr.length() - 2);
                tagStr = tagStr + " ";
            }
            // 去掉标签结尾的>，方便后面匹配属性的正则表达式。
            // 这样可以适应  <video src=http://www.yourhost.com/xxx>  这样的标签
            else if (tagStr.endsWith(">")) {
                tagStr = tagStr.substring(0, tagStr.length() - 1);
                tagStr = tagStr + " ";
            }
            // 去掉字符串开头的 <video 或 <source
            tagStr = tagStr.substring(1 + tagName.length());
            tagStr = " " + tagStr;

            // 取出属性的值
            String regSingleQuote = "^" + propertyName + "='[^']*'"; // 使用单引号
            String regDoubleQuote = "^" + propertyName + "=\"[^\"]*\""; // 使用双引号
            String reg = "^" + propertyName + "=[^\\s]*\\s"; // 不使用引号
            int index = 0;
            int length = tagStr.length();
            while (index <= length) {
                String subStr = tagStr.substring(index);
                // 单引号
                String str = RegexUtils.getFirstMatch(subStr, regSingleQuote, true);
                if (null != str) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                    String srcStr = str;
                    srcStr = srcStr.trim();
                    // 去掉 src=
                    srcStr = srcStr.substring(propertyName.length() + 1);
                    // 去掉单引号
                    srcStr = srcStr.substring(1);
                    srcStr = srcStr.substring(0, srcStr.length() - 1);
                    // 结果中加入图片URL
                    result.add(srcStr);
                }
                // 双引号
                else if ((str = RegexUtils.getFirstMatch(subStr, regDoubleQuote, true)) != null) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                    String srcStr = str;
                    srcStr = srcStr.trim();
                    // 去掉 src=
                    srcStr = srcStr.substring(propertyName.length() + 1);
                    // 去掉双引号
                    srcStr = srcStr.substring(1);
                    srcStr = srcStr.substring(0, srcStr.length() - 1);
                    // 结果中加入图片URL
                    result.add(srcStr);
                }
                // 不使用引号
                else if ((str = RegexUtils.getFirstMatch(subStr, reg, true)) != null) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                    String srcStr = str;
                    srcStr = srcStr.trim();
                    // 去掉 src=
                    srcStr = srcStr.substring(propertyName.length() + 1);
                    // 结果中加入图片URL
                    result.add(srcStr);
                }
                // 其他属性-单引号
                else if ((str = RegexUtils.getFirstMatch(subStr, "^[\\w|-]+='[^']*'", true)) != null) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                }
                // 其他属性-双引号
                else if ((str = RegexUtils.getFirstMatch(subStr, "^[\\w|-]+=\"[^\"]*\"", true)) != null) {
                    // 往后跳过已经匹配的字符串。
                    index += str.length();
                } else {
                    index++;
                }
            }
        } // end for (String tagStr : list)
        result.trimToSize();
        return result;
    }


    /**
     * 从html代码中找出video标签的图片路径
     *
     * @param html HTML代码
     * @return 字符串列表，里面每个字符串都是图片链接地址
     */
    public static List<String> listVideoSrc(final String html) {
        // 结果集合
        ArrayList<String> result = new ArrayList<String>();
        if (null == html || html.length() == 0 || html.trim().length() == 0) {
            return result;
        }
        List<String> videoSrcList = listTagPropertyValue(html, "video", "src");
        List<String> sourceSrcList = listTagPropertyValue(html, "source", "src");
        if (null != videoSrcList) {
            result.addAll(videoSrcList);
        }
        if (null != sourceSrcList) {
            result.addAll(sourceSrcList);
        }
        result.trimToSize();
        return result;
    }
}
