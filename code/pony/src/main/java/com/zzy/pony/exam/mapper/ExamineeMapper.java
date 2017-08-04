package com.zzy.pony.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.vo.ExamineeVo;

public interface ExamineeMapper {
	List<ExamineeVo> findPageByClass(int offset,int pageSize, int examId, int classId);
	int findCountByClass(int examId, int classId);
	List<ExamineeVo> findPageByClassAndArrange(int offset,int pageSize, int examId, int classId,int arrangeId);
	int findCountByClassAndArrange(int examId, int classId,int arrangeId);
	List<Examinee> findByExamIdAndClassId(int examId,int classId);
	List<Examinee> findByArrangeId(int arrangeId);
	List<Examinee> findByExamIdAndClassIds(int examId,@Param(value="classIds")int[] classIds);

}
