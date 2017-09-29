package com.zzy.pony.exam.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.TeacherDao;
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
	@Autowired
	private TeacherDao teacherDao;

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
		List<ExamRoomAllocateVo> rooms=allocateMapper.findByExam(examId, gradeId);
		//查找监考老师数据
		List<ExamMonitorVo> monitors=mapper.find(examId, gradeId);
		for(ExamMonitorVo em: monitors){
			em.init();
		}
		//自动安排监考老师
		for(ExamRoomAllocateVo vo: rooms){
			vo.setTeacherId(null);
			for(ExamMonitorVo em: monitors){
				//剩余有监考次数，已安排的时段不重复，所教科目与考试科目不同，不安排同一考场
				if(isFit(em, vo)){
					addArrange(em, vo);
					break;
				}
			}
		}
		//未安排监考老师的试场，尝试调换
		for(ExamRoomAllocateVo vo: rooms){
			if(vo.getTeacherId() == null) {
				for(ExamMonitorVo em: monitors){
					if(em.getCountLeft()>0) {
						changeArrange(vo, em, rooms, monitors);
						break;
					}
				}
			}
		}
		System.out.println("未安排监考老师的试场：");
		for(ExamRoomAllocateVo vo: rooms){
			if(vo.getTeacherId() == null)
				System.out.println(vo.getRoomName()+"-"+vo.getTimeAllocated()+"-"+vo.getSubjectName());
		}
		System.out.println("剩余未安排的监考老师：");
		for(ExamMonitorVo em: monitors){
			if(em.getCountLeft()>0)
				System.out.println(em.getTeacherId()+"-"+em.getTeacherName()+"-"+em.getSubjectName()+"-"+em.getCountLeft());
		}
		//保存安排结果
		allocateMapper.setMonitorEmpty(examId);
		for(ExamRoomAllocateVo vo: rooms){
			allocateMapper.updateMonitor(vo.getRoomId(), vo.getTeacherId());
		}
	}
	/**
	 * 考场和老师是否匹配安排
	 * @param em
	 * @param vo
	 * @return
	 */
	private boolean isFit(ExamMonitorVo em,ExamRoomAllocateVo vo) {
		if(em.getCountLeft()>0 && ! em.getTimeAllocated().contains(vo.getTimeAllocated()) 
				&& ! em.getSubjectName().equals(vo.getSubjectName()) && ! em.getRoomAllocated().contains(vo.getRoomName())){
			return true;
		}
		return false;
	}
	private void addArrange(ExamMonitorVo em,ExamRoomAllocateVo vo) {
		vo.setTeacherId(em.getTeacherId());
		em.getTimeAllocated().add(vo.getTimeAllocated());
		em.setCountLeft(em.getCountLeft() - 1);
		em.getRoomAllocated().add(vo.getRoomName());
	}
	/**
	 * 因规则限制监考老师不能安排在此考场，可跟已安排好考场记录进行调换
	 * @param vo 没有监考老师的考场
	 * @param em 还未排满的监考老师
	 * @param rooms
	 * @param monitors
	 */
	private void changeArrange(ExamRoomAllocateVo vo,ExamMonitorVo em,List<ExamRoomAllocateVo> rooms,List<ExamMonitorVo> monitors) {
		//试场冲突，跟同一时间段的交换
		if(em.getRoomAllocated().contains(vo.getRoomName())) {
			for(ExamRoomAllocateVo avo: rooms) {
				if(vo.getTimeAllocated().equals(avo.getTimeAllocated()) && ! em.getRoomAllocated().contains(avo.getRoomName())) {//达到可交换条件
					ExamMonitorVo aem=removeArrange(monitors,avo);
					if(isFit(em, avo) && isFit(aem, vo)) {//是否符合交换
						addArrange(em, avo);
						addArrange(aem, vo);
						break;
					}else {//把移除的放回去
						addArrange(aem, avo);
					}
				}
			}
		}
		//科目冲突，按顺序尝试交换
		if(em.getSubjectName().equals(vo.getSubjectName())) {
			for(ExamRoomAllocateVo avo: rooms) {
				if( ! em.getSubjectName().equals(avo.getSubjectName())) {
					ExamMonitorVo aem=removeArrange(monitors,avo);//先移除
					if(isFit(em, avo) && isFit(aem, vo)) {//是否符合交换
						addArrange(em, avo);
						addArrange(aem, vo);
						break;
					}else {//把移除的放回去
						addArrange(aem, avo);
					}
				}
			}
		}
		//暂不考虑时间冲突
	}
	/**
	 * 移除安排
	 * @param monitors
	 * @param vo
	 */
	private ExamMonitorVo removeArrange(List<ExamMonitorVo> monitors,ExamRoomAllocateVo vo) {
		for(ExamMonitorVo em: monitors) {
			if(em.getTeacherId().equals(vo.getTeacherId())) {
				vo.setTeacherId(null);
				em.getTimeAllocated().remove(vo.getTimeAllocated());
				em.setCountLeft(em.getCountLeft() + 1);
				em.getRoomAllocated().remove(vo.getRoomName());
				return em;
			}
		}
		return null;
	}

	@Override
	public void add(int examId, int gradeId, List<ExamMonitorVo> list) {
		for(ExamMonitorVo vo : list){
			Integer teacherId=teacherDao.findIdByTeacherNo(vo.getTeacherNo());
			if(teacherId != null){
				mapper.insertWithCount(examId,gradeId, teacherId, vo.getMonitorCount());
			}
		}
		
	}

}
