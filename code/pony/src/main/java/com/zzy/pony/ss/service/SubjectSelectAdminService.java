package com.zzy.pony.ss.service;

import java.util.List;

import com.zzy.pony.ss.vo.StudentSubjectAdminVo;

public interface SubjectSelectAdminService {
	
	List<StudentSubjectAdminVo> list(int configId,int studentId,String group);
	
	void update(List<String> subjects,int studentId,int configId);
	
}
