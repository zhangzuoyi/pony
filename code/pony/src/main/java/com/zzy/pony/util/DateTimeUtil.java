package com.zzy.pony.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	public static final String FORMAL_SHORT_FORMAT="yyyyMMdd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String FORMAL_FORMAT="yyyy-MM-dd";
	public static final String FORMAL_LONG_FORMAT="yyyy-MM-dd HH:mm:ss";

	public static String dateToStr(Date date){
		SimpleDateFormat fmt=new SimpleDateFormat(FORMAL_FORMAT);
		return fmt.format(date);
	}
	public static String dateToStr(Date date,String format){
		SimpleDateFormat fmt=new SimpleDateFormat(format);
		return fmt.format(date);
	}
	/**
	 * 把日期字符串转成Date
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date strToDate(String date, String patten){
		String format = patten.substring(0, date.length());
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.util.Date dt = null;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			return null;
			//e.printStackTrace();
		}
		return dt;
	}
	/**
	 * 星期几
	 * @param date
	 * @return
	 */
	public static String getWeekDayName(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		int i=cal.get(Calendar.DAY_OF_WEEK);
		if(i == 1){
			return "星期日";
		}else if(i == 2){
			return "星期一";
		}else if(i == 3){
			return "星期二";
		}else if(i == 4){
			return "星期三";
		}else if(i == 5){
			return "星期四";
		}else if(i == 6){
			return "星期五";
		}else if(i == 7){
			return "星期六";
		}
		return "";
	}
	
}
