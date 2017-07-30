package com.zzy.pony.ss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.UserDao;
import com.zzy.pony.model.User;
import com.zzy.pony.ss.dao.StudentSubjectSelectDao;
import com.zzy.pony.ss.model.StudentSubjectSelect;
import com.zzy.pony.ss.model.SubjectSelectConfig;
@Service
@Transactional
public class StudentSubjectSelectServiceImpl implements StudentSubjectSelectService {
	@Autowired
	private StudentSubjectSelectDao dao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SubjectSelectConfigService configService;

	@Override
	public List<String> findCurrentSelect(String loginName) {
		User user=userDao.findByLoginName(loginName);
		SubjectSelectConfig config=configService.getCurrent();
		if(user.getStudent() != null && config != null){
			List<StudentSubjectSelect> list=dao.findBySubjectSelectConfigAndStudent(config, user.getStudent());
			List<String> result=new ArrayList<String>();
			for(StudentSubjectSelect ss: list){
				result.add(ss.getSubject());
			}
			return result;
		}
		return null;
	}

	@Override
	public void save(Integer configId, List<String> subjects, String loginName) {
		User user=userDao.findByLoginName(loginName);
		SubjectSelectConfig config=configService.getCurrent();
		if(user.getStudent() != null && config != null){
			List<StudentSubjectSelect> list=dao.findBySubjectSelectConfigAndStudent(config, user.getStudent());
			//老的选择不在新选择范围内的，需要删除
			for(StudentSubjectSelect ss:list){
				boolean isFind=false;
				for(String subject: subjects){
					if(ss.getSubject().equals(subject)){
						isFind=true;
						break;
					}
				}
				if( ! isFind){
					dao.delete(ss);
				}
			}
			//新的选择不在老范围内的，需要新增
			for(String subject: subjects){
				boolean isFind=false;
				for(StudentSubjectSelect ss: list){
					if(subject.equals(ss.getSubject())){
						isFind=true;
						break;
					}
				}
				if( ! isFind){
					StudentSubjectSelect ss=new StudentSubjectSelect();
					ss.setCreateTime(new Date());
					ss.setStudent(user.getStudent());
					ss.setSubject(subject);
					ss.setSubjectSelectConfig(config);
					
					dao.save(ss);
				}
			}
		}
		
	}

}
