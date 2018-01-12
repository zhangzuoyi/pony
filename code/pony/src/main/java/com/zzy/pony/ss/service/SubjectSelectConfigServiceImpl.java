package com.zzy.pony.ss.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.ss.dao.SubjectSelectConfigDao;
import com.zzy.pony.ss.model.SubjectSelectConfig;
@Service
@Transactional
public class SubjectSelectConfigServiceImpl implements SubjectSelectConfigService {
	@Autowired
	private SubjectSelectConfigDao dao;

	@Override
	public void add(SubjectSelectConfig config) {
		dao.save(config);

	}

	@Override
	public void update(SubjectSelectConfig config, String loginName) {
		SubjectSelectConfig old=dao.findOne(config.getConfigId());
		old.setEndTime(config.getEndTime());
		old.setIsCurrent(config.getIsCurrent());
		old.setSelectNum(config.getSelectNum());
		old.setStartTime(config.getStartTime());
		old.setSubjects(config.getSubjects());
		old.setUpdateTime(new Date());
		old.setUpdateUser(loginName);
		old.setGrades(config.getGrades());
		
		dao.save(old);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);

	}

	@Override
	public List<SubjectSelectConfig> findAll() {
		return dao.findAll();
	}

	@Override
	public SubjectSelectConfig getCurrent() {
		List<SubjectSelectConfig> list=dao.findByIsCurrent(IS_CURRENT_Y);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public SubjectSelectConfig get(int configId) {
		// TODO Auto-generated method stub
		return dao.findOne(configId);
	}
	
	
	

}
