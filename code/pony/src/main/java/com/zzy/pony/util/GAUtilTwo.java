package com.zzy.pony.util;



public class GAUtilTwo {

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
	public static int getSeq(int week,int periodSeq){
		return (week-1)*8+periodSeq;
	}



}
