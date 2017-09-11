package com.zzy.pony.evaluation.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.evaluation.dao.EvaluationItemDao;
import com.zzy.pony.evaluation.model.EvaluationItem;
import com.zzy.pony.evaluation.model.EvaluationSubject;
import com.zzy.pony.evaluation.vo.EvaluationItemVo;
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {
	@Autowired
	private EvaluationItemDao itemDao;

	@Override
	public List<EvaluationItemVo> itemTreeData(Long subjectId) {
		EvaluationSubject subject=new EvaluationSubject();
		subject.setSubjectId(subjectId);
		List<EvaluationItem> items=itemDao.findBySubject(subject);
		List<EvaluationItemVo> vos=new ArrayList<EvaluationItemVo>();
		for(EvaluationItem item : items) {
			EvaluationItemVo vo=new EvaluationItemVo();
			vo.setDescription(item.getDescription());
			vo.setItemId(item.getItemId());
			vo.setLevel(item.getLevel());
			vo.setName(item.getName());
			vo.setParentItemId(item.getParentItemId());
			vo.setScore(item.getScore());
			vo.setSeq(item.getSeq());
			vo.setType(item.getType());
			vos.add(vo);
		}
		List<EvaluationItemVo> result=new ArrayList<EvaluationItemVo>();
		for(EvaluationItemVo vo : vos) {
			if(EvaluationItem.TYPE_DIR.equals(vo.getType())) {//目录，需要放入其子节点
				addChildren(vo, vos);
			}
			if(vo.getLevel() == 1) {//第一级的节点需要放入结果
				result.add(vo);
			}
		}
		return result;
	}
	private void addChildren(EvaluationItemVo item, List<EvaluationItemVo> vos) {
		for(EvaluationItemVo vo : vos) {
			if(item.getItemId().equals(vo.getParentItemId())) {
				item.getChildren().add(vo);
				vo.setParentItemName(item.getName());
			}
		}
	}
	@Override
	public void addItem(EvaluationItem item) {
		itemDao.save(item);
	}
	@Override
	public void updateItem(EvaluationItem item) {
		EvaluationItem old=itemDao.findOne(item.getItemId());
		old.setDescription(item.getDescription());
		old.setName(item.getName());
		old.setScore(item.getScore());
		old.setSeq(item.getSeq());
		int childCount=itemDao.findCountByParentItemId(old.getItemId());
		if(childCount > 0) {
			old.setType(EvaluationItem.TYPE_DIR);//有子节点
		}else {
			old.setType(item.getType());
		}
		itemDao.save(old);
	}
	@Override
	public void deleteItem(Long itemId) {
		itemDao.delete(itemId);
		
	}
}
