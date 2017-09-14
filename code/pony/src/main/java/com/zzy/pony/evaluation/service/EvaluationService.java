package com.zzy.pony.evaluation.service;

import java.util.List;

import com.zzy.pony.evaluation.model.EvaluationItem;
import com.zzy.pony.evaluation.vo.EvaluationItemVo;
import com.zzy.pony.evaluation.vo.EvaluationRecordVo;
import com.zzy.pony.evaluation.vo.EvaluationRowVo;

public interface EvaluationService {
	/**
	 * 评价项的树形结构
	 * @param subjectId
	 * @return
	 */
	List<EvaluationItemVo> itemTreeData(Long subjectId);
	void addItem(EvaluationItem item);
	void updateItem(EvaluationItem item);
	void deleteItem(Long itemId);
	/**
	 * 评价项的表格结构
	 * @param subjectId
	 * @return
	 */
	List<EvaluationRowVo> itemTableData(Long subjectId);
	void addRecord(EvaluationRecordVo record, Integer teacherId, String loginName);
	void updateRecord(EvaluationRecordVo record, String loginName);
	EvaluationRecordVo findBySubjectAndTeacher(Long subjectId, Integer teacherId);
	List<EvaluationRecordVo> findRecords(Long subjectId);
	EvaluationRecordVo findRecordById(Long recordId);
	void checkRecord(EvaluationRecordVo record, String loginName);
}
