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
				for (int k = 0; k < classLength; k++) {
				
				//老师所教的科目不一样，所以此处应改为teacherId+subjectId进行判断	
				String	teacherSubjectString =  geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength, (i*seqLength+j)*dnaBit+k*classDNALength+teacherIdBit)
						+geneString.substring((i*seqLength+j)*dnaBit+k*classDNALength+teacherIdBit+classIdBit, (i*seqLength+j)*dnaBit+k*classDNALength+teacherIdBit+classIdBit+subjectIdBit);
				teacherSubjectSet.add(teacherSubjectString);
				}
				result+=classLength-teacherSubjectSet.size();			
			}
		}	
		return result;
	}
	//同一班级在同一时间段最多只能上1节课ruleTwo  由编码已经决定了该情况不可能发生
	//规则2:班级的老师科目课时统计
	/*** 
	* <p>Description: map为所有班级老师的上课课时情况</p>
	* @author  WANGCHAO262
	* @date  2017年4月10日 下午3:43:39
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
			
	
	//规则3:班级不排课设置ruleThree
	
	public static int ruleThree(Chromosome chromosome){
		int result = 0;
		
		
		return result;
	}
			//规则4:老师不排课设置ruleFour
			//规则5:科目不排课设置ruleFive
			//规则6:年级不排课设置ruleSix

	
}
