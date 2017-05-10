package com.zzy.pony.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	public static final String FORMAL_SHORT_FORMAT="yyyyMMdd";
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
}
