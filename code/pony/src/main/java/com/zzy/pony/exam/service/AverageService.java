package com.zzy.pony.exam.service;

import java.util.List;

import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.AverageIndexRowVo;

public interface AverageService {
	int SECTION_COUNT=22;//分段数
	List<AverageIndexRowVo> findIndexRowVo(Integer examId, Integer gradeId);
	void saveIndexList(Integer examId, Integer gradeId, List<AverageIndex> indexList);
	void uploadIndexList(Integer examId, Integer gradeId, List<String> subjectList, List<List<Float>> valueList);
}
