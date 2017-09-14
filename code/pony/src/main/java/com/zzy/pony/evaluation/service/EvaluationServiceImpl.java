package com.zzy.pony.evaluation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.evaluation.dao.EvaluationItemDao;
import com.zzy.pony.evaluation.dao.EvaluationItemDataDao;
import com.zzy.pony.evaluation.dao.EvaluationRecordDao;
import com.zzy.pony.evaluation.mapper.EvaluationItemDataMapper;
import com.zzy.pony.evaluation.mapper.EvaluationRecordMapper;
import com.zzy.pony.evaluation.mapper.OutcomeMapper;
import com.zzy.pony.evaluation.model.EvaluationItem;
import com.zzy.pony.evaluation.model.EvaluationItemData;
import com.zzy.pony.evaluation.model.EvaluationRecord;
import com.zzy.pony.evaluation.model.EvaluationSubject;
import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.vo.EvaluationItemDataVo;
import com.zzy.pony.evaluation.vo.EvaluationItemVo;
import com.zzy.pony.evaluation.vo.EvaluationRecordVo;
import com.zzy.pony.evaluation.vo.EvaluationRowVo;
import com.zzy.pony.evaluation.vo.OutcomeVo;
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
	@Autowired
	private OutcomeMapper outcomeMapper;
	@Autowired
	private EvaluationRecordMapper recordMapper;

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
			vo.setDataSource(item.getDataSource());
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
		old.setDataSource(item.getDataSource());
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
				rvo.setDataSource(vo.getDataSource());
				
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
				rvo.setDataSource(firstChild.getDataSource());
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
					rvo.setDataSource(child.getDataSource());
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
		er.setStatus(EvaluationRecord.STATUS_UNCHECK);
		recordDao.save(er);
		
		List<OutcomeVo> outcomes=outcomeMapper.findByTeacherAndStatus(teacherId,Outcome.STATUS_CHECKED);//老师成果
		for(EvaluationItemDataVo vo: record.getItemData()) {
			EvaluationItem item=itemDao.findOne(vo.getItemId());
			EvaluationItemData ed=new EvaluationItemData();
			ed.setCreateTime(now);
			ed.setCreateUser(loginName);
			ed.setItem(item);
			ed.setRecord(er);
			if(StringUtils.isBlank(item.getDataSource())) {
				ed.setAccording(vo.getAccording());
				ed.setScore(vo.getInputScore());
			}else {
				setScoreFromOutcome(item, outcomes, ed);
			}
			
			dataDao.save(ed);
		}
	}
	private void setScoreFromOutcome(EvaluationItem item, List<OutcomeVo> outcomes, EvaluationItemData ed) {
		float result=0;
		StringBuilder according=new StringBuilder();
		for(OutcomeVo vo: outcomes) {
			if(vo.getCategory().equals(item.getDataSource())) {
				result +=vo.getScore();
				according.append(vo.getCategory()+"-"+vo.getLevel1()+"-"+vo.getLevel2()+":"+vo.getScore()+"分\n");
			}
		}
		ed.setScore(result > item.getScore() ? item.getScore() : result);
		ed.setAccording(according.toString());
	}
	@Override
	public EvaluationRecordVo findBySubjectAndTeacher(Long subjectId, Integer teacherId) {
		EvaluationSubject subject=new EvaluationSubject();
		subject.setSubjectId(subjectId);
		Teacher teacher=new Teacher();
		teacher.setTeacherId(teacherId);
		EvaluationRecord record=recordDao.findBySubjectAndTeacherAndEvlTime(subject, teacher, EVL_TIME);
		
		return toRecordVo(record);
	}
	private EvaluationRecordVo toRecordVo(EvaluationRecord record) {
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
			result.setStatus(record.getStatus());
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
	@Override
	public List<EvaluationRecordVo> findRecords(Long subjectId) {
		return recordMapper.findBySubjectId(subjectId);
	}
	@Override
	public EvaluationRecordVo findRecordById(Long recordId) {
		EvaluationRecord record=recordDao.findOne(recordId);
		
		return toRecordVo(record);
	}
	@Override
	public void checkRecord(EvaluationRecordVo record, String loginName) {
		EvaluationRecord er=recordDao.findOne(record.getRecordId());
		BigDecimal bd=new BigDecimal(0);
		Date now=new Date();
		for(EvaluationItemDataVo vo: record.getItemData()) {
			dataDao.updateCheckScore(vo.getId(), vo.getCheckScore(), now, loginName);
			bd=bd.add(new BigDecimal(String.valueOf(vo.getCheckScore())));
		}
		er.setCheckTime(now);
		er.setCheckUser(loginName);
		er.setTotalScore(bd.floatValue());
		er.setStatus(EvaluationRecord.STATUS_CHECKED);
		recordDao.save(er);
	}
}
