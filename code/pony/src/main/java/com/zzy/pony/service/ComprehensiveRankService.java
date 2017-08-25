package com.zzy.pony.service;


import com.zzy.pony.vo.ConditionVo;

import java.util.List;
import java.util.Map;


public interface ComprehensiveRankService {
	
	/*** 
	* <p>Description: 单科排名</p>
	* @author  wangchao262
	* @date  2017年8月24日 上午9:59:08
	*/
	void rankExamReult(ConditionVo cv);
	/*** 
	* <p>Description: 整体排名</p>
	* @author  wangchao262
	* @date  2017年8月24日 上午9:59:11
	*/
	void rankExaminee(ConditionVo cv);

	List<Map<String, Object>> findRankByExam(int examId);
}
