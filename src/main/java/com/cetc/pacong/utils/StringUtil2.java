package com.cetc.pacong.utils;

public final class StringUtil2 {

    private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * 把指定的数据转化为16进制格式的字符串
     *
     * @param data 待转化的数据
     * @return 16进制表示的数据
     */
    public static String toHexString(byte[] data) {
        return toHexString(data, 0, data.length);
    }

    /**
     * 把指定的数据转化为16进制格式的字符串， 如toHexString({8,9,12,4},1,3) = "090C"
     *
     * @param data       待转化的数据
     * @param beginIndex 需转化的数据的起始索引
     * @param endIndex   需转化的数据的结束索引
     * @return 16进制表示的数据
     */
    public static String toHexString(byte[] data, int beginIndex, int endIndex) {
        if (data == null || beginIndex < 0) {
            return null;
        }
        StringBuilder strBuilder = new StringBuilder();
        for (int i = beginIndex; i < endIndex; i++) {
            strBuilder.append(hexDigits[data[i] >>> 4 & 0xf]);
            strBuilder.append(hexDigits[data[i] & 0xf]);
        }
        return strBuilder.toString();
    }

    /**
     * slf4j风格的格式化api
     *
     * @param message
     * @param params
     * @return
     */
    public static String formatSlf4jMsg(String message, Object... params) {
        if ((message == null) || (params == null) || (params.length == 0)) {
            return message;
        }

        return org.slf4j.helpers.MessageFormatter.arrayFormat(message, params).getMessage();
    }
}
