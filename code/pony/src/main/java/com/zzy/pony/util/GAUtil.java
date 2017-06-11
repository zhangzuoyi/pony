package com.zzy.pony.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.zzy.pony.AutoClassArrange.DNA;
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ClassNoCourseVo;
import com.zzy.pony.vo.GradeNoCourseVo;
import com.zzy.pony.vo.SubjectNoCourseVo;
import com.zzy.pony.vo.TeacherNoCourseVo;
import com.zzy.pony.vo.TeacherSubjectVo;

public class GAUtil {

	
	
	/*** 
	* <p>Description:获取候选数组,bit为位数,list为id集合，如teacherId flag是否增加空元素，在teacher和subject时为true，用于班级不排课设置</p>
	* @author  WANGCHAO262
	* @date  2017年4月7日 上午10:22:30
	*/
	public static String[] getCandidateStrings(List<Integer> list ,int bit,Boolean flag){
		if (!flag) {
			String[] result  = new String[list.size()];
			for (int i = 0; i < result.length; i++) {
				String idStr = String.format("%0"+bit+"d", list.get(i));
				System.out.println(idStr);
				result[i] =idStr;
			}
			return result;
		}else{
			String[] result  = new String[list.size()+1];
			for (int i = 0; i < result.length-1; i++) {
				String idStr = String.format("%0"+bit+"d", list.get(i));
				System.out.println(idStr);
				result[i] =idStr;
			}
			result[result.length-1] = String.format("%0"+bit+"d", Integer.valueOf(0));
			return result;
		}						
	}
	/*** 
	* <p>Description: 获取班级老师与课时的映射关系  key:teacherId+classId+subjectId value:weekArrange</p>
	* @author  WANGCHAO262
	* @date  2017年4月10日 下午3:23:39
	* 
	* modify weekArrange可以为2+1形式，表示2次独立的课，1次连排课,将返回类型修改
	* 
	*/
	public static Map<String,String> getTeacherSubjectweekArrange(List<TeacherSubjectVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (TeacherSubjectVo teacherSubjectVo : list) {			
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String classId=String.format("%03d", teacherSubjectVo.getClassId()) ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;			
			if (teacherSubjectVo.getWeekArrange() == null) {
				teacherSubjectVo.setWeekArrange("0");
			}
			result.put(teacherId+classId+subjectId, teacherSubjectVo.getWeekArrange());
		}		
		return result;
		
	}
	
	/**
	 * @param list   key teacherId+subjectId value(key classId value count(计数，初始为0))
	 * @return
	 */
	public static Map<String, Map<String, Integer>>	 getTeacherSubjectClass(List<TeacherSubjectVo> list){
		Map<String, Map<String, Integer>> result =  new HashMap<String, Map<String,Integer>>();
		for (TeacherSubjectVo teacherSubjectVo : list) {
			 
				
			
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String classId=String.format("%03d", teacherSubjectVo.getClassId()) ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;	
			String key = teacherId+subjectId;
			if (result.containsKey(key)) {
				result.get(key).put(classId, 0);
			}else {
				Map<String, Integer> innerMap =  new HashMap<String, Integer>();
				innerMap.put(classId, 0);
				result.put(key, innerMap);
			}
			}
		
		
		return result;
	}
	/**
	 * @param list   key teacherId+subjectId value(list<integer> 星期)
	 * @return
	 */
	public static Map<String, Set<Integer>>	 getTeacherSubjectRegularClass(List<TeacherSubjectVo> list){
		Map<String, Set<Integer>> result =  new HashMap<String,Set<Integer>>();
		Random random = new Random();
		for (TeacherSubjectVo teacherSubjectVo : list) {
			if (teacherSubjectVo.getSubjectName().equalsIgnoreCase("语文")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("数学")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("英语")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("物理")
					||teacherSubjectVo.getSubjectName().equalsIgnoreCase("化学")
					){						
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;	
			String key = teacherId+subjectId;
			int weekarranges = 0;
			String weekarrange = teacherSubjectVo.getWeekArrange();
			if (weekarrange.indexOf("+")>0) {
				String[] a = weekarrange.split("\\+");
				weekarranges =  Integer.valueOf(a[0]) + Integer.valueOf(a[1]);				
			}else {
				weekarranges = Integer.valueOf(weekarrange);
			}	
			Set<Integer> weekSet = new HashSet<Integer>();
			while(weekSet.size()<weekarranges){
				weekSet.add(random.nextInt(5)+1);
			}		
			result.put(key, weekSet);			
		}
		}
		
		return result;
	}
	
	
	/**
	 * @param list   key teacherId+subjectId value classId   获取不平课的班级，即某个老师在A班上3节课，在B班上4节课
	 * @return
	 */
	public static Map<String, Map<String, Integer>>	 getTeacherSubjectIrregularClass(List<TeacherSubjectVo> list){
		Map<String, Map<String, Integer>> tmp =  new HashMap<String, Map<String,Integer>>();
		Map<String, Map<String, Integer>> result =  new HashMap<String, Map<String,Integer>>();

		for (TeacherSubjectVo teacherSubjectVo : list) {
			String teacherId = String.format("%04d", teacherSubjectVo.getTeacherId())  ;
			String classId=String.format("%03d", teacherSubjectVo.getClassId()) ;
			String subjectId =String.format("%02d", teacherSubjectVo.getSubjectId())  ;	
			int weekarranges = 0;
			String weekarrange = teacherSubjectVo.getWeekArrange();
			if (weekarrange.indexOf("+")>0) {
				String[] a = weekarrange.split("\\+");
				weekarranges =  Integer.valueOf(a[0]) + 2*Integer.valueOf(a[1]);				
			}else {
				weekarranges = Integer.valueOf(weekarrange);
			}						
			String key = teacherId+subjectId;
			if (tmp.containsKey(key)) {
				for (String classKey : tmp.get(key).keySet()) {
					if (tmp.get(key).get(classKey) !=  weekarranges) {
						if (!result.containsKey(key)) {
							Map<String, Integer> innerMap = new HashMap<String, Integer>();
							innerMap.put(classId, 0);
							innerMap.put(classKey, 0);
							result.put(key, innerMap);							
						}else {							
							result.get(key).put(classId, 0);							
						}																	
					}
				}  
				
				tmp.get(key).put(classId, weekarranges);
				
			}else {
				Map<String, Integer> innerMap =  new HashMap<String, Integer>();								
				innerMap.put(classId, weekarranges);
				tmp.put(key, innerMap);
			}
		}
		
		return result;
	}
	
	/*** 
	* <p>Description:
	* input   key:teacherId+classId+subjectId value weekArrange
	* output  key:classId value( key:teacherId+subjectId value:weekArrange) </p>
	* @author  WANGCHAO262
	* @date  2017年5月3日 下午2:14:46
	*/
	public static Map<String, Map<String, String>> getClassTeacherSubjectweekArrange(Map<String,String> map){
		Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();
		for (String key : map.keySet()) {
			String classId = key.substring(4, 7);//key
			if (result.containsKey(classId)) {
				Map<String, String> innerMap = result.get(classId);
				innerMap.put(key.substring(0, 4)+key.substring(7, 9), map.get(key));
				
			}else{
				Map<String, String> innerMap = new HashMap<String, String>();
				innerMap.put(key.substring(0, 4)+key.substring(7, 9), map.get(key));
				result.put(classId, innerMap);
			}		
		}
		return result;		
	}
	/*** 
	* <p>Description: 获取seq</p>
	* @author  WANGCHAO262
	* @date  2017年6月8日 上午9:14:54
	*/
	public static String getSeq(int classNumber,int seqLength){
		
		int result = seqLength - (classNumber-1)%seqLength ;		
		return result+"";
	}
	/*** 
	* <p>Description:获取星期几 </p>
	* @author  WANGCHAO262
	* @date  2017年6月9日 下午3:42:38
	*/
	public static Integer getWeek(int classNumber,int seqLength){
		
		int result = 5- (classNumber-1)/seqLength  ;		
		return result;
	}
	
	/*** 
	* <p>Description: seqSubjectMap:seq与subject映射关系,classNumber key:techerId+subjectId</p>
	* @author  wangchao262
	* @date  2017年6月7日 下午5:26:22
	*/
	public static boolean  isSeqSubjectMatch(Map<String, List<String>> seqSubjectMap,int classNumber,int seqLength,String key,Set<Integer> alreadyClassNumber,
			List<Integer> significantSeq,List<Integer> importantSeq,List<Integer> commonSeq,Map<String, Integer> subjectImportanceMap){
		String seq = getSeq(classNumber, seqLength);	
		List<String> subjectList = seqSubjectMap.get(seq);
		for (String string : subjectList) {
			if (string.equalsIgnoreCase(key.substring(4))) {
				return true;
			}
		}
		//若上述条件不满足，在判断当天的   非常重要--》重要--》一般
		int week = getWeek(classNumber, seqLength); 
		if (Constants.SUBJECT_SIGNIFICANT==subjectImportanceMap.get(key.substring(4))) {
			for (Integer seqInt : significantSeq) {
				if (!alreadyClassNumber.contains((5-week)*seqLength+(seqLength-seqInt+1))) {
					return false;
				}								
			}	
			if(importantSeq.contains(Integer.valueOf(seq))){
				return true;
			}
			for (Integer seqInt : importantSeq) {
				if (!alreadyClassNumber.contains((5-week)*seqLength+(seqLength-seqInt+1))) {
					return false;
				}								
			}
			if(commonSeq.contains(Integer.valueOf(seq))){
				return true;
			}									
		}
		if (Constants.SUBJECT_IMPORTANT==subjectImportanceMap.get(key.substring(4))) {
			for (Integer seqInt : importantSeq) {
				if (!alreadyClassNumber.contains((5-week)*seqLength+(seqLength-seqInt+1))) {
					return false;
				}								
			}
			if(commonSeq.contains(Integer.valueOf(seq))){
				return true;
			}									
		}
		
		
		
		
		
		return false;
	}
	
	
	/**
	 * @param randomMap   已经排好的课程 
	 * @param classNumber 即将安排的课程序号
	 * @param key 即将安排的课程
	 *                    35 28 21 14 7
						  34 27 20 13 6
						  33 26 19 12 5
						  32 25 18 11 4

						  31 24 17 10 3
						  30 23 16  9 2
						  29 22 15  8 1
	 * @return
	 */
	public static boolean isExistClass(Map<Integer, String> randomMap,int classNumber,String key,int seqLength ){
		int ceil = 0;
		int floor = 0;
		
		if (classNumber%seqLength ==0 ) {
			ceil = classNumber;
			floor = classNumber-seqLength;
		}else{
			ceil =  (classNumber/seqLength+1)*seqLength;
			floor =  classNumber-classNumber%seqLength;
		}	
		for (int i = floor+1; i <= ceil; i++) {
			if (randomMap.containsKey(i)&&randomMap.get(i).equalsIgnoreCase(key)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static  boolean isMorning(int classNumber,int seqLength, int seqMornigLength){
		
		if ((classNumber>=(seqLength-seqMornigLength+1) && classNumber<=seqLength)||(classNumber>=(seqLength*2-seqMornigLength+1) && classNumber<=seqLength*2)||(classNumber>=(seqLength*3-seqMornigLength+1) && classNumber<=seqLength*3)||(classNumber>=(seqLength*4-seqMornigLength+1) && classNumber<=seqLength*4)||(classNumber>=(seqLength*5-seqMornigLength+1) && classNumber<=seqLength*5)) {
			return true;
		}
		
		return false;
	}
	public static boolean isAfternoon(int classNumber,int seqLength, int seqAfternoonLength){
		
		if ((classNumber>=1 && classNumber<=seqAfternoonLength)||(classNumber>=(seqLength+1) && classNumber<=(seqLength+seqAfternoonLength))||(classNumber>=(seqLength*2+1) && classNumber<=(seqLength*2+seqAfternoonLength))||(classNumber>=(seqLength*3+1) && classNumber<=(seqLength*3+seqAfternoonLength))||(classNumber>=(seqLength*4+1) && classNumber<=(seqLength*4+seqAfternoonLength))) {
			return true;
		}
		
		return false;
	}
	public static boolean isInWeekSet(int classNumber,Set<Integer> weekSet,int seqLength){
		
		for (Integer integer : weekSet) {
			switch (integer) {
			case 5:
				if (classNumber>=1&&classNumber<=seqLength) {
					return true;
				}
				break;
			case 4:
				if (classNumber>=(seqLength+1)&&classNumber<=(2*seqLength)) {
					return true;
				}
				break;
			case 3:
				if (classNumber>=(2*seqLength+1)&&classNumber<=(3*seqLength)) {
					return true;
				}
				break;
			case 2:
				if (classNumber>=(3*seqLength+1)&&classNumber<=(4*seqLength)) {
					return true;
				}
				break;
			case 1:
				if (classNumber>=(4*seqLength+1)&&classNumber<=(5*seqLength)) {
					return true;
				}
				break;	
			default:
				break;
			}						
		}
		
		return false;
	}
	
	public static Map<String, String> classInMorning(List<Subject> subjects){
		Map<String, String> result = new HashMap<String, String>();
		for (Subject subject : subjects) {
			if ("语文".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}
			if ("数学".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}
			if ("英语".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}			
		}				
		return result;		
	}
	
	public static Map<String, String> classInAfternoon(List<Subject> subjects){
		Map<String, String> result = new HashMap<String, String>();
		for (Subject subject : subjects) {
			if ("政治".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}
			if ("历史".equalsIgnoreCase(subject.getName())) {
				result.put(String.format("%02d", subject.getSubjectId()), subject.getName());
			}						
		}				
		return result;		
	}
	public static Map<String, String> sortMapByVPriority(Map<String, String> oriMap,Map<String, String> classInMorning,Map<String, String> classInAfternoon){
		 Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
		    if (oriMap != null && !oriMap.isEmpty()) { 
		    	for (String key : oriMap.keySet()) {
					if(classInMorning.containsKey(key.substring(4, 6))){
						sortedMap.put(key, oriMap.get(key));
					}
					if(classInAfternoon.containsKey(key.substring(4, 6))){
						sortedMap.put(key, oriMap.get(key));
					}
				}
		    	for (String key  : oriMap.keySet()) {
					if (!sortedMap.containsKey(key)) {
						sortedMap.put(key, oriMap.get(key));
					}
				}
	       
		    }  
		    return sortedMap;
		
	}
	
	
	
	
	public static Map<String, String> sortMapByValue(Map<String, String> oriMap){
		 Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
		    if (oriMap != null && !oriMap.isEmpty()) { 		    	
		        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());  
		        Collections.sort(entryList,  
		                new Comparator<Map.Entry<String, String>>() {  
		                    public int compare(Entry<String, String> entry1,  
		                            Entry<String, String> entry2) {  
		                        int value1 = 0, value2 = 0;  
		                        try {  
		                            if (entry1.getValue().indexOf("+")>0) {
										String[] a = entry1.getValue().split("\\+");
										value1 = Integer.valueOf(a[0])+2*Integer.valueOf(a[1]);
									}else {
										value1 = Integer.valueOf(entry1.getValue());
									}
		                            if (entry2.getValue().indexOf("+")>0) {
										String[] b = entry2.getValue().split("\\+");
										value2 = Integer.valueOf(b[0])+2*Integer.valueOf(b[1]);
									}else {
										value2 = Integer.valueOf(entry2.getValue());
									}	                        	 
		                        } catch (NumberFormatException e) {  
		                            value1 = 0;  
		                            value2 = 0;  
		                        } 		                        		                        		                        		                        
		                        return value2 - value1;  
		                    }  
		                });  
		        Iterator<Map.Entry<String, String>> iter = entryList.iterator();  
		        Map.Entry<String, String> tmpEntry = null;  
		        while (iter.hasNext()) {  
		            tmpEntry = iter.next();  
		            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
		        }  
		    }  
		    return sortedMap;
		
	}
	
	
	public static Map<String,String> getClassNoCourse(List<ClassNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (ClassNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			String classId=String.format("%03d", vo.getClassId()) ;
			String periodId =String.format("%01d", vo.getLessonPeriodId())  ;			
			
		result.put(classId, seqId+periodId);
		}		
		return result;
		
	}
	public static Map<String,String> getTeacherNoCourse(List<TeacherNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (TeacherNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			String teacherId=String.format("%04d", vo.getTeacherId()) ;
			String periodId =String.format("%01d", vo.getLessonPeriodId())  ;			
			
		result.put(teacherId, seqId+periodId);
		}		
		return result;
		
	}
	public static Map<String,String> getSubjectNoCourse(List<SubjectNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (SubjectNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			
			String periodId =String.format("%01d", vo.getLessonPeriodId())  ;
			String subjectId =String.format("%02d", vo.getSubjectId())  ;
			List<Integer> gradeClassIds = vo.getGradeClassIds();			
			for (Integer gradeClassId : gradeClassIds) {
				String classId= String.format("%3d", gradeClassId);
				result.put(classId+subjectId, seqId+periodId);
			}
		}		
		return result;
		
	}
	public static Map<String,String> getGradeNoCourse(List<GradeNoCourseVo> list){
		Map<String, String> result =  new HashMap<String, String>();
		for (GradeNoCourseVo vo : list) {			
			String seqId = String.format("%01d", vo.getWeekdayId())  ;
			String periodId =String.format("%01d", vo.getLessonPeriodId())  ;
			List<Integer> gradeClassIds = vo.getGradeClassIds();			
			for (Integer gradeClassId : gradeClassIds) {
				String classId= String.format("%3d", gradeClassId);
				result.put(classId, seqId+periodId);
			}			
		}		
		return result;
		
	}
	
	public static List<ArrangeVo> getLessonArranges(String bestChromosome){
		List<ArrangeVo> result = new ArrayList<ArrangeVo>();		
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
	    int weekdayIdBit = DNA.getInstance().getWeekdayIdBit();
	    int seqIdBit = DNA.getInstance().getSeqIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {				
				//一个上课单元
				ArrangeVo vo = new ArrangeVo();
				String dnaString = bestChromosome.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				
				Integer teacherId = Integer.valueOf(dnaString.substring(0, teacherIdBit)); 
				Integer classId = Integer.valueOf(dnaString.substring(teacherIdBit, teacherIdBit+classIdBit)); 
				Integer subjectId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit, teacherIdBit+classIdBit+subjectIdBit)); 
				Integer weekdayId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit)); 
				Integer seqId = Integer.valueOf(dnaString.substring(teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit, teacherIdBit+classIdBit+subjectIdBit+weekdayIdBit+seqIdBit)); 
				vo.setTeacherId(teacherId);
				vo.setClassId(classId);
				vo.setSubjectId(subjectId);
				vo.setWeekdayId(weekdayId);
				vo.setSeqId(seqId);
				vo.setSourceType("1");
				result.add(vo);
				
				}
			}
		}
	
		return result;
	}
	public static void print(String a){
		System.out.println();	

		for (int i = 0; i < 140; i++) {		
			System.out.print(a.substring(i*11, (i+1)*11)+"  ");
			if (i>0 && (i+1)%7==0) {
			System.out.println();	
			}
		}
	}
	public static void print2(String a){
		System.out.println();	

		for (int i = 0; i < 35; i++) {		
			System.out.print(a.substring(i*11, (i+1)*11)+"  ");
			if (i>0 && (i+1)%7==0) {
			System.out.println();	
			}
		}
	}
	
	
	 
	
	
	
	public static void main(String[] args){
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			list.add(new Integer(i));
		}		
		getCandidateStrings(list, 4,true);
	}
}
