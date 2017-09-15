package com.zzy.pony.exam.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.exam.mapper.ExamMonitorMapper;
import com.zzy.pony.exam.mapper.ExamRoomAllocateMapper;
import com.zzy.pony.exam.vo.ExamMonitorVo;
import com.zzy.pony.exam.vo.ExamRoomAllocateVo;

@Service
@Transactional
public class ExamMonitorServiceImpl implements ExamMonitorService {
	@Autowired
	private ExamMonitorMapper mapper;
	@Autowired
	private ExamRoomAllocateMapper allocateMapper;

	@Override
	public void add(int examId, int gradeId, int[] teacherIds) {
		for(int teacherId: teacherIds){
			mapper.insert(examId,gradeId, teacherId);
		}

	}

	@Override
	public void setCount(int examId,int gradeId, int[] teacherIds, int count) {
		mapper.setCount(examId,gradeId, teacherIds, count);
	}

	@Override
	public void delete(int[] ids) {
		mapper.deleteByIds(ids);
	}

	@Override
	public void monitorArrange(int examId, int gradeId) {
		//查找考场安排数据
		List<ExamRoomAllocateVo> rooms=allocateMapper.findByExam(examId);
		//查找监考老师数据
		List<ExamMonitorVo> monitors=mapper.find(examId, gradeId);
		for(ExamMonitorVo em: monitors){
			em.init();
		}
		//自动安排监考老师
		for(ExamRoomAllocateVo vo: rooms){
			vo.setTeacherId(null);
			String timeAllocated=vo.getTimeAllocated();
			for(ExamMonitorVo em: monitors){
				//剩余有监考次数，已安排的时段不重复，所教科目与考试科目不同
				if(em.getCountLeft()>0 && ! em.getTimeAllocated().contains(timeAllocated) && ! em.getSubjectName().equals(vo.getSubjectName())){
					vo.setTeacherId(em.getTeacherId());
					em.getTimeAllocated().add(timeAllocated);
					em.setCountLeft(em.getCountLeft() - 1);
					break;
				}
			}
		}
		//保存安排结果
		allocateMapper.setMonitorEmpty(examId);
		for(ExamRoomAllocateVo vo: rooms){
			allocateMapper.updateMonitor(vo.getRoomId(), vo.getTeacherId());
		}
	}

}
