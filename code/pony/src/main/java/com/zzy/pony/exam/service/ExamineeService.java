package com.zzy.pony.exam.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.vo.ExamineeVo;




public interface ExamineeService {
	
	void generateNo(int examId,int gradeId,String prefixNo,int bitNo);
	Page<ExamineeVo> findPageByClass(int currentPage,int pageSize,int examId,int classId);
	Page<ExamineeVo> findPageByClassAndArrange(int currentPage,int pageSize,int examId,int classId,int arrangeId);
	List<Examinee> findByExamIdAndClassId(int examId,int classId);
	List<Examinee> findByArrangeId(int arrangeId);

	
}
