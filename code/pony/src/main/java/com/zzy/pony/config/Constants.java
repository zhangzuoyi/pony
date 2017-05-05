package com.zzy.pony.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {
	public static final String CURRENT_FLAG_TRUE="0";//当前标志，是
	public static final String CURRENT_FLAG_FALSE="1";//当前标志，否
	public static final String USER_TYPE_TEACHER="t";
	public static final String USER_TYPE_STUDENT="s";
	public static final String ID_TYPE_DEFAULT="01";//默认证件类型，就是身份证
	public static final Integer HAVECLASS_FLAG_FALSE=0;//是否上课，否
	public static final Integer HAVECLASS_FLAG_TRUE=1;//是否上课，是
	public static final Map<String, String> WEEKDAYS;
	public static final Map<String, String> WEEKDAYMAP;
	public static final String SOURCE_TYPE_PRE="0";//预排课
	public static final String SOURCE_TYPE_AUTO="1";//自动排课
	public static final String SOURCE_TYPE_CHANGE="2";//调课
	public static final String USER_GROUP_TYPE_TEACHER="1";//用户组类型:老师
	public static final String USER_GROUP_TYPE_STUDENT="2";//用户组类型:学生
	public static final int IS_VALID_Y=  0 ;//有效
	public static final int IS_VALID_N=  1 ;//无效
	public static final String RECEIVER_TYPE_GROUP=  "0" ;//组
	public static final String RECEIVER_TYPE_USER=  "1" ;//个人
	
	public static final String MESSAGE_ATTACH_UPLOADPATH = "/home/upload";
	




	public static final Map<Integer, String> SUBJECT_TYPES;
	public static final Map<String, String> USER_TYPES;
	public static final Map<Integer, String> SUBJETCS;
	public static final Map<Integer, String> PP_TYPES;//奖惩类型 
	
	static{
		WEEKDAYS=new LinkedHashMap<String, String>();
		WEEKDAYS.put("1", "星期一");
		WEEKDAYS.put("2", "星期二");
		WEEKDAYS.put("3", "星期三");
		WEEKDAYS.put("4", "星期四");
		WEEKDAYS.put("5", "星期五");
		WEEKDAYS.put("6", "星期六");
		WEEKDAYS.put("7", "星期七");
		
		WEEKDAYMAP=new LinkedHashMap<String, String>();
		WEEKDAYMAP.put("1", "Monday");
		WEEKDAYMAP.put("2", "Tuesday");
		WEEKDAYMAP.put("3", "Wednesday");
		WEEKDAYMAP.put("4", "Thrusday");
		WEEKDAYMAP.put("5", "Friday");
		WEEKDAYMAP.put("6", "Saturday");
		WEEKDAYMAP.put("7", "Sunday");
		
		
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
		
		PP_TYPES=new LinkedHashMap<Integer, String>(); 
		PP_TYPES.put(0, "奖"); 
		PP_TYPES.put(1, "惩"); 
	}
}
