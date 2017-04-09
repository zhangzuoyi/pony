package com.zzy.pony.AutoClassArrange;

public class Rule {
// classLength * [teacherId(4)+classId(3)+subjectId(2)+weekdayId(1)+seqId(1)]*weekdayLength*seqLength
	//个体适应度函数定义为 如下规则对于每个班满足得1分，不满足则得0分，然后在算一个个体中所有班级的算术平均值
			
	//规则1:同一老师在同一时间段最多只能上1节课ruleOne
	public static int ruleOne(Chromosome chromosome){
		int result = 0;
		int classLength =	 DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		char[] gene =chromosome.getGene();

		for(int i = 0;i<classLength;i+=classDNALength){
			
		} 
		
		
		return result;
	}
	
	
	
			//规则2:同一班级在同一时间段最多只能上1节课ruleTwo
			//规则3:班级不排课设置ruleThree
			//规则4:老师不排课设置ruleFour
			//规则5:科目不排课设置ruleFive
			//规则6:年级不排课设置ruleSix
	
	
}
