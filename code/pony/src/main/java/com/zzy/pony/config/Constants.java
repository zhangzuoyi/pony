package com.zzy.pony.config;

import java.util.Collections;
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
	public static final int RECEIVER_READ_Y=  1 ;//已读
	public static final int RECEIVER_READ_N=  0 ;//未读
	public static final String PROPERTY_STATUS_FREE=  "0" ;//资产状态  空闲
	public static final String PROPERTY_STATUS_OCCUPY=  "1" ;//资产状态  使用中
	public static final String PROPERTY_STATUS_DESTROY=  "2" ;//资产状态  作废
	public static final int RESOURCE_PARENT_LEVEL=  1 ;//资源级别 一级
	public static final int RESOURCE_CHILD_LEVEL=  2 ;//资源级别  二级
	public static final int SUBJECT_SIGNIFICANT=  1;
	public static final int SUBJECT_IMPORTANT=  2;
	public static final int SUBJECT_COMMON=  3;



	
	public static final String MESSAGE_ATTACH_UPLOADPATH = "/home/upload";
	




	public static final Map<Integer, String> SUBJECT_TYPES;
	public static final Map<String, String> USER_TYPES;
	public static final Map<Integer, String> SUBJETCS;
	public static final Map<Integer, String> PP_TYPES;//奖惩类型 
	
	private static Map<String, String> dictTypes;
	public static final String DICT_SEX = "sex";
	public static final String DICT_CREDENTIAL = "credential";
	public static final String DICT_EDU_DEGREE= "edu_degree";
	public static final String DICT_STU_REMARK_LEVEL = "stu_remark_level";
	public static final String DICT_STUDENT_TYPE = "student_type";
	public static final String DICT_STUDENT_STATUS= "student_status";
	public static final String DICT_PROPERTY_STATUS= "property_status";
	public static final String DICT_SUBJECT_TYPE= "subject_type";
	public static final String DICT_IMPORTANCE= "importance";





	
	
	public static Map<String, String> getDictTypes(){
		if (dictTypes == null) {
			dictTypes=new LinkedHashMap<String, String>();
			dictTypes.put(DICT_SEX, "性别");
			dictTypes.put(DICT_CREDENTIAL, "证件类型");
			dictTypes.put(DICT_EDU_DEGREE, "教育程度");
			dictTypes.put(DICT_STU_REMARK_LEVEL, "成绩等级");
			dictTypes.put(DICT_STUDENT_TYPE, "学生类型");
			dictTypes.put(DICT_STUDENT_STATUS, "学生状态");
			dictTypes.put(DICT_PROPERTY_STATUS, "资产状态");
			dictTypes.put(DICT_SUBJECT_TYPE, "科目类型");
			dictTypes.put(DICT_IMPORTANCE, "重要程度");

			Collections.unmodifiableMap(dictTypes);
		}
		return dictTypes;
	}
	
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
