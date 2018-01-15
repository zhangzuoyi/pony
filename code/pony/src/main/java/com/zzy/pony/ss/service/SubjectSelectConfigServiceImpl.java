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

		// add 新增为 "当前" 状态,需将其他置为 "非当前"
		if (IS_CURRENT_Y.equals(config.getIsCurrent())) {
			List<SubjectSelectConfig> configs = dao.findByIsCurrent(IS_CURRENT_Y);
			for (SubjectSelectConfig subjectSelectConfig : configs) {
				subjectSelectConfig.setIsCurrent(IS_CURRENT_N);
			}
			dao.save(configs);
		}
		dao.save(config);

	}

	@Override
	public void update(SubjectSelectConfig config, String loginName) {
		SubjectSelectConfig old = dao.findOne(config.getConfigId());
		// add 新增为 "当前" 状态,需将其他置为 "非当前"
		if (IS_CURRENT_Y.equals(config.getIsCurrent())) {
			List<SubjectSelectConfig> configs = dao.findAll();
			configs.remove(old);
			for (SubjectSelectConfig subjectSelectConfig : configs) {
				subjectSelectConfig.setIsCurrent(IS_CURRENT_N);
			}
			dao.save(configs);
		}
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
		List<SubjectSelectConfig> list = dao.findByIsCurrent(IS_CURRENT_Y);
		if (list != null && list.size() > 0) {
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
