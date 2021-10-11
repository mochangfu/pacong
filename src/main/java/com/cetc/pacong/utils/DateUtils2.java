package com.cetc.pacong.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class DateUtils2 extends org.apache.commons.lang3.time.DateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils2.class);
    private static final String[] WEEK_DAYS = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
    private static final String[] SECOND_WEEK_DAYS = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };


    public static final String WEB_TRANSFER_FORMAT = "yyyyMMddHHmmssSSS";
    private static final String WEB_TRANSFER_FORMAT_PATTERN = "^\\d{17}$";

    public static final String DATE_TIME_CN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_CN_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";

    public static final String DATE_TIME_MILLI_CN = "yyyy-MM-dd HH:mm:ss:SSS";
    private static final String DATE_TIME_MILLI_CN_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3}$";

    public static final String NO_SEC_CN = "yyyy-MM-dd HH:mm";
    private static final String NO_SEC_CN_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";

    public static final String DATE_TIME_EN = "dd/MM/yyyy HH:mm:ss";
    private static final String DATE_TIME_EN_PATTERN = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}$";

    public static final String DATE_TIME_MILLI_EN = "dd/MM/yyyy HH:mm:ss:SSS";
    private static final String DATE_TIME_MILLI_EN_PATTERN = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}:\\d{3}$";

    public static final String NO_SEC_EN = "dd/MM/yyyy HH:mm";
    private static final String NO_SEC_EN_PATTERN = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}$";

    public static final String DATE = "dd/MM/yyyy";
    private static final String DATE_PATTERN = "^\\d{2}/\\d{2}/\\d{4}$";

    public static final String DATE_CN = "yyyy-MM-dd";
    private static final String DATE_CN_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    private static final String XML_GREGORIAN_CALENDAR = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String XML_GREGORIAN_CALENDAR_PATTERN = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";



    private static final int YEAR_LOW = 1;

    private static final int YEAR_TOP = 2200;
/*
    private static final List<DateTimeFormatter> FORMATS = Lists.newArrayList(
            DateTimeFormat.forPattern(DateUtils2.WEB_TRANSFER_FORMAT),
            DateTimeFormat.forPattern(DateUtils2.DATE_TIME_CN),
            DateTimeFormat.forPattern(DateUtils2.DATE_TIME_MILLI_CN),
            DateTimeFormat.forPattern(DateUtils2.NO_SEC_CN),
            DateTimeFormat.forPattern(DateUtils2.DATE_CN),
            DateTimeFormat.forPattern(DateUtils2.DATE_TIME_EN),
            DateTimeFormat.forPattern(DateUtils2.DATE_TIME_MILLI_EN),
            DateTimeFormat.forPattern(DateUtils2.NO_SEC_EN),
            DateTimeFormat.forPattern(DateUtils2.DATE),
            DateTimeFormat.forPattern(DateUtils2.XML_GREGORIAN_CALENDAR)
    );*/

    private static List<Pattern> regexPatterns = Lists.newArrayList(
            Pattern.compile(WEB_TRANSFER_FORMAT_PATTERN),
            Pattern.compile(DATE_TIME_CN_PATTERN),
            Pattern.compile(DATE_TIME_MILLI_CN_PATTERN),
            Pattern.compile(NO_SEC_CN_PATTERN),
            Pattern.compile(DATE_CN_PATTERN),
            Pattern.compile(DATE_TIME_EN_PATTERN),
            Pattern.compile(DATE_TIME_MILLI_EN_PATTERN),
            Pattern.compile(NO_SEC_EN_PATTERN),
            Pattern.compile(DATE_PATTERN),
            Pattern.compile(XML_GREGORIAN_CALENDAR_PATTERN)

    );

    /**
     * Same with paeseDate, without checked exception
     *
     * @param 
     * @return
     */
/*
    public static Date parseDateWithPattern(final String str, final String... parsePatterns) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str, parsePatterns);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
*/
/*

    public static Date parseDate(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        boolean success = false;
        LocalDateTime date = null;
        if (!success) {
            int index = 0;
            while (index < FORMATS.size()) {
                int currentIndex = index;
                ++index;
                Pattern pattern = regexPatterns.get(currentIndex);
                if (pattern.matcher(text).matches()) {
                    date = FORMATS.get(currentIndex).parseLocalDateTime(text);
                    success = true;
                    break;
                }
            }
        }

        if (!success) {//如果时间小于阈值，则直接取null
            date = LocalDateTime.parse(text);
        }

        if (date.getYear() <= YEAR_LOW) {//如果时间小于阈值，则直接取null
            return null;
        }

        return date.toDate();
    }
*/

    public static Integer getDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得指定日期的开始点，值为yyyy-MM-dd HH:mm:ss:000，如2011-09-07的开始时间为2011-09-07
     * 00:00:00:000
     *
     * @param d 待操作的日期
     * @return 指定的日期的开始时间点
     */
    public static Date getDayBegin(Date d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 取得指定日期的结束点，值为yyyy-MM-dd 23:59:59:990，如2011-09-07的结束时间为2011-09-07
     * 23:59:59:990   sqlserver数据库精度为1/300秒，不能用999
     *
     * @param d 待操作的日期
     * @return 指定的日期的结束时间点
     */
    public static Date getDayEnd(Date d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 990);
        return c.getTime();
    }
/*

    public static Calendar parseXMLGregorianCalendar(String sDate) {
        try {
            DatatypeFactory df = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlGCalendar = df.newXMLGregorianCalendar(sDate);
            return xmlGCalendar.toGregorianCalendar();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
*/

    /**
     * 客户端对null的日期序列化之后会变成0001-01-03 08:00:00这样的日期，需要过滤为null
     *
     * @param date
     * @return
     * @author Derek Jin
     */
    /*public static Date getAllowedDate(Date date) {
        if (date != null) {
            if ((new DateTime(date)).isBefore(DATE_TIME_LOW)) {
                return null;
            }
        }
        return date;
    }*/

    /**
     * 取得系统的当前时间，不带微秒
     *
     * @return 系统当前时间，不带微秒
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }


    /**
     * 取得以格式yyyy-MM-dd表示的日期
     *
     * @param date 待格式化的日期
     * @return yyyy-MM-dd格式化的日期
     */
    public static String getDateYMD(Date date) {
        return getFormattedDate(date, "yyyy-MM-dd");
    }

    public static String getDateYMDHMS(Date date) {
        return getFormattedDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 取得指定格式表示的日期/时间
     *
     * @param date    待格式化的日期/时间
     * @param pattern 目标格式
     * @return 按目标格式格式化后的日期/时间，如果时间或者目标格式为null则返回null
     */
    public static String getFormattedDate(Date date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        if (XML_GREGORIAN_CALENDAR_PATTERN.equals(pattern)) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return toXMLGregorianCalendarFormat(calendar);
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static Integer calDateGap(Date before, Date after) {
        if (before == null || after == null) {
            throw new IllegalArgumentException();
        }
        before = DateUtils2.getDayBegin(before);
        after = DateUtils2.getDayBegin(after);
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(before);
        aft.setTime(after);
        Integer days = (int) ((after.getTime() - before.getTime()) / (24 * 60 * 60 * 1000)) + 1;
        return days;

    }

    public static String toXMLGregorianCalendarFormat(Calendar calendar) {
        try {
            DatatypeFactory df = DatatypeFactory.newInstance();
            GregorianCalendar gCalendar = new GregorianCalendar(calendar.getTimeZone());
            gCalendar.setTime(calendar.getTime());
            XMLGregorianCalendar xmlGCalendar = df.newXMLGregorianCalendar(gCalendar);
            return xmlGCalendar.toXMLFormat();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断指定的日期是否属于同一天
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            return true;
        }
        if (d1 == null && d2 != null) {
            return false;
        }
        if (d1 != null && d2 == null) {
            return false;
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 删除某日期中的秒和毫秒
     *
     * @param d
     * @return
     */
    public static Date truncateSecond(Date d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 删除某日期中的毫秒
     *
     * @param d
     * @return
     */
    public static Date truncateMillisecond(Date d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getDatePart(Date date) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(DateFormatUtils.format(date, "yyyy-MM-dd"),
                    "yyyy-MM-dd");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseUTCDate(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getNextDayPart(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            return org.apache.commons.lang3.time.DateUtils.parseDate(DateFormatUtils.format(c.getTime(), "yyyy-MM-dd"),
                    "yyyy-MM-dd");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String partseDateToString(Date date, String format) {
        DateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

/*
    public static Date getDate(Integer year, Integer month, Integer day) {
        return new DateTime(year, month, day != null ? day : 1, 0, 0, 0).toDate();
    }
*/

    /**
     * 返回周几
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static Date getMondayOfWeek() {
        return getMondayOfWeek(new Date());
    }

    public static Date getMondayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        int current = c.get(Calendar.DAY_OF_WEEK);

        int first = c.getFirstDayOfWeek();

        c.add(Calendar.DAY_OF_MONTH, first - (current >= Calendar.MONDAY ? current : 8));

        return c.getTime();
    }

    public static boolean isMonday(Date date) {
        return isWeekday(date, Calendar.MONDAY);
    }

    public static boolean isSunday(Date date) {
        return isWeekday(date, Calendar.SUNDAY);
    }

    public static boolean isWeekday(Date date, int weekday) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return day == weekday;
    }

    public static int getSpanDays(Date beginDate, Date endDate) {
        beginDate = getDatePart(beginDate);
        endDate = getDatePart(endDate);

        return Integer.valueOf("" + (Math.abs((beginDate.getTime() - endDate.getTime())) / (24 * 60 * 60 * 1000) + 1));
    }

    //获取昨天0点0分0秒
    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date getSundayOfWeek() {

        return getSundayOfWeek(getNowDate());
    }

    public static Date getSundayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(getMondayOfWeek(date));

        c.add(Calendar.DAY_OF_MONTH, 6);

        return c.getTime();
    }

    public static Date getNowDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 990);
        return c.getTime();
    }

    /**
     * 获取某一年的同一天
     *
     * @param date
     * @param year
     * @return
     */
    public static Date getYearSameDay(Date date, Integer year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, year);
        return c.getTime();
    }

    public static Date getNowDateBeforeWeek() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -7);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    public static Date getYearBegin(Integer year) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);
        return c.getTime();
    }

    public static Date getYearBegin() {
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        c.clear();
        c.set(Calendar.YEAR, currentYear);
        return c.getTime();
    }

    public static Date getYearEnd(Integer year) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);
        c.roll(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    public static Date getYearEnd() {
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        c.clear();
        c.set(Calendar.YEAR, currentYear);
        c.roll(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    public static Date getCurrentQuarterBeginDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        Integer month = c.get(Calendar.MONTH);

        if (month < 3) {
            c.set(Calendar.MONTH, 0);
        } else if (month < 6) {
            c.set(Calendar.MONTH, 3);
        } else if (month < 9) {
            c.set(Calendar.MONTH, 6);
        } else {
            c.set(Calendar.MONTH, 9);
        }

        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getQuarterBeginDate(Integer year, Integer quarter) {
        assert quarter >= 1 && quarter <= 4;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, quarter * 3 - 3);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getQuarterEndDate(Integer year, Integer quarter) {
        assert quarter >= 1 && quarter <= 4;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, quarter * 3 - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 990);
        return c.getTime();
    }

    public static Date getMonthBeginDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getMonthEndDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 990);
        return c.getTime();
    }

    public static Date getMonthBeginDate(Integer year, Integer month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getMonthEndDate(Integer year, Integer month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 990);
        return c.getTime();
    }

    public static Date getCurrentMonthBeginDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getCurrentRecentMonthDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -30);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * 获取最近几个月的开始时间
     *
     * @param last        last为1，则是最近1个月，即本月
     * @param currentDate 所在月份
     * @return
     */
    public static Date getRecentMonthBegin(int last, Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.MONTH, -last + 1);
        return c.getTime();
    }

    /**
     * 获取下几个月的开始时间
     *
     * @param next        next为0，即本月
     * @param currentDate 所在月份
     * @return
     */
    public static Date getNextMonthBegin(int next, Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.MONTH, next);
        return c.getTime();
    }

    /**
     * 获取最近几个天前时间
     *
     * @param last        last为1，则是前几天的这时间
     * @param currentDate 所在月份
     * @return
     */
    public static Date getRecentDay(int last, Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DAY_OF_YEAR, -last);
        return c.getTime();
    }

    /**
     * 获取最近几个月前时间
     *
     * @param last        last为1，则是上个月的这时间
     * @param currentDate 所在月份
     * @return
     */
    public static Date getRecentMonth(int last, Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MONTH, -last);
        return c.getTime();
    }

    /**
     * 获取最近几年前时间
     *
     * @param last        last为1，则是去年的这时间
     * @param currentDate 所在时期
     * @return
     */
    public static Date getRecentYear(int last, Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.YEAR, -last);
        return c.getTime();
    }

    /**
     * 检测输入时间是否晚于中午12点
     *
     * @param currentTime
     * @return true 晚于12点
     * false 早于12点
     */
    public static Boolean overNoon(Date currentTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        return c.get(Calendar.HOUR_OF_DAY) >= 12;
    }


    public static Integer getYear(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(date);
        return Integer.parseInt(year);
    }

    public static Integer getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static Integer getSeason(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    public static String getFormatDate(Date date, String formatPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        return sdf.format(date);
    }

    public static String calculateWeekDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return WEEK_DAYS[w];
    }


    public static Integer[] calMongthGap(Date before, Date after) {
        Integer[] res = new Integer[2];
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(before);
        aft.setTime(after);
        int month = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int year = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR));
        if (month < 0) {
            month += 12;
            year -= 1;
        }
        res[0] = year;
        res[1] = month;
        return res;
    }

    public static Date[] getNprevDateMafterDate(Date d, int date, int after, int prev) {
        Date[] result = new Date[after + prev];
        if (d == null) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < prev; i++, index++) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.setFirstDayOfWeek(2);
            int current = c.get(Calendar.DAY_OF_WEEK);
            int first = c.getFirstDayOfWeek() + date;
            c.add(Calendar.DAY_OF_MONTH, first - current - (prev - i) * 7);
            c.set(11, 0);
            c.set(12, 0);
            c.set(13, 0);
            c.set(14, 0);
            result[index] = c.getTime();
        }

        for (int i = 0; i < after; i++, index++) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.setFirstDayOfWeek(2);
            int current = c.get(Calendar.DAY_OF_WEEK);
            int first = c.getFirstDayOfWeek() + date;
            c.add(Calendar.DAY_OF_MONTH, first - current + i * 7);
            c.set(11, 0);
            c.set(12, 0);
            c.set(13, 0);
            c.set(14, 0);
            result[index] = c.getTime();
        }
        return result;
    }

    public static Date truncateTime(Date time) {
        return org.apache.commons.lang3.time.DateUtils.truncate(time, Calendar.DATE);
    }

    /**
     * 根据开始与结束的日期生成在开始与结束日期范围中，每个月的天数分配
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static Map<Integer, Integer> buildMonthDateMap(Date beginTime, Date endTime) {
        Map<Integer, Integer> resultMap = new HashMap<>();
        Date dummyBeginTime = org.apache.commons.lang3.time.DateUtils.addDays(DateUtils2.getDayBegin(beginTime), 1);
        Date dummyEndTime = org.apache.commons.lang3.time.DateUtils.addDays(DateUtils2.getDayBegin(endTime), 1);
        Integer currentCount = 1;
        Integer currentMonth = getMonth(beginTime);
        while (dummyBeginTime.before(dummyEndTime)) {
            Integer dummyMonth = getMonth(dummyBeginTime);
            if (!dummyMonth.equals(currentMonth)) {
                resultMap.put(currentMonth, currentCount);
                currentCount = 1;
                currentMonth = dummyMonth;
            } else {
                currentCount++;
            }
            dummyBeginTime = org.apache.commons.lang3.time.DateUtils.addDays(dummyBeginTime, 1);
        }
        //remain part
        resultMap.put(currentMonth, currentCount);

        return resultMap;
    }

    /**
     * 返回beginTime到endTime内的分月区间
     *
     * @param beginTime
     * @param endTime
     * @param beginList
     * @param endList
     * @param rangeNameList
     */
    public static void buildRangeList(Date beginTime, Date endTime, List<Date> beginList, List<Date> endList,
            List<String> rangeNameList) {
        beginTime = getDatePart(beginTime);
        endTime = getDatePart(endTime);
        if (beginTime.after(endTime)) {
            return;
        }
        Date temp = beginTime;
        while (!temp.after(endTime)) {
            beginList.add(temp);
            Date tempEnd = getDatePart(getMonthEndDate(temp));
            if (tempEnd.before(endTime)) {
                endList.add(tempEnd);
            } else {
                endList.add(endTime);
            }
            rangeNameList.add(concatRangeStr(beginList.get(beginList.size() - 1), endList.get(endList.size() - 1)));
            temp = getDatePart(getMonthBeginDate(org.apache.commons.lang3.time.DateUtils.addMonths(temp, 1)));
        }
    }

    private static String concatRangeStr(Date beginDate, Date endDate) {
        Date preBegin = org.apache.commons.lang3.time.DateUtils.addDays(beginDate, -1);
        Date afterEnd = org.apache.commons.lang3.time.DateUtils.addDays(endDate, 1);
        Integer month = getMonth(beginDate);
        return !getMonth(preBegin).equals(month) && !getMonth(afterEnd).equals(month) ?
                getFormattedDate(beginDate, "YYYY-MM") :
                getFormattedDate(beginDate, "YYYY-MM(MM.dd-") + getFormattedDate(endDate, "MM.dd)");


    }

    public static String monthDayWeek(Date date) {
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return month + "月" + day + "日" + "(" + SECOND_WEEK_DAYS[week] + ")";
    }


    /**
     * 两者的时间月份差距，满一个月才算一个月
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Integer[] getFullMonthGap(Date beginDate, Date endDate) {
        String validDay = DateUtils2.getFormatDate(endDate, "dd");
        String nowDay = DateUtils2.getFormatDate(beginDate, "dd");
        if (Integer.valueOf(validDay) >= Integer.valueOf(nowDay)) {
            return DateUtils2.calMongthGap(beginDate, endDate);
        } else {
            Date preValidDate = org.apache.commons.lang3.time.DateUtils.addMonths(endDate, -1);
            return DateUtils2.calMongthGap(beginDate, preValidDate);
        }
    }

    /**
     * 获得两个日期中间时间
     *
     * @param thisSundayBegin
     * @param thisSundayEnd
     * @return
     */
    public static Date getMiddle(Date thisSundayBegin, Date thisSundayEnd) {
        long midTime = (thisSundayBegin.getTime() + thisSundayEnd.getTime()) / 2;
        return new Date(midTime);
    }

    private static volatile AtomicLong index = new AtomicLong(new SecureRandom().nextInt(899));

    /**
     * 生成一个相对不会重复的时间
     *
     * @param eventDate
     * @return
     */
    public static Date getRandomTime(Date eventDate) {
        Date beginTime = getDayBegin(eventDate);
        StringBuilder sbr = new StringBuilder();
        Random random = new Random();
        sbr.append(random.nextInt(9999))
           .append(Math.abs(index.addAndGet(1)) % 900 + 100);
        long r = Long.parseLong(sbr.toString());
        return new Date(beginTime.getTime() + r);
    }

    /**
     * generate dates during range
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static List<Date> generateDateRange(Date beginTime, Date endTime) {
        if (endTime.before(beginTime)) {
            return Collections.emptyList();
        }
        List<Date> dateRange = new ArrayList<>();
        Date curBeginTime = getDayBegin(beginTime);
        Date curEndTime = getDayBegin(endTime);
        while (curBeginTime.before(curEndTime)) {
            dateRange.add(curBeginTime);
            curBeginTime = org.apache.commons.lang3.time.DateUtils.addDays(curBeginTime, 1);
        }
        dateRange.add(curEndTime);
        return dateRange;
    }

    public static int getDateComparator(Date o1, Date o2) {
        return o1.compareTo(o2);
    }

    /**
     * 多日期转化为一字符串
     *
     * @param excludeDates
     * @return
     */
    public static String convertToStr(List<Date> excludeDates) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < excludeDates.size(); i++) {
            Date date = excludeDates.get(i);
            if (i == 0) {
                sb.append(format.format(date));
            } else {
                sb.append(";" + format.format(date));
            }
        }
        return sb.toString();
    }

    /**
     * 得到两个日期的整小时数差值
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static double getHoursDifference(Date beginDate, Date endDate) {
        return Math.abs(endDate.getTime() - beginDate.getTime()) / (3600 * 1000 * 1.0);
    }



    /**
     * 如果是周末返回true，如果是周一至周五返回false
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * 将一段日子转化为对应的字符串，连续的日期，隔断的日期
     * 4/12-4/15 4/17 4.19-4/20
     */
    public static List<String> convertDatesToStringArray(List<Date> targetDates) {
        if (targetDates == null || targetDates.isEmpty()) {
            return null;
        }
        targetDates.sort(Comparator.comparing(Date::getTime));
        //利用map来存储连续时间段的首尾
        Map<Date, Date> beginAndEndTimeForString = new HashMap<>();
        Date cursorDate = targetDates.get(0);
        Date mapKeyDate = targetDates.get(0);
        int continuous = -1;
        //按顺序存储map的key，因为map没有顺序
        List<Date> orderKey = new ArrayList<>();
        while (!cursorDate.after(targetDates.get(targetDates.size() - 1))) {

            if (targetDates.contains(cursorDate)) {
                //如果包含当前游标yi期，连续性+1
                continuous++;
            } else {
                //否则放到map中，并且连续阶段起始日期重设,连续性重设-1
                if (continuous >= 0) {
                    beginAndEndTimeForString.put(mapKeyDate,
                            org.apache.commons.lang3.time.DateUtils.addDays(mapKeyDate, continuous));
                    orderKey.add(mapKeyDate);
                }

                mapKeyDate = org.apache.commons.lang3.time.DateUtils.addDays(cursorDate, 1);
                continuous = -1;

            }
            //周期性+1
            cursorDate = org.apache.commons.lang3.time.DateUtils.addDays(cursorDate, 1);
        }
        //循环结束如果continuous不为-1，那么就加最后一个
        if (continuous >= 0) {
            beginAndEndTimeForString.put(mapKeyDate, org.apache.commons.lang3.time.DateUtils.addDays(mapKeyDate, continuous));
            orderKey.add(mapKeyDate);
        }
        List<String> promptString = new ArrayList<>();

        for (Date key : orderKey) {
            Date value = beginAndEndTimeForString.get(key);
            if (!key.equals(value)) {
                promptString.add(DateUtils2.getFormatDate(key, "MM/dd") + " 至 " + DateUtils2.getFormatDate(value, "MM/dd"));
            } else {
                promptString.add(DateUtils2.getFormatDate(key, "MM/dd"));
            }

        }
        return promptString;
    }

    public static Map<Date, Date> buildToDateMap(List<Date> targetDates) {
        if (targetDates == null || targetDates.isEmpty()) {
            return Maps.newHashMap();
        }
        targetDates.sort(Comparator.comparing(Date::getTime));
        //利用map来存储连续时间段的首尾
        Map<Date, Date> beginAndEndTimeForString = new HashMap<>();
        Date cursorDate = targetDates.get(0);
        Date mapKeyDate = targetDates.get(0);
        int continuous = -1;
        //按顺序存储map的key，因为map没有顺序
        List<Date> orderKey = new ArrayList<>();
        while (!cursorDate.after(targetDates.get(targetDates.size() - 1))) {

            if (targetDates.contains(cursorDate)) {
                //如果包含当前游标yi期，连续性+1
                continuous++;
            } else {
                //否则放到map中，并且连续阶段起始日期重设,连续性重设-1
                if (continuous >= 0) {
                    beginAndEndTimeForString.put(mapKeyDate,
                            org.apache.commons.lang3.time.DateUtils.addDays(mapKeyDate, continuous));
                    orderKey.add(mapKeyDate);
                }

                mapKeyDate = org.apache.commons.lang3.time.DateUtils.addDays(cursorDate, 1);
                continuous = -1;

            }
            //周期性+1
            cursorDate = org.apache.commons.lang3.time.DateUtils.addDays(cursorDate, 1);
        }
        //循环结束如果continuous不为-1，那么就加最后一个
        if (continuous >= 0) {
            beginAndEndTimeForString.put(mapKeyDate, org.apache.commons.lang3.time.DateUtils.addDays(mapKeyDate, continuous));
            orderKey.add(mapKeyDate);
        }
        return beginAndEndTimeForString;
    }

    /**
     * 组装两个月份之间的所有月份list
     *
     * @param beginYear
     * @param beginMonth
     * @param endYear
     * @param endMonth
     * @param yearList
     * @param monthList
     */
    public static void completeYearMonthList(Integer beginYear, Integer beginMonth, Integer endYear, Integer endMonth,
            List<Integer> yearList,
            List<Integer> monthList) {
        if (beginYear > endYear) {
            return;
        }
        if (Objects.equals(beginYear, endYear) && beginMonth > endMonth) {
            return;
        }
        completePeriodList(beginYear, beginMonth, endYear, endMonth, yearList, monthList, 12);
    }

    public static void completeYearQuarterList(Integer beginYear, Integer beginMonth, Integer endYear, Integer endMonth,
            List<Integer> yearList,
            List<Integer> monthList) {
        if (beginYear > endYear) {
            return;
        }
        if (Objects.equals(beginYear, endYear) && beginMonth > endMonth) {
            return;
        }
        completePeriodList(beginYear, beginMonth, endYear, endMonth, yearList, monthList, 4);
    }

    public static void completePeriodList(int beginYear, int beginMonth, int endYear, int endMonth, List<Integer> yearList,
            List<Integer> monthList, int max) {
        for (int i = beginYear; i <= endYear; i++) {
            if (i == beginYear) {
                int end = max;
                if (beginYear == endYear) {
                    end = endMonth;
                }
                for (int j = beginMonth; j <= end; j++) {
                    yearList.add(i);
                    monthList.add(j);
                }
            } else if (i < endYear) {
                for (int j = 1; j <= max; j++) {
                    yearList.add(i);
                    monthList.add(j);
                }
            } else {
                for (int j = 1; j <= endMonth; j++) {
                    yearList.add(i);
                    monthList.add(j);
                }
            }
        }
    }

    /**
     * 一个时间是否正好几点
     *
     * @param endDate
     * @return
     */
    public static boolean isExactNoon(Date endDate, int exactHour) {
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        return c.get(Calendar.HOUR_OF_DAY) == exactHour && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0;
    }

    /**
     * 操作月份
     *
     * @param months
     * @param date
     * @return
     */
    public static Date getMonthBefore(int months, Date date) {
        if (date == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - months);
        return cal.getTime();
    }

    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static Date getWeekBegin(Integer year, Integer week) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setWeekDate(year, week, Calendar.MONDAY);
        return cal.getTime();
    }

    public static Date getWeekEnd(Integer year, Integer week) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setWeekDate(year, week, Calendar.SUNDAY);
        return cal.getTime();
    }

    public static String getDayHourStr(Double day, Double hour) {
        int fDay = (int) (day == null ? 0 : day);
        double fHour = hour == null ? 0 : hour;
        if (fDay == 0) {
            return fHour + "小时";
        }
        if (fHour == 0) {
            return fDay + "天";
        }
        if (fDay > 0) {
            return Math.abs(fDay) + "天" + Math.abs(fHour) + "小时";
        }
        return "-" + Math.abs(fDay) + "天" + Math.abs(fHour) + "小时";
    }

    public static String getDayHourStr(Integer day, BigDecimal hour) {
        int fDay = day == null ? 0 : day;
        BigDecimal fHour = hour == null ? BigDecimal.ZERO : hour;
        if (fDay == 0) {
            return fHour.setScale(3, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString() + "小时";
        }
        if (fHour.compareTo(BigDecimal.ZERO) == 0) {
            return fDay + "天";
        }
        if (fDay > 0) {
            return fDay + "天" +
                    fHour.abs().setScale(3, BigDecimal.ROUND_HALF_UP)
                         .stripTrailingZeros().toPlainString() + "小时";
        }
        return "-" + Math.abs(fDay) + "天" +
                fHour.abs().setScale(3, BigDecimal.ROUND_HALF_UP)
                     .stripTrailingZeros().toPlainString() + "小时";
    }

    public static String getDayHourStr(Integer day, Integer hour) {
        int fDay = day == null ? 0 : day;
        int fHour = hour == null ? 0 : hour;
        if (fDay == 0) {
            return fHour + "小时";
        }
        if (fHour == 0) {
            return fDay + "天";
        }
        if (fDay > 0) {
            return Math.abs(fDay) + "天" + Math.abs(fHour) + "小时";
        }
        return "-" + Math.abs(fDay) + "天" + Math.abs(fHour) + "小时";
    }


    public static final String PATTERN_A = "yyyy-MM-dd";
    public static final String PATTERN_A_1 = "yyyyMM";
    public static final String PATTERN_JQUERY = "dd/MM/yyyy";
    public static final String PATTERN_ZH = "yyyy年MM月dd日";
    public static final String PATTERN_B = "yyyyMMdd";
    public static final String PATTERN_C = "yyyy/MM/dd";
    public static final String PATTERN_D = "yyyyMM";
    public static final String PATTERN_E = "yyMM";
    public static final String PATTERN_F = "MM/dd";
    public static final String PATTERN_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_LONG_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_LONG_ZH = "yyyy年MM月dd日 HH时mm分ss秒";

    public static final String PATTERN_LONG_MS = "yyyyMMddHHmmssSSS";
    public static final String PATTERN_LONG_TIGHT = "yyyyMMddHHmmss";
    public static final String PATTERN_LONG_MINUTE_TIGHT = "yyyyMMddHHmm";

    public static final String HMS_START = " 00:00:00";
    public static final String HMS_END = " 23:59:59";

    public static final String PATTERN_LONG_SLASH = "yyyy/MM/dd HH:mm:ss";


    /**
     * 获得当前日期所在年的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayInYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得当前日期所在年的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayInYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 计算两个日期间隔的小时数
     *
     * @param firstDate 小者
     * @param lastDate  大者
     * @return
     */
    public static int getTimeIntervalHours(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }

        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / (60 * 60 * 1000));
    }

    public static double getTimeIntervalHoursDouble(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }

        double intervalMilli = lastDate.getTime() - firstDate.getTime();
        double result = intervalMilli / (60 * 60 * 1000);
        return result;
    }

    /**
     * 获取当前如期前后小时数，当前之间之前，hourSum为负数
     *
     * @param sourceDate
     * @param hourSum
     * @return
     */
    public static Date getDateAfter(Date sourceDate, Double hourSum) {
        int minuteSum = (int) (hourSum * 60);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.add(Calendar.MINUTE, minuteSum);
        return calendar.getTime();
    }

    /**
     * 获取日期对象,不包含时分秒
     *
     * @param day 偏移的日期数,整数向前,负数向后
     * @return 不含时分秒的日期对象
     */
    public static Date getMidnightOfOneDay(int day) {
        Calendar midnight = Calendar.getInstance();
        midnight.add(Calendar.DAY_OF_MONTH, day);
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        return midnight.getTime();
    }

    /**
     * 描述： 获取日期特定格式字符串 作者： yangqiang 创建时间： 2009-9-8上午08:51:07
     *
     * @param date
     * @param datePattern 字符串格式
     * @return
     * @since v0.1
     */
    public static String getDateStr(Date date, String datePattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        return format.format(date);
    }

    /**
     * 描述： 获取日期默认格式（"yyyy-MM-dd"）对应字符串 作者： yangqiang 创建时间： 2009-9-8上午08:51:11
     *
     * @param date
     * @return
     * @since v0.1
     */
    public static String getDateStr(Date date) {
        return getDateStr(date, PATTERN_A);
    }

    /**
     * 描述： 将字符串按特定格式转换为日期 作者： yangqiang 创建时间： 2009-11-3下午01:27:34
     *
     * @param date
     * @param datePattern
     * @return
     * @since v2.0
     */
    public static Date parseDate(String date, String datePattern) {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 描述： 获取某一时间的一个间隔时间 作者： yangqiang 创建时间： 2009-11-3下午01:28:21
     *
     * @param intervalType 间隔类型 年 Calendar.YEAR、月Calendar.MONTH、日Calendar.DATE
     * @param intervalVal  间隔长短
     * @param curdate      基准时间
     * @since v2.0
     */
    public static String getIntervalDate(int intervalType, int intervalVal, Date curdate) {
        if (curdate == null) {
            curdate = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curdate);
        switch (intervalType) {
            case Calendar.YEAR:
                calendar.add(Calendar.YEAR, intervalVal);
                return getDateStr(calendar.getTime());
            case Calendar.MONTH:
                calendar.add(Calendar.MONTH, intervalVal);
                return getDateStr(calendar.getTime());
            case Calendar.DATE:
                calendar.add(Calendar.DATE, intervalVal);
                return getDateStr(calendar.getTime());
            default:
                calendar.add(Calendar.MONTH, intervalVal);
                return getDateStr(calendar.getTime());
        }
    }

    /**
     * 描述： 获取特定时间所在年期
     * example：2009-11-15 为 200911
     * 作者： yangqiang
     * 创建时间： 2009-11-11下午03:30:24
     *
     * @param date
     * @since v2.0
     */
    public static int getYearPeriod(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取年期所在日历月的第一天，时间为 00:00:00 000。 如果时间小于2009年，则返回null
     *
     * @param yearPeriod
     * @return
     */
    public static Date yearPeriod2Date(int yearPeriod) {
        if (yearPeriod < 200900) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, yearPeriod / 100);
        cal.set(Calendar.MONTH, (yearPeriod % 100) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 描述： 判断是否为该月第一天 作者： yangqiang 创建时间： 2009-11-3下午02:06:37
     *
     * @param date
     * @return
     * @since v2.0
     */
    public static boolean isFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_MONTH) == 1;

    }

    /**
     * 描述： 判断是否为该月第一天 作者： yangqiang 创建时间： 2009-11-3下午02:06:37
     *
     * @param date
     * @return
     * @since v2.0
     */
    public static boolean isFirstDay(String date) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(date)) {
            return false;
        }

        return date.endsWith("-01");

    }

    public static String getDateSub(Date date, String dateSubStr) {
        SimpleDateFormat format = new SimpleDateFormat(dateSubStr); // "yyyy"
        return format.format(date);
    }

    public static String getCurDateStr(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    public static Date parseDateC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.parse(s);
    }

    /**
     * Parses text in 'YYYY-MM-DD' format to produce a date.
     *
     * @param s the text
     * @return Date
     * @throws ParseException
     */
    public static Date parseDateTime(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(s);
    }

    public static Date parseDateTimeC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return format.parse(s);
    }

    /**
     * Parses text in 'HH:mm:ss' format to produce a time.
     *
     * @param s the text
     * @return Date
     * @throws ParseException
     */
    public static Date parseTime(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.parse(s);
    }

    public static Date parseTimeC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH时mm分ss秒");
        return format.parse(s);
    }

    public static int yearOfDate(Date s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(0, 4));
    }

    public static int monthOfDate(Date s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(5, 7));
    }

    public static int dayOfDate(Date s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(8, 10));
    }

    @SuppressWarnings("deprecation")
    public static String getDateTimeStr(java.sql.Date date, double time) {
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        String dateStr = year + "-" + month + "-" + day;
        Double d = time;
        String timeStr = String.valueOf(d.intValue()) + ":00:00";

        return dateStr + " " + timeStr;
    }

    /**
     * Get the total month from two date.
     *
     * @param sd the start date
     * @param ed the end date
     * @return int month form the start to end date
     */
    @SuppressWarnings("deprecation")
    public static int diffDateM(Date sd, Date ed) {
        return (ed.getYear() - sd.getYear()) * 12 + ed.getMonth() - sd.getMonth() + 1;
    }

    public static int diffDateD(Date sd, Date ed) {
        return (int) (Math.round((double) (ed.getTime() - sd.getTime()) / 86400000) + 1);
    }

    public static int diffDateMinute(Date sd, Date ed) {
        return (int) Math.round((double) (ed.getTime() - sd.getTime()) / 60000);
    }

    public static int diffDateM(int sym, int eym) {
        return (int) ((Math.round((double) eym / 100) - Math.round((double) sym / 100)) * 12 + (eym % 100 - sym % 100) + 1);
    }

    /**
     * 前或后几个月
     *
     * @param date
     * @param count
     * @return
     */
    public static Date getDiffMonth(Date date, int count) {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, count);
        return new Date(scalendar.getTime().getTime());
    }

    public static Date getFrontDateByDayCount(Date date, int dayCount) {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.DATE, -dayCount);
        return new Date(scalendar.getTime().getTime());
    }

    /**
     * Get first day of the month.
     *
     * @param year  the year
     * @param month the month
     * @return Date first day of the month.
     * @throws ParseException
     */
    public static Date getFirstDay(String year, String month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(year + "-" + month + "-1");
    }

    public static Date firstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


    //取所在月下一月第一天
    public static Date firstDayOfNextMonth() {
        Calendar calendar = Calendar.getInstance();
        return firstDayOfNextMonth(calendar.getTime());
    }

    public static Date firstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


    /**
     * 获取指定日期当年的月份, 1-12
     */
    public static Date getMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getTime();
    }

    /**
     * 获取指定月份, 1-12
     */
    public static Date getMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getTime();
    }

    /**
     * 取今天所在月的第一天。
     *
     * @return
     * @author hewei
     */
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(Calendar.getInstance().getTime());
    }

    /**
     * 取今天所在月的最后一天
     */
    public static Date getLastDayOfMonth() {
        return getLastDayOfMonth(Calendar.getInstance().getTime());
    }

    /**
     * 取当天所在月的最后一天
     */
    public static Date getLastDayOfMonth(Date day) {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(day);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.set(Calendar.DATE, 1);
        scalendar.set(Calendar.HOUR_OF_DAY, 23);
        scalendar.set(Calendar.MINUTE, 59);
        scalendar.set(Calendar.SECOND, 59);
        Date firstDayOfNextMonth = new Date(scalendar.getTime().getTime());
        return getDateBefore(firstDayOfNextMonth, 1);
    }

    /**
     * 取当天所在月的最后一天
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar scalendar = new GregorianCalendar();
        scalendar.set(Calendar.YEAR, year);
        scalendar.set(Calendar.MONTH, month - 1);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.set(Calendar.DATE, 1);
        scalendar.set(Calendar.HOUR_OF_DAY, 23);
        scalendar.set(Calendar.MINUTE, 59);
        scalendar.set(Calendar.SECOND, 59);
        Date firstDayOfNextMonth = new Date(scalendar.getTime().getTime());
        return getDateBefore(firstDayOfNextMonth, 1);
    }

    /**
     * 取day所在月的第一天
     */
    public static Date getFirstDayOfMonth(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getFirstDay(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date rtn = null;
        try {
            rtn = format.parse(year + "-" + month + "-1");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return rtn;
    }

    public static Date getLastDay(String year, String month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(year + "-" + month + "-1");

        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.add(Calendar.DATE, -1);
        date = scalendar.getTime();
        return date;
    }

    public static Date getLastDay(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(year + "-" + month + "-1");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.add(Calendar.DATE, -1);
        date = scalendar.getTime();
        return date;
    }

    /**
     * getToday get todat string with format YYYY-MM-DD from a Date object
     *
     * @return String
     */

    // public static String getTodayStr() throws ParseException {
    // Calendar calendar = Calendar.getInstance();
    // return getDateStr(calendar.getTime());
    // }
    public static Date getToday() {
        return new Date(System.currentTimeMillis());
    }

    public static Date tuncateMillis(Date date) {
        return new Date(tuncateMillis(date.getTime()));
    }

    public static void tuncateMillis(Calendar calendar) {
        calendar.setTimeInMillis(tuncateMillis(calendar.getTimeInMillis()));
    }

    public static long tuncateMillis(long millis) {
        return (millis / 1000L * 1000L);
    }

    public static Date toDate(Long millis) {
        if (millis == null) {
            return null;
        }

        return new Date(tuncateMillis(millis));
    }

    public static String getTodayAndTime() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    // public static String getTodayC() throws ParseException {
    // Calendar calendar = Calendar.getInstance();
    // return getDateStrC(calendar.getTime());
    // }

    public static int getThisYearMonth() {
        Date today = Calendar.getInstance().getTime();
        return getYearPeriod(today);
    }

    public static int getYearMonth(Date date) {
        return getYearPeriod(date);
    }

    // 获取相隔月数
    @SuppressWarnings("deprecation")
    public static long getDistinceMonth(String beforedate, String afterdate) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        long monthCount = 0;
        try {
            Date d1 = d.parse(beforedate);
            Date d2 = d.parse(afterdate);

            monthCount = (long) (d2.getYear() - d1.getYear()) * 12 + d2.getMonth() - d1.getMonth();
        } catch (ParseException e) {
            LOGGER.error("", e);
        }
        return monthCount;
    }

    // 获取相隔天数
    public static long getDistinceDay(String beforedate, String afterdate) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        long dayCount = 0;
        try {
            Date d1 = d.parse(beforedate);
            Date d2 = d.parse(afterdate);

            dayCount = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    /**
     * 忽略小时直接的差异,只比较日期
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDaysBetween(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
    }

    // 获取相隔天数
    public static long getDistinceDay(Date beforedate, Date afterdate) {
        long dayCount = 0;

        try {
            dayCount = (long) Math.ceil(1.0 * Math.abs(afterdate.getTime() - beforedate.getTime()) / (24 * 60 * 60 * 1000));

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    public static long getDistinceDay(java.sql.Date beforedate, java.sql.Date afterdate) {
        long dayCount = 0;

        try {
            dayCount = (afterdate.getTime() - beforedate.getTime()) / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    public static long getDistinceDay(long beforedateTime, long afterdateTime) {
        long dayCount = 0;

        try {
            dayCount = (beforedateTime - afterdateTime) / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    // 获取相隔时间数
    public static long getDistinceTime(String beforeDateTime, String afterDateTime) throws ParseException {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeCount = 0;
        try {
            Date d1 = d.parse(beforeDateTime);
            Date d2 = d.parse(afterDateTime);

            timeCount = (d2.getTime() - d1.getTime()) / (60 * 60 * 1000);

        } catch (ParseException e) {
            LOGGER.error("", e);
            throw e;
        }
        return timeCount;
    }

    // 获取相隔时间数
    @SuppressWarnings("deprecation")
    public static long getDistinceTime(String beforeDateTime) throws ParseException {
        return getDistinceTime(beforeDateTime, new Timestamp(System.currentTimeMillis()).toLocaleString());
    }

    /**
     * 把cal的时间设置为 00:00:00 000
     *
     * @param cal
     */
    public static void clearTimeFiled(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 把cal的时间设置为 23:59:59 000
     *
     * @param cal
     */
    public static void setLastTimeFiled(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 1);
        clearTimeFiled(c);
        return getDateStr(c.getTime(), PATTERN_LONG);
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 7);
        setLastTimeFiled(c);
        return getDateStr(c.getTime(), PATTERN_LONG);
    }

    /**
     * 描述： 获得上周星期日的日期
     * 作者： yangqiang
     * 创建时间： 2010-7-16下午02:35:19
     *
     * @param date 为空 表示当前日期
     * @return
     * @since v2.0
     */
    public static String getPreviousWeekSunday(Date date) {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        Calendar currentDate = Calendar.getInstance();
        if (date != null) {
            currentDate.setTime(date);
        }
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        setLastTimeFiled(currentDate);

        return getDateStr(currentDate.getTime(), PATTERN_LONG);
    }

    /**
     * 描述：获得上周星期一的日期
     * 作者： yangqiang
     * 创建时间： 2010-7-16下午02:35:48
     *
     * @param date 为空 表示当前日期
     * @return
     * @since v2.0
     */
    public static String getPreviousWeekday(Date date) {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        Calendar currentDate = Calendar.getInstance();
        if (date != null) {
            currentDate.setTime(date);
        }
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        clearTimeFiled(currentDate);

        return getDateStr(currentDate.getTime(), PATTERN_LONG);
    }

    // 获得当前日期与本周日相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        return dayOfWeek == 1 ? 0 : 1 - dayOfWeek;
    }

    /**
     * 指定相隔天数
     */
    public static Date addDate(Date day, int dayCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_MONTH, dayCount);
        return calendar.getTime();
    }

    /**
     * 指定相隔年
     */
    public static Date addDateByYear(Date day, int yearCount, Boolean flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.YEAR, yearCount);
        if (flag != null) {
            if (flag) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static long getUnixTimeStamp(int dateAdd, int field) {
        Calendar c = Calendar.getInstance();
        c.add(field, dateAdd);
        return Math.round((double) c.getTime().getTime() / 1000);
    }

    public static long getUnixTimeStamp() {
        return Math.round(System.currentTimeMillis() / 1000);
    }

    /**
     * Unix时间搓转换成Stirng时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateStrByUnix(long date, String pattern) {
        return getDateStr(new Date(date * 1000), pattern);
    }

    //当天 23:59:59
    public static Date getRawDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    //当天 00:00:00
    public static Date getInitToday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDay(Integer year, Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //获取相隔年数
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTime(date);
        return cal;
    }

    public static Date getTomorrow() {
        return getDateAfter(new Date(), 1);
    }




    public static Date replaceYear(Date date, Integer year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 获取当前日期所在的季度
     */
    public static DateUtils2.QuarterType getQuarter(Date date) {
        return DateUtils2.QuarterType.match(date);

    }

    public enum QuarterType {
        QUARTER_ONE(getFirstDayOfMonth(getMonth(1)), getLastDayOfMonth(getMonth(3)), "第一季度"),
        QUARTER_TWO(getFirstDayOfMonth(getMonth(4)), getLastDayOfMonth(getMonth(6)), "第二季度"),
        QUARTER_THREE(getFirstDayOfMonth(getMonth(7)), getLastDayOfMonth(getMonth(9)), "第三季度"),
        QUARTER_FOUR(getFirstDayOfMonth(getMonth(10)), getLastDayOfMonth(getMonth(12)), "第四季度");

        private Date quarterStart;
        private Date quarterEnd;
        private String def;

        QuarterType() {
        }

        public static DateUtils2.QuarterType match(Date date) {
            if (date == null) {
                return null;
            }
            Date curDate = new Date();
            Date modifiedDate;
            //如果不在同年，则转换为同年
            Integer gap = DateUtils2.getYear(curDate) - DateUtils2.getYear(date);
            modifiedDate = gap != 0 ? DateUtils2.addYears(date, gap) : date;

            for (DateUtils2.QuarterType type : DateUtils2.QuarterType.values()) {
                if (modifiedDate.compareTo(type.getQuarterStart()) >= 0 && modifiedDate.compareTo(type.getQuarterEnd()) <= 0) {
                    return type;
                }
            }
            return null;
        }


        QuarterType(Date quarterStart, Date quarterEnd, String def) {
            this.quarterStart = quarterStart;
            this.quarterEnd = quarterEnd;
            this.def = def;
        }

        public String getDef() {
            return def;
        }

        public Date getQuarterStart() {
            return quarterStart;
        }

        public Date getQuarterStart(int year) {
            return replaceYear(quarterStart, year);
        }

        public Date getQuarterEnd() {
            return quarterEnd;
        }

        public Date getQuarterEnd(int year) {
            return replaceYear(quarterEnd, year);
        }
    }

    /**
     * 计算时间差,返回y年-M个月-d天
     */
    public static String getDuration(Date first, Date last) {
        if (first == null || last == null) {
            return "";
        }
        last = DateUtils2.addDays(last, 1);
        if (first.after(last)) {
            return "";
        }
        String period = DurationFormatUtils
                .formatPeriod(first.getTime(), last.getTime(), "y年-M个月-d天");
        String[] list = period.split("-");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            if (list[i].indexOf("0") != 0) { //第一位不是0
                sb.append(list[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 计算时间差,返回y年-M个月-d天,如有年或月，不返回天
     */
    public static String getDuration2(Date first, Date last) {
        if (first == null || last == null) {
            return "";
        }
        last = DateUtils2.addDays(last, 1);
        if (first.after(last)) {
            return "";
        }
        String period = DurationFormatUtils
                .formatPeriod(first.getTime(), last.getTime(), "y年-M个月-d天");
        String[] list = period.split("-");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            if (list[i].indexOf("0") != 0) { //第一位不是0
                if (i == 2) {
                    if (sb.length() == 0) {
                        sb.append(list[i]);
                    }
                } else {
                    sb.append(list[i]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 计算工作年限
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Integer getWorkYear(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return null;
        }

        if (endDate == null) {
            endDate = new Date();
        }
        return getDiffYears(beginDate, endDate);
    }

    /**
     * 获取起止时间内的日期列表
     */
    public static List<Date> getDateRange(Date beginDate, Date endDate) {
        beginDate = getDayBegin(beginDate);
        endDate = getDayBegin(endDate);
        List<Date> lDate = new ArrayList();
        lDate.add(beginDate);//把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);//把结束时间加入集合
        return lDate;
    }

}
