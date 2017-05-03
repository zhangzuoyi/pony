package com.zzy.pony.AutoClassArrange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.zzy.pony.util.GAUtil;





public class DNA {
	//资源池    单例模式
	private  int teacherIdBit = 4;
	private  int subjectIdBit = 2;
	private  int weekdayIdBit = 1;
	private  int seqIdBit = 1;
	private  int classIdBit = 3;
	private  int dnaBit = 11;

	private String[] teacherIdCandidate;  
	private String[] subjectIdCandidate;
	private String[] weekdayIdCandidate;
	private String[] seqIdCandidate;
	private String[] classIdCandidate;
	private Map<String,Integer> TeacherSubjectweekArrange;//班级老师与课时的映射关系  key:teacherId+classId+subjectId value:weekArrange
	private Map<String, String> classNoCourse;
	private Map<String, String> teacherNoCourse;
	private Map<String, String> subjectNoCourse;
	private Map<String, String> gradeNoCourse;

	
	private String dnaString;//基因序列   由1个班组成，课表安排形成一个最小单位  4位teacherId  3位classId 2位subjectId  1位星期weekday   1位课时seq
	
	
	private static DNA instance = new DNA();
	private DNA(){}
	public static DNA getInstance(){
		return instance;
	}

	/*** 
	* <p>Description:获取一个dna个体,index为classIdCandidate的索引 </p>
	* @author  WANGCHAO262
	* @date  2017年4月7日 下午2:22:42
	*/
	public String getDnaString(int classIndex){
		StringBuilder sb = new StringBuilder();
		Random random  = new Random();
		for (int i = 0; i < this.weekdayIdCandidate.length; i++) {
			for (int j = 0; j < this.seqIdCandidate.length; j++) {				
				String noClassTeacherString =  this.teacherIdCandidate[random.nextInt(this.teacherIdCandidate.length)] ;
				sb.append(noClassTeacherString);
				sb.append(this.classIdCandidate[classIndex]);
				
				if (noClassTeacherString.equalsIgnoreCase("0000")) {
					sb.append("00"); //0000classId00weekdayIdseqId为不上课编码
				}else {
					sb.append(this.subjectIdCandidate[random.nextInt(this.subjectIdCandidate.length)]);
				}
				
				sb.append(this.weekdayIdCandidate[i]);
				sb.append(this.seqIdCandidate[j]);	
			}
				
		}			
		this.dnaString = sb.toString();		
		System.out.println(this.dnaString);
		return this.dnaString;	
	}
	/*** 
	* <p>Description:获取一个dna个体,index为classIdCandidate的索引  key:teacherId+classId+subjectId value:weekArrange </p>
	* @author  WANGCHAO262
	* @date  2017年4月7日 下午2:22:42
	*/
	public String getDnaStringRuleTwo(int classIndex,Map<String, Map<String, Integer>> map){
		StringBuilder sb = new StringBuilder();
		Random random  = new Random();
		int k = this.weekdayIdCandidate.length * this.seqIdCandidate.length;//总时间段数 5*7
		//key:classId value( key:teacherId+subjectId value:weekArrange)	
		Map<String, Integer> classMap = map.get(this.classIdCandidate[classIndex]);
		Map<Integer, String> randomMap = new HashMap<Integer, String>();
		for (String key : classMap.keySet()) {
			for (int i = 0; i < classMap.get(key); i++) {
				int classNumber = random.nextInt(k)+1;
				while(randomMap.containsKey(classNumber)){
					classNumber = random.nextInt(k)+1;
				}
				randomMap.put(classNumber, key);									
			}		
		}	
		for (int i = 0; i < this.weekdayIdCandidate.length; i++) {
			for (int j = 0; j < this.seqIdCandidate.length; j++) {								
				if (randomMap.containsKey(k)) {
					sb.append(randomMap.get(k).substring(0, this.teacherIdBit));
					sb.append(this.classIdCandidate[classIndex]);	
					sb.append(randomMap.get(k).substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit));
				}else {
					String noClassTeacherString =  this.teacherIdCandidate[random.nextInt(this.teacherIdCandidate.length)] ;
					sb.append(noClassTeacherString);
					sb.append(this.classIdCandidate[classIndex]);				
					if (noClassTeacherString.equalsIgnoreCase("0000")) {
						sb.append("00"); //0000classId00weekdayIdseqId为不上课编码
					}else {
						String subjectString= this.subjectIdCandidate[random.nextInt(this.subjectIdCandidate.length)];
						while (classMap.containsKey(noClassTeacherString+subjectString) ||subjectString.equalsIgnoreCase("00") ) {
							subjectString= this.subjectIdCandidate[random.nextInt(this.subjectIdCandidate.length)];						
						}
						sb.append(subjectString);
						
					}
				}			
				sb.append(this.weekdayIdCandidate[i]);
				sb.append(this.seqIdCandidate[j]);	
				k--;
			}
				
		}			
		this.dnaString = sb.toString();		
		System.out.println(this.dnaString);
		//GAUtil.print2(this.dnaString);
		return this.dnaString;	
	}

	public String[] getTeacherIdCandidate() {
		return teacherIdCandidate;
	}
	public void setTeacherIdCandidate(String[] teacherIdCandidate) {
		this.teacherIdCandidate = teacherIdCandidate;
	}	
	public String[] getClassIdCandidate() {
		return classIdCandidate;
	}
	public void setClassIdCandidate(String[] classIdCandidate) {
		this.classIdCandidate = classIdCandidate;
	}
	public String[] getSubjectIdCandidate() {
		return subjectIdCandidate;
	}
	public void setSubjectIdCandidate(String[] subjectIdCandidate) {
		this.subjectIdCandidate = subjectIdCandidate;
	}
	public String[] getWeekdayIdCandidate() {
		return weekdayIdCandidate;
	}
	public void setWeekdayIdCandidate(String[] weekdayIdCandidate) {
		this.weekdayIdCandidate = weekdayIdCandidate;
	}
	public String[] getSeqIdCandidate() {
		return seqIdCandidate;
	}
	public void setSeqIdCandidate(String[] seqIdCandidate) {
		this.seqIdCandidate = seqIdCandidate;
	}	
	public Map<String, Integer> getTeacherSubjectweekArrange() {
		return TeacherSubjectweekArrange;
	}
	public void setTeacherSubjectweekArrange(
			Map<String, Integer> teacherSubjectweekArrange) {
		TeacherSubjectweekArrange = teacherSubjectweekArrange;
	}
	public int getTeacherIdBit() {
		return teacherIdBit;
	}
	public int getSubjectIdBit() {
		return subjectIdBit;
	}
	public int getWeekdayIdBit() {
		return weekdayIdBit;
	}
	public int getSeqIdBit() {
		return seqIdBit;
	}
	public int getClassIdBit() {
		return classIdBit;
	}
	public int getDnaBit() {
		return dnaBit;
	}
	public Map<String, String> getClassNoCourse() {
		return classNoCourse;
	}
	public void setClassNoCourse(Map<String, String> classNoCourse) {
		this.classNoCourse = classNoCourse;
	}
	public Map<String, String> getTeacherNoCourse() {
		return teacherNoCourse;
	}
	public void setTeacherNoCourse(Map<String, String> teacherNoCourse) {
		this.teacherNoCourse = teacherNoCourse;
	}
	public Map<String, String> getSubjectNoCourse() {
		return subjectNoCourse;
	}
	public void setSubjectNoCourse(Map<String, String> subjectNoCourse) {
		this.subjectNoCourse = subjectNoCourse;
	}
	public Map<String, String> getGradeNoCourse() {
		return gradeNoCourse;
	}
	public void setGradeNoCourse(Map<String, String> gradeNoCourse) {
		this.gradeNoCourse = gradeNoCourse;
	}
	
	
	
	
	
	

	
}
