package com.zzy.pony.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	public static final String FORMAL_FORMAT="yyyy-MM-dd";
	public static String dateToStr(Date date){
		SimpleDateFormat fmt=new SimpleDateFormat(FORMAL_FORMAT);
		return fmt.format(date);
	}
}
