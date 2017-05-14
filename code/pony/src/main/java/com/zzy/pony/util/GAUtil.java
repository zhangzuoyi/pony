package com.zzy.pony.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.id.IntegralDataTypeHolder;

import com.zzy.pony.AutoClassArrange.DNA;
import com.zzy.pony.model.LessonArrange;
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
	public static boolean isExistClass(Map<Integer, String> randomMap,int classNumber,String key ){
		
		int ceil =  (classNumber/7+1)*7;
		int floor =  classNumber-classNumber%7;
		for (int i = floor+1; i <= ceil; i++) {
			if (randomMap.containsKey(i)&&randomMap.get(i).equalsIgnoreCase(key)) {
				return true;
			}
		}
		
		return false;
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
