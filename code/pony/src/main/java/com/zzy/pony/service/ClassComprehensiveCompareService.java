package com.zzy.pony.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.vo.ConditionVo;

public interface ClassComprehensiveCompareService {
	List<Map<String, Object>> findByCondition(ConditionVo cv);
	
	
}
