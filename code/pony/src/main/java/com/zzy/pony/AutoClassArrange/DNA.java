package com.zzy.pony.AutoClassArrange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.zzy.pony.util.GAUtil;







public class DNA {
	//资源池    单例模式
	private  int teacherIdBit = 4;
	private  int subjectIdBit = 2;
	private  int weekdayIdBit = 1;
	private  int seqIdBit = 1;
	private  int classIdBit = 3;
	private  int dnaBit = 11;
	private  int seqMornigLength = 4;  //早上上课数
	private  int seqAfternoonLength = 3;//下午上课数

	

	private String[] teacherIdCandidate;  
	private String[] subjectIdCandidate;
	private String[] weekdayIdCandidate;
	private String[] seqIdCandidate;
	private String[] classIdCandidate;
	private Map<String,String> TeacherSubjectweekArrange;//班级老师与课时的映射关系  key:teacherId+classId+subjectId value:weekArrange
	private Map<String, Map<String,Integer>> teacherSubjectClassMap;//平课设置  key:teacherId+subjectId value:(key classId value count(初始为0))
	private Map<String, Map<String,Integer>> teacherSubjectIrregularClassMap;//平课设置 不规则班级 key:teacherId+subjectId value:(key classId value count(初始为0))
	private Map<String, String> classNoCourse;
	private Map<String, String> teacherNoCourse;
	private Map<String, String> subjectNoCourse;
	private Map<String, String> gradeNoCourse;
	private Map<String, String> classInMorning;
	private Map<String, String> classInAfternoon;
	private Map<String, Set<Integer>> teacherSubjectRegularClassMap;
	private Map<String, List<String>> seqSubjectMap;
	private List<Integer> significantSeq;
	private List<Integer> importantSeq;
	private List<Integer> commonSeq;
	private Map<String, Integer> subjectImportanceMap;
	private Map<String, Integer> arrangeRotationMap;
	private Map<String, Integer> arrangeCombineMap;
	private Map<String, Set<Integer>> combineMap;//合班资源池


 



	
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
	* 
	* modify 支持2+1形式
	* 
	* 合班: 配置表中的课程一起上(主要不同班级)
	* 走班: 配置表中的课程一起上(主要同一班级，在某一时间点可以同时上两门课,保证两节课上的课时数是一致的)
	* 
	*/
	public String getDnaStringRuleTwo(int classIndex,Map<String, Map<String, String>> map){
		StringBuilder sb = new StringBuilder();
		Random random  = new Random();
		int k = this.weekdayIdCandidate.length * this.seqIdCandidate.length;//总时间段数 5*7
		//key:classId value( key:teacherId+subjectId value:weekArrange)	
		Map<String, String> tmpMap = map.get(this.classIdCandidate[classIndex]);	
		Map<String, String> classtmpMap = GAUtil.sortMapByVPriority(tmpMap, classInMorning, classInAfternoon);
		Map<String, String> classMap =   GAUtil.sortMapByValue(classtmpMap);		
		Map<Integer, String> randomMap = new HashMap<Integer, String>();
		
				 		
		for (String key : classMap.keySet()) {
			
			//分两种情况，第一种是weekArrange不含+,第二种是含+
			//第一种
			if (classMap.get(key).indexOf("+")<0) {
				for (int i = 0; i <  Integer.valueOf(classMap.get(key)) ; i++) {
					int classNumber=0 ;
					
					if (combineMap != null && !combineMap.isEmpty()&& combineMap.containsKey(key)&& combineMap.get(key).size()==Integer.valueOf(classMap.get(key))) {
						//@todo  随机从set中取值赋给当前classNumber且不能够重复
						Iterator<Integer> iterator =   combineMap.get(key).iterator();
						while (iterator.hasNext()) {
							Integer integer = (Integer) iterator.next();
							if (!randomMap.containsKey(integer)) {
								randomMap.put(integer, key);
								break;
							}							
						}
												
					}else {	
						classNumber = random.nextInt(k)+1;
						//@todo  增加规则在combineMap已经排过的不能够在下面排
					while(randomMap.containsKey(classNumber) ||GAUtil.isExistClass(randomMap, classNumber,key,this.seqIdCandidate.length)
							||( !key.startsWith("C")&&!key.startsWith("R")&&  this.classInMorning.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isMorning(classNumber,this.seqIdCandidate.length,this.seqMornigLength))
							||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInAfternoon.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isAfternoon(classNumber,this.seqIdCandidate.length,this.seqAfternoonLength))
							||(this.teacherSubjectRegularClassMap.containsKey(key)&& !GAUtil.isInWeekSet(classNumber, teacherSubjectRegularClassMap.get(key),this.seqIdCandidate.length)
							||!GAUtil.isSeqSubjectMatch(this.seqSubjectMap, classNumber, this.seqIdCandidate.length, key,randomMap.keySet(),this.significantSeq,this.importantSeq,this.commonSeq,this.subjectImportanceMap)
							|| GAUtil.isInCombineMap(combineMap, classNumber)	)
							){
						classNumber = random.nextInt(k)+1;
						}
					
					randomMap.put(classNumber, key);

					}
					
					
					if (key.startsWith("C") && classNumber != 0) {
						if (combineMap.containsKey(key)) {
							combineMap.get(key).add(classNumber);
						}else {
							Set<Integer> set = new HashSet<Integer>();
							set.add(classNumber);
							combineMap.put(key, set);
						}
					}
					
				}
			}else {
				//第二种  2+1  暂不支持合班设置
				   String[] a = classMap.get(key).split("\\+");
				   for (int i = 0; i <  Integer.valueOf(a[0]) ; i++) {
						int classNumber = random.nextInt(k)+1;
						while(randomMap.containsKey(classNumber) ||GAUtil.isExistClass(randomMap, classNumber,key,this.seqIdCandidate.length)
								||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInMorning.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isMorning(classNumber,this.seqIdCandidate.length,this.seqMornigLength))
								||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInAfternoon.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isAfternoon(classNumber,this.seqIdCandidate.length,this.seqAfternoonLength))
								||(this.teacherSubjectRegularClassMap.containsKey(key)&& !GAUtil.isInWeekSet(classNumber, teacherSubjectRegularClassMap.get(key),this.seqIdCandidate.length))
								||!GAUtil.isSeqSubjectMatch(this.seqSubjectMap, classNumber, this.seqIdCandidate.length, key,randomMap.keySet(),this.significantSeq,this.importantSeq,this.commonSeq,this.subjectImportanceMap)
								|| GAUtil.isInCombineMap(combineMap, classNumber)){
							classNumber = random.nextInt(k)+1;
						}
						randomMap.put(classNumber, key);	
						
						/*if (key.startsWith("C")) {
							if (combineMap.containsKey(key)) {
								combineMap.get(key).add(classNumber);
							}else {
								Set<Integer> set = new HashSet<Integer>();
								set.add(classNumber);
								combineMap.put(key, set);
							}
						}*/
						
					}
				   for (int i = 0; i <  Integer.valueOf(a[1]) ; i++) {
						int classNumber = random.nextInt(k)+1;		
						//头一天的最后一节不能与第二天的第一节形成联排课  classNumber%this.seqIdCandidate.length == 1
						//早上的第一节课不能与下午的第一节课形成里联排课 classNumber%this.seqIdCandidate.length == 4
						//classNumber+1>k 不能超出范围
						/*35 28 21 14 7
						  34 27 20 13 6
						  33 26 19 12 5
						  32 25 18 11 4

						  31 24 17 10 3
						  30 23 16  9 2
						  29 22 15  8 1*/
				       //当天已经上过该课就不能再上
						while(randomMap.containsKey(classNumber)||randomMap.containsKey(classNumber-1)||classNumber%this.seqIdCandidate.length == 1|| classNumber%this.seqIdCandidate.length == 4 ||GAUtil.isExistClass(randomMap, classNumber,key,this.seqIdCandidate.length)
								||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInMorning.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isMorning(classNumber,this.seqIdCandidate.length,this.seqMornigLength))
								||(!key.startsWith("C")&&!key.startsWith("R")&&this.classInAfternoon.containsKey(key.substring(this.teacherIdBit, this.teacherIdBit+this.subjectIdBit)) && !GAUtil.isAfternoon(classNumber,this.seqIdCandidate.length,this.seqAfternoonLength))
								||(this.teacherSubjectRegularClassMap.containsKey(key)&& !GAUtil.isInWeekSet(classNumber, teacherSubjectRegularClassMap.get(key),this.seqIdCandidate.length))
								||!GAUtil.isSeqSubjectMatch(this.seqSubjectMap, classNumber, this.seqIdCandidate.length, key,randomMap.keySet(),this.significantSeq,this.importantSeq,this.commonSeq,this.subjectImportanceMap)
								|| GAUtil.isInCombineMap(combineMap, classNumber)|| GAUtil.isInCombineMap(combineMap, classNumber-1)){
							classNumber = random.nextInt(k)+1;
						}
						randomMap.put(classNumber, key);
						randomMap.put(classNumber-1, key);
						
						/*if (key.startsWith("C")) {
							if (combineMap.containsKey(key)) {
								combineMap.get(key).add(classNumber);
								combineMap.get(key).add(classNumber-1);

							}else {
								Set<Integer> set = new HashSet<Integer>();
								set.add(classNumber);
								set.add(classNumber-1);
								combineMap.put(key, set);
							}
						}*/
					}			
				
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
		//System.out.println(this.dnaString);
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
	public Map<String, String> getTeacherSubjectweekArrange() {
		return TeacherSubjectweekArrange;
	}
	public void setTeacherSubjectweekArrange(
			Map<String, String> teacherSubjectweekArrange) {
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
	public Map<String, Map<String, Integer>> getTeacherSubjectClassMap() {
		return teacherSubjectClassMap;
	}
	public void setTeacherSubjectClassMap(
			Map<String, Map<String, Integer>> teacherSubjectClassMap) {
		this.teacherSubjectClassMap = teacherSubjectClassMap;
	}
	public Map<String, Map<String, Integer>> getTeacherSubjectIrregularClassMap() {
		return teacherSubjectIrregularClassMap;
	}
	public void setTeacherSubjectIrregularClassMap(
			Map<String, Map<String, Integer>> teacherSubjectIrregularClassMap) {
		this.teacherSubjectIrregularClassMap = teacherSubjectIrregularClassMap;
	}
	public Map<String, String> getClassInMorning() {
		return classInMorning;
	}
	public void setClassInMorning(Map<String, String> classInMorning) {
		this.classInMorning = classInMorning;
	}
	public Map<String, String> getClassInAfternoon() {
		return classInAfternoon;
	}
	public void setClassInAfternoon(Map<String, String> classInAfternoon) {
		this.classInAfternoon = classInAfternoon;
	}
	public Map<String, Set<Integer>> getTeacherSubjectRegularClassMap() {
		return teacherSubjectRegularClassMap;
	}
	public void setTeacherSubjectRegularClassMap(
			Map<String, Set<Integer>> teacherSubjectRegularClassMap) {
		this.teacherSubjectRegularClassMap = teacherSubjectRegularClassMap;
	}
	public Map<String, List<String>> getSeqSubjectMap() {
		return seqSubjectMap;
	}
	public void setSeqSubjectMap(Map<String, List<String>> seqSubjectMap) {
		this.seqSubjectMap = seqSubjectMap;
	}
	public List<Integer> getSignificantSeq() {
		return significantSeq;
	}
	public void setSignificantSeq(List<Integer> significantSeq) {
		this.significantSeq = significantSeq;
	}
	public List<Integer> getImportantSeq() {
		return importantSeq;
	}
	public void setImportantSeq(List<Integer> importantSeq) {
		this.importantSeq = importantSeq;
	}
	public List<Integer> getCommonSeq() {
		return commonSeq;
	}
	public void setCommonSeq(List<Integer> commonSeq) {
		this.commonSeq = commonSeq;
	}
	public Map<String, Integer> getSubjectImportanceMap() {
		return subjectImportanceMap;
	}
	public void setSubjectImportanceMap(Map<String, Integer> subjectImportanceMap) {
		this.subjectImportanceMap = subjectImportanceMap;
	}
	public Map<String, Integer> getArrangeRotationMap() {
		return arrangeRotationMap;
	}
	public void setArrangeRotationMap(Map<String, Integer> arrangeRotationMap) {
		this.arrangeRotationMap = arrangeRotationMap;
	}
	public Map<String, Integer> getArrangeCombineMap() {
		return arrangeCombineMap;
	}
	public void setArrangeCombineMap(Map<String, Integer> arrangeCombineMap) {
		this.arrangeCombineMap = arrangeCombineMap;
	}
	public Map<String, Set<Integer>> getCombineMap() {
		return combineMap;
	}
	public void setCombineMap(Map<String, Set<Integer>> combineMap) {
		this.combineMap = combineMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
