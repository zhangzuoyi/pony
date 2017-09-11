package com.zzy.pony.evaluation.service;

import java.util.List;

import com.zzy.pony.evaluation.model.EvaluationItem;
import com.zzy.pony.evaluation.vo.EvaluationItemVo;

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
}
