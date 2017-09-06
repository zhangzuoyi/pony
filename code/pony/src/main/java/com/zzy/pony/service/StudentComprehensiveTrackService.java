package com.zzy.pony.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.vo.ConditionVo;

public interface StudentComprehensiveTrackService {
	List<Map<String, Object>> findByCondition(ConditionVo cv);
	//获取某一次考试的学生排名情况
	Map<Integer, String> findExamRank(int examId);
	
	
}
