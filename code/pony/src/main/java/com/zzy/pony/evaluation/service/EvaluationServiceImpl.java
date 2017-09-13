package com.zzy.pony.evaluation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.evaluation.dao.EvaluationItemDao;
import com.zzy.pony.evaluation.dao.EvaluationItemDataDao;
import com.zzy.pony.evaluation.dao.EvaluationRecordDao;
import com.zzy.pony.evaluation.mapper.EvaluationItemDataMapper;
import com.zzy.pony.evaluation.model.EvaluationItem;
import com.zzy.pony.evaluation.model.EvaluationItemData;
import com.zzy.pony.evaluation.model.EvaluationRecord;
import com.zzy.pony.evaluation.model.EvaluationSubject;
import com.zzy.pony.evaluation.vo.EvaluationItemDataVo;
import com.zzy.pony.evaluation.vo.EvaluationItemVo;
import com.zzy.pony.evaluation.vo.EvaluationRecordVo;
import com.zzy.pony.evaluation.vo.EvaluationRowVo;
import com.zzy.pony.model.Teacher;
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {
	private String EVL_TIME="2017";
	@Autowired
	private EvaluationItemDao itemDao;
	@Autowired
	private EvaluationRecordDao recordDao;
	@Autowired
	private EvaluationItemDataDao dataDao;
	@Autowired
	private EvaluationItemDataMapper dataMapper;

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
	@Override
	public List<EvaluationRowVo> itemTableData(Long subjectId) {
		List<EvaluationItemVo> list=itemTreeData(subjectId);//取得树形结构
		List<EvaluationRowVo> result=new ArrayList<EvaluationRowVo>();
		for(EvaluationItemVo vo: list) {
			EvaluationRowVo rvo=new EvaluationRowVo();
			if(vo.getChildren().size() == 0) {//没有子节点，不用设置category
				rvo.setColspan(2);//固定为2
				rvo.setDescription(vo.getDescription());
				rvo.setItemId(vo.getItemId());
				rvo.setName(vo.getName());
				rvo.setScore(vo.getScore());
				
				result.add(rvo);
			}else {
				EvaluationItemVo firstChild=vo.getChildren().get(0);//父节点和第一个子节点组成一行
				if(vo.getScore()>0)
					rvo.setCategory(vo.getName()+"("+vo.getScore()+")");
				else {
					rvo.setCategory(vo.getName());
				}
				rvo.setCategoryRowspan(vo.getChildren().size());
				rvo.setColspan(1);//固定为1
				rvo.setDescription(firstChild.getDescription());
				rvo.setItemId(firstChild.getItemId());
				rvo.setName(firstChild.getName());
				rvo.setScore(firstChild.getScore());
				result.add(rvo);
				//插入其它子节点
				for(int i=1;i<vo.getChildren().size();i++) {
					EvaluationItemVo child=vo.getChildren().get(i);
					rvo=new EvaluationRowVo();//新建对象
					rvo.setColspan(1);//固定为1
					rvo.setDescription(child.getDescription());
					rvo.setItemId(child.getItemId());
					rvo.setName(child.getName());
					rvo.setScore(child.getScore());
					result.add(rvo);
				}
				
			}
		}
		return result;
	}
	@Override
	public void addRecord(EvaluationRecordVo record, Integer teacherId, String loginName) {
		EvaluationSubject subject=new EvaluationSubject();
		subject.setSubjectId(record.getSubjectId());
		Teacher teacher=new Teacher();
		teacher.setTeacherId(teacherId);
		Date now=new Date();
		EvaluationRecord er=new EvaluationRecord();
		er.setCreateTime(now);
		er.setCreateUser(loginName);
		er.setEvlTime(EVL_TIME);//TODO
		er.setSubject(subject);
		er.setTeacher(teacher);
		recordDao.save(er);
		for(EvaluationItemDataVo vo: record.getItemData()) {
			EvaluationItem item=new EvaluationItem();
			item.setItemId(vo.getItemId());
			EvaluationItemData ed=new EvaluationItemData();
			ed.setAccording(vo.getAccording());
			ed.setCreateTime(now);
			ed.setCreateUser(loginName);
			ed.setItem(item);
			ed.setRecord(er);
			ed.setScore(vo.getInputScore());
			dataDao.save(ed);
		}
	}
	@Override
	public EvaluationRecordVo findBySubjectAndTeacher(Long subjectId, Integer teacherId) {
		EvaluationSubject subject=new EvaluationSubject();
		subject.setSubjectId(subjectId);
		Teacher teacher=new Teacher();
		teacher.setTeacherId(teacherId);
		EvaluationRecord record=recordDao.findBySubjectAndTeacherAndEvlTime(subject, teacher, EVL_TIME);
		EvaluationRecordVo result=new EvaluationRecordVo();
		if(record != null) {
			result.setCheckTime(record.getCheckTime());
			result.setCheckUser(record.getCheckUser());
			result.setComments(record.getComments());
			result.setCreateTime(record.getCreateTime());
			result.setCreateUser(record.getCreateUser());
			result.setEvlResult(record.getEvlResult());
			result.setEvlTime(record.getEvlTime());
			result.setItemData(dataMapper.findByRecord(record.getRecordId()));
			result.setRank(record.getRank());
			result.setRecordId(record.getRecordId());
			result.setSubjectId(record.getSubject().getSubjectId());
			result.setSubjectName(record.getSubject().getName());
			result.setTeacherId(record.getTeacher().getTeacherId());
			result.setTeacherName(record.getTeacher().getName());
			result.setTeacherNo(record.getTeacher().getTeacherNo());
			result.setTotalScore(record.getTotalScore());
		}
		return result;
	}
	@Override
	public void updateRecord(EvaluationRecordVo record, String loginName) {
		EvaluationRecord er=recordDao.findOne(record.getRecordId());
		for(EvaluationItemDataVo vo: record.getItemData()) {
			if(vo.getId() !=null && vo.getId() != 0) {
				dataDao.updateScore(vo.getId(), vo.getInputScore(), vo.getAccording());
			}else {
				EvaluationItem item=new EvaluationItem();
				item.setItemId(vo.getItemId());
				EvaluationItemData ed=new EvaluationItemData();
				ed.setAccording(vo.getAccording());
				ed.setCreateTime(new Date());
				ed.setCreateUser(loginName);
				ed.setItem(item);
				ed.setRecord(er);
				ed.setScore(vo.getInputScore());
				dataDao.save(ed);
			}
			
		}
	}
}
