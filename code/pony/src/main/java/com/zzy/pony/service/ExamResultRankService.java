package com.zzy.pony.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.vo.conditionVo;

public interface ExamResultRankService {
	List<Map<String, Object>> findByCondition(conditionVo cv);
	
	
}
