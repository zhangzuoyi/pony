package com.zzy.pony.util;

import java.util.ArrayList;
import java.util.List;

public class GAUtil {

	/*** 
	* <p>Description:获取候选数组,bit为位数,list为id集合，如teacherId </p>
	* @author  WANGCHAO262
	* @date  2017年4月7日 上午10:22:30
	*/
	public static String[] getCandidateStrings(List<Integer> list ,int bit){
		String[] result  = new String[list.size()];
		for (int i = 0; i < result.length; i++) {
			String idStr = String.format("%0"+bit+"d", list.get(i));
			System.out.println(idStr);
			result[i] =idStr;		
		}		
		return result;		
	}
	
	public static void main(String[] args){
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			list.add(new Integer(i));
		}		
		getCandidateStrings(list, 4);
	}
}
