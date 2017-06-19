/**  
 *@Description: 基因遗传染色体   
 */ 
package com.zzy.pony.AutoClassArrange;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.zzy.pony.util.GAUtil;

public class Chromosome {
	private char[] gene;//基因序列
	private double score;//对应的函数得分

	
	private int scoreOne; 
	private int scoreThree; 
	private int scoreFour; 
	private int scoreFive; 
	private int scoreSix; 
	private int scoreSeven; 

	
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public char[] getGene(){	
		return gene;
	}
	public void setGene(char[] gene){	
		this.gene= gene;
	}
	
	
	
	
	

	public int getScoreOne() {
		return scoreOne;
	}

	public void setScoreOne(int scoreOne) {
		this.scoreOne = scoreOne;
	}

	public int getScoreThree() {
		return scoreThree;
	}

	public void setScoreThree(int scoreThree) {
		this.scoreThree = scoreThree;
	}

	public int getScoreFour() {
		return scoreFour;
	}

	public void setScoreFour(int scoreFour) {
		this.scoreFour = scoreFour;
	}

	public int getScoreFive() {
		return scoreFive;
	}

	public void setScoreFive(int scoreFive) {
		this.scoreFive = scoreFive;
	}

	public int getScoreSix() {
		return scoreSix;
	}

	public void setScoreSix(int scoreSix) {
		this.scoreSix = scoreSix;
	}

	public int getScoreSeven() {
		return scoreSeven;
	}

	public void setScoreSeven(int scoreSeven) {
		this.scoreSeven = scoreSeven;
	}

	/**
	 * @param size
	 * 随机生成所有班级的基因序列
	 */
	public Chromosome(String[] classIdCandidate) {
		if (classIdCandidate.length <= 0) {
			return;
		}
	   StringBuilder sb = new StringBuilder();
	   for (int i = 0; i < classIdCandidate.length; i++) {
		  sb.append(DNA.getInstance().getDnaString(i));
	  }	   
	   gene = sb.toString().toCharArray();
			
	}
	/**
	 * @param size
	 * 随机生成所有班级的基因序列,且每个班满足老师课时数条件 key:teacherId+classId+subjectId value:weekArrange
	 * 
	 *  mofify  支持2+1形式
	 */
	public Chromosome(String[] classIdCandidate,Map<String, String> map) {
		/*if (classIdCandidate.length <= 0) {
			return;
		}
	   StringBuilder sb = new StringBuilder();
	   for (int i = 0; i < classIdCandidate.length; i++) {
		  sb.append(DNA.getInstance().getDnaString(i));
	  }	   
	   gene = sb.toString().toCharArray();*/
		
		if (classIdCandidate.length <= 0) {
			return;
		}
	   StringBuilder sb = new StringBuilder();
	   Map<String, Map<String, String>> classMap = GAUtil.getClassTeacherSubjectweekArrange(map,DNA.getInstance().getArrangeRotationMap(),DNA.getInstance().getArrangeCombineMap());
	   for (int i = 0; i < classIdCandidate.length; i++) {
		  sb.append(DNA.getInstance().getDnaStringRuleTwo(i,classMap));
	  }	   
	   gene = sb.toString().toCharArray();	
			
	}
	
	/**
	 * 生成一个新基因
	 */
	public Chromosome() {
		
	}
	
	/**
	 * @param c
	 * @return
	 * @Description: 克隆基因
	 */
	public static Chromosome clone(final Chromosome c) {
		if (c == null || c.gene == null) {
			return null;
		}
		Chromosome copy = new Chromosome();
		copy.initGeneSize(c.gene.length);
		for (int i = 0; i < c.gene.length; i++) {
			copy.gene[i] = c.gene[i];
		}
		return copy;
	}
	
	/**
	 * @param size
	 * @Description: 初始化基因长度
	 */
	private void initGeneSize(int size) {
		if (size <= 0) {
			return;
		}
		gene = new char[size];
	}
	
	
	/**
	 * @param c1
	 * @param c2
	 * @Description: 遗传产生下一代
	 */
	public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
		if (p1 == null || p2 == null) { //染色体有一个为空，不产生下一代
			return null;
		}
		if (p1.gene == null || p2.gene == null) { //染色体有一个没有基因序列，不产生下一代
			return null;
		}
		if (p1.gene.length != p2.gene.length) { //染色体基因序列长度不同，不产生下一代
			return null;
		}
		Chromosome c1 = clone(p1);
		Chromosome c2 = clone(p2);
		//随机产生交叉互换位置  
		int size = c1.gene.length;
		int classCount = DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit() *DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		Random random = new Random();
		
		
		
		int a =  (random.nextInt(classCount)+1)*classDNALength  ;
		while (a>=size) {
			a=(random.nextInt(classCount)+1)*classDNALength;
		}
		int b =  (random.nextInt(classCount)+1)*classDNALength + (random.nextInt(classCount)+1)*classDNALength;
		if(b>size){
			b=size;
		}
		/*GAUtil.print(String.valueOf(c1.gene));
		GAUtil.print(String.valueOf(c2.gene));*/

		//对位置上的基因进行交叉互换 注意数组长度需要减1
		for (int i = a-1; i < b; i++) {
			char t = c1.gene[i];
			c1.gene[i] = c2.gene[i];
			c2.gene[i] = t;
		}
		List<Chromosome> list = new ArrayList<Chromosome>();
		list.add(c1);
		list.add(c2);
		/*GAUtil.print(String.valueOf(c1.gene));
		GAUtil.print(String.valueOf(c2.gene));*/
		
		return list;
	}
	
	/**
	 * @param num
	 * @Description: 基因发生变异 第classIndex个班级表突变 classIndex从0开始
	 */
	public void mutation(int classIndex,Map<String, Map<String, String>> map) {
		//允许变异
		//int classCount = DNA.getInstance().getClassIdCandidate().length;
		int classDNALength = DNA.getInstance().getDnaBit()*DNA.getInstance().getWeekdayIdCandidate().length*DNA.getInstance().getSeqIdCandidate().length;
		char[] a =   DNA.getInstance().getDnaStringRuleTwo(classIndex, map).toCharArray();
	    for (int i = classIndex*classDNALength; i < classIndex*classDNALength+classDNALength; i++) {
			gene[i] = a[i-(classIndex*classDNALength)];			
		}
        
        
	}
	
	
}