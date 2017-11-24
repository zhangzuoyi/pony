package com.zzy.pony.exam.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.groovy.classgen.asm.indy.IndyBinHelper;
import org.springframework.web.multipart.MultipartFile;

import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.AverageAssignExcelVo;
import com.zzy.pony.exam.vo.AverageExcelVo;
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
	Map<String,Map<String, Map<String, BigDecimal>>> calculateAverageByFile(MultipartFile file);
	List<String> getClassCode(List<AverageExcelVo> averageExcelVos,String schoolName);
	void sortAverageExcelVo(List<AverageExcelVo> averageExcelVos);
	void sortAverageExcelVoSum(List<AverageExcelVo> averageExcelVos);
	List<AverageExcelVo> getAverageExcelVo (Workbook wb,int index,int schoolIndex);
	List<AverageAssignExcelVo> getAverageAssignExcelVo(Workbook wb,int schoolIndex,String schoolName);
	List<AverageExcelVo> getAverageExcelVoSum (Workbook wb,List<String> subjectNames);
	/**
	 * @param averageExcelVos
	 * @return 整体每个等级人数
	 */
	Map<Integer,List<AverageExcelVo>> getLevelMap(List<AverageExcelVo> averageExcelVos);
	Map<Integer,List<AverageExcelVo>> getLevelMapSum(List<AverageExcelVo> averageExcelVos);
	
	Map<Integer,List<AverageExcelVo>> getLevelAssignMap(List<AverageExcelVo> averageExcelVos);

	/**
	 * @param averageExcelVos
	 * @return 整体每个等级指标
	 */
	Map<Integer,BigDecimal> getLevelMapDecimal(List<AverageExcelVo> averageExcelVos);
	/**
	 * @param levelMap
	 * @param schoolName
	 * @return 某个学校的等级人数
	 */
	Map<Integer,List<AverageExcelVo>> getLevelMapBySchoolName(Map<Integer,List<AverageExcelVo>> levelMap,String schoolName);
	Map<Integer,List<AverageExcelVo>> getLevelAssignMapBySchoolName(Map<Integer,List<AverageExcelVo>> levelMap,String schoolName);

	Map<Integer,List<AverageExcelVo>> getTopTenLevelMapSumBySchoolName(Map<Integer,List<AverageExcelVo>> levelMap,List<String> classCode,String schoolName);

	/**
	 * @param levelMap
	 * @param schoolName
	 * @return 某个学校的前10等级人数
	 */
	Map<Integer,List<AverageExcelVo>> getTopTenLevelMapBySchoolName(Map<Integer,List<AverageExcelVo>> levelMap,List<String> classCode,String schoolName);

	/**
	 * @param levelMap
	 * @param levelMapDecimal
	 * @param schoolName
	 * @return  某个学校的指标
	 */
	Map<Integer, BigDecimal> getLevelMapDecimalBySchoolName(Map<Integer,List<AverageExcelVo>> levelMap,Map<Integer,BigDecimal> levelMapDecimal,String schoolName);
	Map<String, Map<String, BigDecimal>> calculate(Map<Integer,List<AverageExcelVo>> schoolLevelMap,Map<Integer,BigDecimal> schoolLevelMapDecimal,List<String> classCodes);
	Map<String, Map<String, BigDecimal>> calculateTopTen(Map<Integer,List<AverageExcelVo>> schoolTopTenLevelMap,Map<Integer,List<AverageExcelVo>> schoolLevelMap,Map<Integer,BigDecimal> schoolLevelMapDecimal,List<String> classCodes);
	void calculateAssign(Map<Integer,List<AverageExcelVo>> schoolLevelMap);
	void calculateAssignScore(Map<Integer,List<AverageExcelVo>> schoolLevelMap,List<AverageAssignExcelVo> averageAssignExcelVos);
}
