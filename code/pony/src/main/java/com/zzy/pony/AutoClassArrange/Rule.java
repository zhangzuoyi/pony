package com.zzy.pony.AutoClassArrange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Rule {
// classLength * [teacherId(4)+classId(3)+subjectId(2)+weekdayId(1)+seqId(1)]*weekdayLength*seqLength
	//个体适应度函数定义为 如下规则对于满足得1分，不满足则得0分
			
	//规则1:同一老师在同一时间段最多只能上1节课ruleOne
	public static int ruleOne(Chromosome chromosome){
		int result = 0;
		char[] gene = chromosome.getGene();
		String geneString = String.valueOf(gene);
		int dnaBit = DNA.getInstance().getDnaBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		//同一时间段比较
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				Set<String> teacherSubjectSet =  new HashSet<String>();
				int noClassCount = 0;//不排课编码计数
				int combineCount = 0;//合班计数
				for (int k = 0; k < classLength; k++) {
				
				//老师所教的科目不一样，所以此处应改为teacherId+subjectId进行判断	
				String	teacherSubjectString =  geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+teacherIdBit)
						+geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength+teacherIdBit+classIdBit, (i*seqLength+j)*dnaBit+k*classDNALength+teacherIdBit+classIdBit+subjectIdBit);
				if (teacherSubjectString.equalsIgnoreCase("000000")) {
					noClassCount++;
				}else if (teacherSubjectString.startsWith("C")) {
					combineCount++;
				}else {
					teacherSubjectSet.add(teacherSubjectString);				
				}
				
				}
				result+=classLength-teacherSubjectSet.size()-noClassCount-combineCount;			
			}
		}	
		//GAUtil.print2(String.valueOf(chromosome.getGene()));
		return result;
	}
	//同一班级在同一时间段最多只能上1节课ruleTwo  由编码已经决定了该情况不可能发生
	//规则2:班级的老师科目课时统计
	/*** 
	* <p>Description: map为所有班级老师的上课课时情况</p>
	* @author  WANGCHAO262
	* @date  2017年4月10日 下午3:43:39   map : key teacherId+classId+subjectId value 课时数
	*/
	public static int ruleTwo(Chromosome chromosome,Map<String, Integer> map){
		int result = 0;
		Map<String, Integer> mapCopy = new HashMap<String, Integer>();
		mapCopy.putAll(map);
		char[] gene = chromosome.getGene();
		String geneString = String.valueOf(gene);
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		//同一班级比较
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {
				
				//一个上课单元
				String dnaString = geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				String key = dnaString.substring(0,teacherIdBit+classIdBit+subjectIdBit);
				if (mapCopy.containsKey(key)) {				
					mapCopy.put(key, mapCopy.get(key).intValue()-1);				
				}									
				}
			}
		}	
		
		for (String key : mapCopy.keySet()) {
			result+= Math.abs(mapCopy.get(key).intValue());			
		}
		return result;
		
	}
			
	
	//规则3:班级不排课设置ruleThree 由于编码规则每个班在每个课时都会有课 ,顾该规则需要特别考虑(年级不排课设置也是一样的) 
	// map : key classId value weekdayId+seqId
	
	public static int ruleThree(Chromosome chromosome,Map<String, String> map){
		int result = map.size();
		Map<String, String> mapCopy = new HashMap<String, String>();
		mapCopy.putAll(map);
		char[] gene = chromosome.getGene();
		String geneString = String.valueOf(gene);
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		//同一班级比较
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {
				
				//一个上课单元
				String dnaString = geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				String teacherSubjectString =  dnaString.substring(0, teacherIdBit)+dnaString.substring(teacherIdBit+classIdBit, teacherIdBit+classIdBit+subjectIdBit);
				String key = dnaString.substring(teacherIdBit,teacherIdBit+classIdBit);
				String value = dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, dnaBit);
				if (mapCopy.containsKey(key)&&mapCopy.get(key).equals(value)&&teacherSubjectString.equalsIgnoreCase("000000")) {				
					result--;					
				}									
				}
			}
		}	
		
		
		return result;
	}
			//规则4:老师不排课设置ruleFour
	// map : key teacherId value weekdayId+seqId
	public static int ruleFour(Chromosome chromosome,Map<String, String> map){
		int result = map.size();
		Map<String, String> mapCopy = new HashMap<String, String>();
		mapCopy.putAll(map);
		char[] gene = chromosome.getGene();
		String geneString = String.valueOf(gene);
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		//同一班级比较
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {
				
				//一个上课单元
				String dnaString = geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				String key = dnaString.substring(0,teacherIdBit);
				String value = dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, dnaBit);
				if (mapCopy.containsKey(key)&&mapCopy.get(key).equals(value)) {				
					result --;
					
				}									
				}
			}
		}	
		
		
		return Math.abs(result);
	}
	
	
			//规则5:科目不排课设置ruleFive
	// map : key classId+subjectId value weekdayId+seqId
	public static int ruleFive(Chromosome chromosome,Map<String, String> map){
		int result = map.size();
		Map<String, String> mapCopy = new HashMap<String, String>();
		mapCopy.putAll(map);
		char[] gene = chromosome.getGene();
		String geneString = String.valueOf(gene);
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		//同一班级比较
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {
				
				//一个上课单元
				String dnaString = geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				String key = dnaString.substring(teacherIdBit,teacherIdBit+classIdBit+subjectIdBit);
				String value = dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, dnaBit);
				if (mapCopy.containsKey(key)&&mapCopy.get(key).equals(value)) {				
					result --;
					
				}									
				}
			}
		}	
		
		
		return Math.abs(result);
	}
	
			//规则6:年级不排课设置ruleSix
	// map : key classId value weekdayId+seqId
	public static int ruleSix(Chromosome chromosome,Map<String, String> map){
		int result = map.size();
		Map<String, String> mapCopy = new HashMap<String, String>();
		mapCopy.putAll(map);
		char[] gene = chromosome.getGene();
		String geneString = String.valueOf(gene);
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		//同一班级比较
		for (int i = 0; i < weekdayLength; i++) {
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {
				
				//一个上课单元
				String dnaString = geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				String teacherSubjectString =  dnaString.substring(0, teacherIdBit)+dnaString.substring(teacherIdBit+classIdBit, teacherIdBit+classIdBit+subjectIdBit);
				String key = dnaString.substring(teacherIdBit,teacherIdBit+classIdBit);
				String value = dnaString.substring(teacherIdBit+classIdBit+subjectIdBit, dnaBit);
				if (mapCopy.containsKey(key)&&mapCopy.get(key).equals(value)&&teacherSubjectString.equalsIgnoreCase("000000")) {				
					result--;				
				}									
				}
			}
		}	
		
		
		return result;
	}
	//规则7:平课设置ruleSeven 对于一个老师如果在多个班上课，那么在每个班的上课进度应该一致
	//map (key teacherId+subjectId  value (key classId  value count(用来计数，初始化为0)) )
	//如果在安排上存在两个班课程不一样，即一个为3+1 (5节课)一个为4(4节课) 新增irregularMap 数据结构同map，只存放课程不平的classId
	
	public static int ruleSeven(Chromosome chromosome,Map<String, Map<String, Integer>> map,Map<String, Map<String, Integer>> irregularMap){
		int result=0;
		//重置,解决浅复制问题
		/*for (String teacherSubject : map.keySet()) {
			Map<String , Integer> classMap = map.get(teacherSubject);
			for (String classId : classMap.keySet()) {
				classMap.put(classId, 0);
			}
		}
		 
		char[] gene = chromosome.getGene();
		String geneString = String.valueOf(gene);
		int dnaBit = DNA.getInstance().getDnaBit();
		int classIdBit = DNA.getInstance().getClassIdBit();
		int teacherIdBit = DNA.getInstance().getTeacherIdBit();
		int subjectIdBit = DNA.getInstance().getSubjectIdBit();
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		int weekdayLength = DNA.getInstance().getWeekdayIdCandidate().length;
		int seqLength = DNA.getInstance().getSeqIdCandidate().length;
		//同一天的同一老师同一科目的上课数量应该一致
		for (int i = 0; i < weekdayLength; i++) {
			//每天结束计算一次分数并重置
			if (i!=0) {
				for (String teacherSubject : map.keySet()) {
					Map<String , Integer> classMap = map.get(teacherSubject);
					Set<Integer> set = new HashSet<Integer>(); 
					if (irregularMap.containsKey(teacherSubject)) {
						continue;//一旦该教师存在与不平课中，就不计算该规则分值
					}else {
						for (String classId : classMap.keySet()) {							
							set.add(classMap.get(classId));
						}
						result += set.size()-1;
					}
			
				}
				//重置
				for (String teacherSubject : map.keySet()) {
					Map<String , Integer> classMap = map.get(teacherSubject);
					for (String classId : classMap.keySet()) {
						classMap.put(classId, 0);
					}
				}
				
			}
			
			for (int j = 0; j < seqLength; j++) {
				for (int k = 0; k < classLength; k++) {
				
				//一个上课单元
				String dnaString = geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+dnaBit);
				String teacherSubjectString =  dnaString.substring(0, teacherIdBit)+dnaString.substring(teacherIdBit+classIdBit, teacherIdBit+classIdBit+subjectIdBit);			
				String classIdString = dnaString.substring(teacherIdBit,teacherIdBit+classIdBit);
				if (map.containsKey(teacherSubjectString)&&map.get(teacherSubjectString).containsKey(classIdString)) {
					int count =   map.get(teacherSubjectString).get(classIdString).intValue();
					map.get(teacherSubjectString).put(classIdString, count+1);
				}												
				}
			}
		}*/	
		
		
		return result;
	}

	
}
