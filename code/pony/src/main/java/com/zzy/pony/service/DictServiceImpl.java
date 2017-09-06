package com.zzy.pony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.CommonDictDao;
import com.zzy.pony.model.CommonDict;

@Service
public class DictServiceImpl implements DictService {
	@Autowired
	private CommonDictDao dao;

	
	
	
	@Override
	public List<CommonDict> list() {
		// TODO Auto-generated method stub
		Sort sort = new Sort("dictType"); 
		return dao.findAll(sort);
	}

	@Override
	public void add(CommonDict commonDict) {
		// TODO Auto-generated method stub
		dao.save(commonDict);
	}

	@Override
	public void update(CommonDict commonDict) {
		// TODO Auto-generated method stub
		CommonDict old = dao.findOne(commonDict.getDictId());
		old.setCode(commonDict.getCode());
		old.setValue(commonDict.getValue());
		dao.save(old);
	}

	@Override
	public void delete(int dictId) {
		// TODO Auto-generated method stub
		dao.delete(dictId);
	}
	//按照字典值code查看是否存在
	@Override
	public Boolean isExist(CommonDict commonDict) {
		// TODO Auto-generated method stub
		List<CommonDict> commonDicts = dao.findByDictTypeAndCode(commonDict.getDictType(), commonDict.getCode());
		if (commonDicts == null || commonDicts.isEmpty()) {
			return false;
		}
		return true;
	}

	
	
	@Override
	public List<CommonDict> listByDictType(String dictType) {
		// TODO Auto-generated method stub
		return dao.findByDictType(dictType);
	}

	@Override
	public List<CommonDict> findSexes() {
		return dao.findByDictType("sex");
	}

	@Override
	public List<CommonDict> findCredentials() {
		return dao.findByDictType("credential");
	}

	@Override
	public List<CommonDict> findStudentStatus() {
		return dao.findByDictType("student_status");
	}

	@Override
	public List<CommonDict> findEducationDegrees() {
		return dao.findByDictType("edu_degree");
	}

	@Override
	public List<CommonDict> findStudentRemarkLevels() {
		return dao.findByDictType("stu_remark_level");
	}

	@Override
	public List<CommonDict> findStudentTypes() {
		return dao.findByDictType("student_type");
	}

	@Override
	public List<CommonDict> findPropertyStatus() {
		// TODO Auto-generated method stub
		return dao.findByDictType(Constants.DICT_PROPERTY_STATUS);
	}

	@Override
	public List<CommonDict> findSubjectTypes() {
		// TODO Auto-generated method stub
		return dao.findByDictType(Constants.DICT_SUBJECT_TYPE);
	}

	@Override
	public List<CommonDict> findImportances() {
		// TODO Auto-generated method stub
		return dao.findByDictType(Constants.DICT_IMPORTANCE);
	}
	
	

	
	
	
	
	

}
