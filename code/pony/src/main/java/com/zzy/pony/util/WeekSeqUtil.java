package com.zzy.pony.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.zzy.pony.config.Constants;
import org.apache.poi.util.SystemOutLogger;


public class WeekSeqUtil {

	/**
	  * @Author : Administrator
	  * @Description : 默认按照5*8的课程表计算
	 1	9   17	25	33
	 2	10	18	26	34
	 3	11	19	27	35
	 4	12	20	28	36
	 5	13	21	29	37
	 6	14	22	30	38
	 7	15	23	31	39
	 8	16	24	32	40
	*/
	public static int WeekSeq[][] = {{1,2,3,4,5,6,7,8},
			                           {9,10,11,12,13,14,15,16},
										{17,18,19,20,21,22,23,24},
										{25,26,27,28,29,30,31,32},
										{33,34,35,36,37,38,39,40}}
										;


	public static int getWeekPeriod(int week,int periodSeq){
		return (week-1)*8+periodSeq;
	}
	/***
	* <p>Description: 获取星期</p>
	* @author  wangchao262
	*/
	public static int getWeek(int weekSeq){
		return (weekSeq-1)/8+1;
	}
	/*** 
	* <p>Description: 获取seq</p>
	* @author  wangchao262
	*/
	public static int getSeq(int weekSeq){
		int result = weekSeq%8;
		if (result == 0){
			result = 8 ;
		}
		return result;
	}
	/*** 
	* <p>Description: 获取星期</p>
	* @author  wangchao262
	*/
	public static Set<Integer> getWeek(List<Integer> list){

		Set<Integer> result = new HashSet<Integer>();
		for (int weekSeq : list) {
			result.add(getWeek(weekSeq));			
		}		
		return result ;
	}
	/*** 
	* <p>Description: 获取未排的星期</p>
	* @author  wangchao262
	*/
	public static Set<Integer> getAvailWeek(List<Integer> list){
		Set<Integer> result = new LinkedHashSet<Integer>();
		Random random = new Random();
		while(result.size()!= 5) {
			result.add(random.nextInt(5)+1);
		}		
		for (int weekSeq : list) {
			result.remove(getWeek(weekSeq));			
		}		
		return result ;
	}
	
	/*** 
	* <p>Description: 随机返回一个week,
	* 1每天安排的课程不能超过3节
	* 2已经安排完了的不能选择
	* 3优先按照之前的教师上课星期选择
	* </p>
	* @author  wangchao262
	*/
	public static Integer getRandomWeek(Set<Integer> set,List<Integer> preAlreadyTeacherList,List<Integer> alreadyTeacherList,List<Integer> alreadyTeacherAllList,Set<Integer> classAlreadySet){
		Set<Integer> alreadySet = new HashSet<Integer>();
		Set<Integer> allSet = new HashSet<Integer>();
		allSet.addAll(classAlreadySet);
		allSet.addAll(alreadyTeacherAllList);
		if(alreadyTeacherList!= null){
			alreadySet =  getWeek(alreadyTeacherList);//已经安排的星期,需要排除掉预排的
			alreadySet.removeAll(getWeek(preAlreadyTeacherList));
			for (int e:
			alreadySet) {
				int[] seqs = WeekSeq[e-1];
				int count = 0;
				for (int j : seqs) {
					if (alreadyTeacherAllList.contains(j)) {
						count++;
					}
				}
				//条件2
				if (classAlreadySet.containsAll(arrayToList( WeekSeq[e-1]))) {
					continue ;
				}else {
					//条件1
					if (count >=3) {
						continue ;
					}
				}

				return e;
			}
		}
        for (int e : set) {
            	int[] seqs = WeekSeq[e-1];
            	int count = 0;
            	for (int j : seqs) {
            		if (alreadyTeacherAllList.contains(j)) {
						count++;
					}
				}
            	//条件2
            	if (classAlreadySet.containsAll(arrayToList( WeekSeq[e-1]))) {
					continue ;
				}else {
					//条件1
					if (count >=3) {
						continue ;
					}
				}
                return e;  
        }
        return null;  
    }
	/*weekseq的获取 
	 * 1 不能在已安排的课程中 classAlreadySet
	 * 2 满足年级不排课(放在classAlreadySet)
	 * 3 满足班级不排课(放在classAlreadySet)
	 * 4 满足老师不排课
	 * 5 满足科目不排课
	 * 6 若该老师在其他班级有安排，则需要靠拢(每天安排的课程不能超过3节)
	 * 7 重要程度的设定，语数外尽量在上午
	 */
	public static int getWeekSeq(int week,List<Integer> preAlreadyTeacherList,List<Integer> alreadyTeacherList,List<Integer> alreadyTeacherAllList,Set<Integer> classAlreadySet,int type){
		int result = 0;
		int[] seqs = WeekSeq[week-1];
		
		List<Integer> list =  new ArrayList<Integer>();
		for (int i = 0; i < seqs.length; i++) {
			if (!classAlreadySet.contains(seqs[i])&& !alreadyTeacherAllList.contains(seqs[i])) {
				list.add(seqs[i]);
			}										
		}				
		//优先根据老师之前的安排就近选取,如果preAlreadyTeacherList == alreadyTeacherList 表明是第一次排，不需要获取最近
		if (alreadyTeacherList != null && preAlreadyTeacherList.size() != alreadyTeacherList.size() ) {
			result = getNext(week,list,alreadyTeacherAllList);
		}else {
			if (type == Constants.SUBJECT_SIGNIFICANT) {
				result = getSigWeekSeq(week, classAlreadySet);
			}
			if (type == Constants.SUBJECT_IMPORTANT ) {
				result = getImpWeekSeq(week, classAlreadySet);
			}
			if (type == Constants.SUBJECT_COMMON) {
				result = getComWeekSeq(week, classAlreadySet);
			}									
		}				
		return result;
		
	}
	
	public static int getNext(int week, List<Integer> list, List<Integer> alreadyTeacherAllList) {
		
		int result = 0;
		int min = 0;
		int max = 0;
		int i = 0;
		for (Integer integer : alreadyTeacherAllList) {						
			if (getWeek(integer) == week) {
				if (i == 0) {
					max = integer;
					min = integer;
				}else {
					if (integer>max) {
						max = integer;
					}
					if (integer<min) {
						min = integer;
					}
				}								
				i++;
			}						
		}
		for (Integer integer : list) {
			if (Math.abs(integer-min)<=Math.abs(integer-max)) {
				result = min; 	
			}else{
				result = max;
			}
				
		}		
		return result;
		
		
	}
	
	public static int  getSigWeekSeq(int week,Set<Integer> classAlreadySet){
		List<Integer> list = new ArrayList<Integer>();
		int[] weekSeqs = WeekSeq[week-1];
		//非常重要
		for (int i = 0; i < 5; i++) {
			if(!classAlreadySet.contains(weekSeqs[i])){
				list.add(weekSeqs[i]);
			}
		}
		//重要
		if (list.size() == 0) {
			for (int i = 5; i < 7; i++) {
				if(!classAlreadySet.contains(weekSeqs[i])){
					list.add(weekSeqs[i]);
				}
			}
		}
		 Integer[] array =  list.toArray(new Integer[list.size()]);
		 Random random = new Random();
		 return   array[random.nextInt(array.length)];
						
	}
	
	public static int  getImpWeekSeq(int week,Set<Integer> classAlreadySet ){
		List<Integer> list = new ArrayList<Integer>();
		int[] weekSeqs = WeekSeq[week-1];
		//重要
		for (int i = 4 ; i < 7; i++) {
			if(!classAlreadySet.contains(weekSeqs[i])){
				list.add(weekSeqs[i]);
			}
		}
		//非常重要
		if (list.size() == 0) {
			for (int i = 0; i < 5; i++) {
				if(!classAlreadySet.contains(weekSeqs[i])){
					list.add(weekSeqs[i]);
				}
			}
		}
		//一般
		if (list.size() == 0) {
			for (int i = 7; i < 8; i++) {
				if(!classAlreadySet.contains(weekSeqs[i])){
					list.add(weekSeqs[i]);
				}
			}
		}
		Integer[] array =  list.toArray(new Integer[list.size()]);
		Random random = new Random();
		return   array[random.nextInt(array.length)];
						
	}
	public static int  getComWeekSeq(int week,Set<Integer> classAlreadySet  ){
		List<Integer> list = new ArrayList<Integer>();
		int[] weekSeqs = WeekSeq[week-1];
		//一般
		for (int i = 6; i < weekSeqs.length; i++) {
			if(!classAlreadySet.contains(weekSeqs[i])){
				list.add(weekSeqs[i]);
			}
		}
		//重要
		if (list.size() == 0) {
			for (int i = 4; i < 7; i++) {
				if(!classAlreadySet.contains(weekSeqs[i])){
					list.add(weekSeqs[i]);
				}
			}
		}
		//非常重要
		if (list.size() == 0) {
			for (int i = 0; i < 4; i++) {
				if(!classAlreadySet.contains(weekSeqs[i])){
					list.add(weekSeqs[i]);
				}
			}
		}
		Integer[] array =  list.toArray(new Integer[list.size()]);
		Random random = new Random();
		return   array[random.nextInt(array.length)];
						
	}
	
	public static List<Integer> arrayToList(int[] array){
		List<Integer> result = new ArrayList<Integer>();
		for (int num : array) {
			result.add(num);
		}	
		return result;
		
	}
	
	
	
	
	
	
	
	



}
