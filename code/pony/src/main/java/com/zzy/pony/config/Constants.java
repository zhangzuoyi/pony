package com.zzy.pony.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {
	public static final String STUDENT_STATUS_DEFAULT="0";//学生状态，默认在读
	public static final String STUDENT_STATUS_LEAVE="1";//学生状态，离校
	public static final String CURRENT_FLAG_TRUE="0";//当前标志，是
	public static final String CURRENT_FLAG_FALSE="1";//当前标志，否
	public static final String USER_TYPE_TEACHER="t";
	public static final String USER_TYPE_STUDENT="s";
	public static final String ID_TYPE_DEFAULT="01";//默认证件类型，就是身份证
	public static final Map<String, String> WEEKDAYS;
	public static final Map<Integer, String> SUBJECT_TYPES;
	public static final Map<String, String> USER_TYPES;
	public static final Map<Integer, String> SUBJETCS;

	
	static{
		WEEKDAYS=new LinkedHashMap<String, String>();
		WEEKDAYS.put("1", "星期一");
		WEEKDAYS.put("2", "星期二");
		WEEKDAYS.put("3", "星期三");
		WEEKDAYS.put("4", "星期四");
		WEEKDAYS.put("5", "星期五");
		
		SUBJECT_TYPES=new LinkedHashMap<Integer, String>();
		SUBJECT_TYPES.put(0, "主科目");
		SUBJECT_TYPES.put(1, "上课科目");
		SUBJECT_TYPES.put(2, "选修科目");
		SUBJECT_TYPES.put(3, "考试科目");
		
		USER_TYPES=new LinkedHashMap<String, String>();
		USER_TYPES.put(USER_TYPE_TEACHER, "老师");
		USER_TYPES.put(USER_TYPE_STUDENT, "学生");
		
		SUBJETCS=new LinkedHashMap<Integer, String>();
		SUBJETCS.put(1, "Chinese");
		SUBJETCS.put(2, "Math");
		SUBJETCS.put(3, "English");
		SUBJETCS.put(4, "PE");
		SUBJETCS.put(5, "Physics");
		SUBJETCS.put(6, "Chemistry");
		
	}
}
