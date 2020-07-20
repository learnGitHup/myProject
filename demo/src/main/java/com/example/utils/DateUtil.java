package com.example.utils;

import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @program: issue_demo
 * @description
 * @author: wl
 * @create: 2019-08-12 14:51
 **/
public class DateUtil {
	public static final String timePattern = "yyyy-MM-dd HH:mm:ss";
	public static final String datePattern = "yyyyMMdd";
	public static final String PATTENR_TOS="yyyy-MM-dd'T'HH:mm:ss";
	public static final String FORMAT_SHORTG = "yyyy-MM-dd";
	public static final String PATTENR_TOZERO="yyyy-MM-dd'T'HH:mm:ss.SSS+0000";
	public static final String PATTENR_TOEIGHT="yyyy-MM-dd'T'HH:mm:ss.SSS+0800";
	public static final String pattern_noSymbol="yyyyMMdd HHmmss";
	public static final String pattern_noSpace="yyyyMMddHHmmss";
	public static final String pattern_ms="yyyyMMddHHmmssSSS";
	public static final String pattern_minus_ver="yyyyMMddHHmm";
	public static final String pattern_hours_ver="yyyyMMddHH";

	/**
	 * 获取指定模板的日期时间字符串
	 *
	 * @param pattern
	 * @return
	 */

	public static String getDateTimeStr(String pattern) {
		String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
		return dateTimeStr;
	}


	public static String getDateByPostTime(String postTime) {
		String dateStr = LocalDateTime.parse(postTime).format(DateTimeFormatter.ofPattern(datePattern));
		return dateStr;
	}

	/**
	 * 字符串转LocalDateTime
	 * @param dateTimeStr
	 * @param pattern
	 * @return
	 */
	public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * localDateTime 转 string
	 * @param dateTime
	 * @param pattern
	 * @return
	 */
	public static String localDateTimeParseStr(LocalDateTime dateTime, String pattern){
		return dateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * date转localDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime dateToLocalDateTime(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * 获取指定格式的公元元年
	 * @param pattern yyyy-MM-dd'T'HH:mm:ss.SSS+0000
	 * @return
	 */
	public static Date getFirstYear(String pattern){
		return Date.from(parseLocalDateTime(DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(0)), pattern).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 将日期格式的字符串转换为date
	 * @param dateStr 日期字符串(只能到日)
	 * @param pattern
	 * @return
	 */
	public static Date strToDate(String dateStr,String pattern){
		return Date.from(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}


	/**
	 * 去除日期字符串中间的T
	 */
	public static String replaceT(String dateStr) {
		if (StringUtils.isEmpty(dateStr)){
			return "";
		}
		if (dateStr != null && !"".equals(dateStr)) {
			if (dateStr.contains("T")) {
				return dateStr.replace("T", " ");
			}
		}
		return dateStr;
	}


	/**
	 * 去除时间中的T - ：
	 * @param dateStr
	 * @return
	 */
	public static String replaceAll(String dateStr){
		if (StringUtils.isEmpty(dateStr)){
			return "";
		}
		if (dateStr != null && !"".equals(dateStr)) {
			if (dateStr.contains("T")) {
				dateStr = dateStr.replace("T", " ");
			}
			if (dateStr.contains("-")){
				dateStr = dateStr.replace("-", "");
			}
			if (dateStr.contains(":")){
				dateStr = dateStr.replace(":", "");
			}
			if(!dateStr.contains(" ")){
				dateStr=dateStr.substring(0,8)+" "+dateStr.substring(8,dateStr.length());
			}
		}
		return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd HHmmss")).format(DateTimeFormatter.ofPattern("yyyyMMdd HHmmss"));
	}

	/**
	 * 日期格式转换   19111010->1911-10-10
	 * @param oldStr
	 * @return
	 */
	public static String getDateStr(String oldStr){
		return LocalDate.parse(oldStr,DateTimeFormatter.ofPattern(DateUtil.datePattern)).format(DateTimeFormatter.ofPattern(DateUtil.FORMAT_SHORTG));
	}

	/**
	 * 日期转date
	 * @param oldStr
	 */
	public static Date toDate(String oldStr){
		LocalDateTime parse = LocalDateTime.parse(oldStr, DateTimeFormatter.ofPattern(PATTENR_TOS));
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = parse.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
		Date date = Date.from(zdt.toInstant());
		return date;
	}

	/**
	 * 传入格式必须为yyyy-MM-dd'T'HH:mm:ss 否则报错
	 * @param str
	 * @return
	 */
	public static String removeT(String str){
		if (StringUtils.isEmpty(str)){
			return "";
		}
		return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DateUtil.PATTENR_TOS)).format(DateTimeFormatter.ofPattern(pattern_noSpace));
	}

	public static void main(String[] args) {
		Date date1 = toDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTENR_TOS)));

		Date date = strToDate("2019-11-30T11:26:01", "yyyy-MM-dd'T'HH:mm:ss");
		System.out.println(date);

		LocalTime hHmmss = LocalTime.parse("221101", DateTimeFormatter.ofPattern("HHmmss"));
		System.out.println(hHmmss);

		String sadasd = "132123142";
		String substring2 = sadasd.substring(sadasd.length()-6);
		System.out.println(substring2);


		LocalDate parse = LocalDate.parse("20191127",DateTimeFormatter.ofPattern("yyyyMMdd"));
		System.out.println(parse);


		String s = "20191127001|1120191127";
		String[] split = s.split("\\|");
		int length = split[0].length();
		System.out.println(length);
		if (split[0].length()!=11){
			System.out.println("长度不正确");
		}
		String substring = split[0].substring(0, 8);
		System.out.println(substring);
		String substring1 = split[1].substring(2, 10);
		System.out.println(substring1);

	}

}
