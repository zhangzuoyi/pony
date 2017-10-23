package com.zzy.pony.exam.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.AverageIndexRowVo;

public interface AverageService {
	int SECTION_COUNT=22;//分段数
	List<AverageIndexRowVo> findIndexRowVo(Integer examId, Integer gradeId);
	void saveIndexList(Integer examId, Integer gradeId, List<AverageIndex> indexList);
	void uploadIndexList(Integer examId, Integer gradeId, List<String> subjectList, List<List<Float>> valueList);
	/**
	 * @param examId
	 * @param gradeId
	 * 计算档数，累数
	 */
	Map<Integer,Map<String, Map<String, BigDecimal>>> calculateAverage(int examId,int gradeId);
	Map<Integer,Map<String, Map<String, BigDecimal>>> calculateAverageByFile(MultipartFile file);

}
