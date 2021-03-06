package com.zzy.pony.exam.mapper;

import java.util.List;

import com.zzy.pony.vo.RankVo;
import org.apache.ibatis.annotations.Param;

import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.vo.ExamineeVo;

public interface ExamineeMapper {
	List<ExamineeVo> findPageByClass(int offset,int pageSize, int examId, int classId);
	List<ExamineeVo> findByClass(int examId, int classId);
	int findCountByClass(int examId, int classId);
	List<ExamineeVo> findPageByClassAndArrange(int offset,int pageSize, int examId, int classId,int arrangeId);
	int findCountByClassAndArrange(int examId, int classId,int arrangeId);
	List<Examinee> findByExamIdAndClassId(int examId,int classId);
	List<Examinee> findByArrangeId(int arrangeId);
	List<Examinee> findByExamIdAndClassIds(int examId,@Param(value="classIds")int[] classIds);
	List<RankVo> findRankByExam(int examId,int yearId,int gradeId);
	void deleteExamineeArrangeByClass(int examId,int arrangeId,@Param(value="classIds")int[] classIds);
	void insertExamineeArrangeByClass(int examId,int arrangeId,@Param(value="classIds")int[] classIds);
	void insertExamineeArrangeByExaminee(int arrangeId,int examineeId);
	void deleteExamineeArrangeByArrangeId(int arrangeId);
	List<ExamineeVo> findVoByArrangeId(int arrangeId,int yearId);
	/**
	 * 根据考生的考试科目
	 * @param examId
	 * @param subjects
	 * @return
	 */
	List<ExamineeVo> findByExamIdAndSubjects(int examId,@Param(value="subjects")String[] subjects);
	List<ExamineeVo> findByExam(int examId);
}