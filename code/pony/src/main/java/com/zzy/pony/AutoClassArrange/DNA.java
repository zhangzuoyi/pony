package com.zzy.pony.AutoClassArrange;

import java.util.Random;





public class DNA {
	//资源池    单例模式
	private static final int teacherIdBit = 4;
	private static final int subjectIdBit = 2;
	private static final int weekdayIdBit = 1;
	private static final int seqIdBit = 1;
	private static final int classIdBit = 3;
	private static final int dbaBit = 11;

	private String[] teacherIdCandidate;  
	private String[] subjectIdCandidate;
	private String[] weekdayIdCandidate;
	private String[] seqIdCandidate;
	private String[] classIdCandidate;
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
				sb.append(this.teacherIdCandidate[random.nextInt(this.teacherIdCandidate.length)]);
				sb.append(this.classIdCandidate[classIndex]);
				sb.append(this.subjectIdCandidate[random.nextInt(this.subjectIdCandidate.length)]);
				sb.append(this.weekdayIdCandidate[i]);
				sb.append(this.seqIdCandidate[j]);	
			}
				
		}			
		this.dnaString = sb.toString();		
		System.out.println(this.dnaString);
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
	public int getDnaBit(){
		return this.dbaBit;
	}
	
	

	
}
