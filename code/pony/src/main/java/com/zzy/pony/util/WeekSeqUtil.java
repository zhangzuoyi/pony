package com.zzy.pony.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.zzy.pony.config.Constants;







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
	public static int WeekSeq[][] = {{1,9,17,25,33},
									 {2,10,18,26,34},
									 {3,11,19,27,35},
									 {4,12,20,28,36},
									 {5,13,21,29,37},
									 {6,14,22,30,38},
									 {7,15,23,31,39},
									 {8,16,24,32,40}};
		
	
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
		Set<Integer> result = new HashSet<Integer>();
		result.add(1);
		result.add(2);
		result.add(3);
		result.add(4);
		result.add(5);
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
	public static Integer getRandomWeek(Set<Integer> set,List<Integer> alreadyTeacherList,Set<Integer> classAlreadySet){  						
		Random random = new Random();			
		int rn = random.nextInt(set.size());  
        int i = 0;  
        outer:
        for (int e : set) {  
            if(i==rn){
            	
            	int[] seqs = WeekSeq[i];
            	int count = 0;
            	for (int j : seqs) {
            		if (alreadyTeacherList.contains(j)) {
						count++;
					}  
				}
            	//条件2
            	if (classAlreadySet.containsAll(Arrays.asList( WeekSeq[e]))) {
            		rn = random.nextInt(set.size());  
                    i = 0;
					continue outer;
				}else {
					//条件1
					if (count >=3) {
	            		 rn = random.nextInt(set.size());  
	                     i = 0;
						continue outer;
					} 
					//条件3(非硬性)
					int maxCount = 0;
					if (alreadyTeacherList!=null && !getWeek(alreadyTeacherList).contains(e) && maxCount < 20) {
						rn = random.nextInt(set.size());  
	                     i = 0;
						continue outer;
					}
				}
            	
            	         	
                return e;  
            }  
            i++;  
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
	public static int getWeekSeq(int week,List<Integer> alreadyTeacherList,Set<Integer> classAlreadySet,int type){
		int result = 0;
		int[] seqs = WeekSeq[week];
		
		List<Integer> list =  new ArrayList<Integer>();
		for (int i = 0; i < seqs.length; i++) {
			if (!classAlreadySet.contains(seqs[i])&& !alreadyTeacherList.contains(seqs[i])) {
				list.add(seqs[i]);
			}										
		}				
		//优先根据老师之前的安排就近选取
		if (alreadyTeacherList != null) {
			result = getNext(week,list,alreadyTeacherList);
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
	
	public static int getNext(int week, List<Integer> list,List<Integer> alreadyTeacherList) {
		
		int result = 0;
		int min = 0;
		int max = 0;
		int i = 0;
		for (Integer integer : alreadyTeacherList) {						
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
	
	public static int  getSigWeekSeq(int week,Set<Integer> classAlreadySet ){
		List<Integer> list = new ArrayList<Integer>();
		int[] weekSeqs = WeekSeq[week];
		for (int i = 0; i < weekSeqs.length; i++) {
			if(i<=5 &&  !classAlreadySet.contains(weekSeqs[i])){
				list.add(weekSeqs[i]);
			}
		}					
		 Integer[] array =  list.toArray(new Integer[list.size()]);
		 Random random = new Random();
		 return   array[random.nextInt(array.length)];
						
	}
	
	public static int  getImpWeekSeq(int week,Set<Integer> classAlreadySet ){
		List<Integer> list = new ArrayList<Integer>();
		int[] weekSeqs = WeekSeq[week];
		for (int i = 0; i < weekSeqs.length; i++) {
			if(i>=5 &&  !classAlreadySet.contains(weekSeqs[i])){
				list.add(weekSeqs[i]);
			}
		}					
		Integer[] array =  list.toArray(new Integer[list.size()]);
		Random random = new Random();
		return   array[random.nextInt(array.length)];
						
	}
	public static int  getComWeekSeq(int week,Set<Integer> classAlreadySet ){
		List<Integer> list = new ArrayList<Integer>();
		int[] weekSeqs = WeekSeq[week];
		for (int i = 0; i < weekSeqs.length; i++) {
			if(i>=7 &&  !classAlreadySet.contains(weekSeqs[i])){
				list.add(weekSeqs[i]);
			}
		}					
		Integer[] array =  list.toArray(new Integer[list.size()]);
		Random random = new Random();
		return   array[random.nextInt(array.length)];
						
	}
	
	
	
	
	
	
	
	



}
