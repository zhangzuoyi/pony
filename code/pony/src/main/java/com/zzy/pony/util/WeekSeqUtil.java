package com.zzy.pony.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;






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
	* <p>Description: 随机返回一个week,每天安排的课程不能超过3节</p>
	* @author  wangchao262
	*/
	public static Integer getRandomWeek(Set<Integer> set,List<Integer> alreadyTeacherList){  						
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
            	if (count >=3) {
            		 rn = random.nextInt(set.size());  
                     i = 0;
					continue outer;
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
			list.add(seqs[i]);
			if (alreadyTeacherList.contains(seqs[i])&&i==0) {
				result = seqs[1];
			}
			else if (alreadyTeacherList.contains(seqs[i])&&i==8) {
				result = seqs[7];
			}
			else if (alreadyTeacherList.contains(seqs[i])) {
				result = seqs[i-1];
			}					
		}
		if (result == 0) {
			//说明未排过 
		}
		
		//优先根据老师之前的安排就近选取
		
		
		
		
		
		
		
		
		
		
		
		return result;
		
	}
	
	
	
	
	
	
	



}
