package com.zzy.pony.exam.service;







import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.dao.ExamineeRoomArrangeDao;
import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamArrangeGroup;
import com.zzy.pony.exam.model.ExamRoomAllocate;
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.ExamVo;







@Service
@Transactional
public class ExamineeRoomArrangeServiceImpl implements ExamineeRoomArrangeService {

	static Logger log=LoggerFactory.getLogger(ExamineeRoomArrangeServiceImpl.class);

	
	@Autowired
	private ExamineeRoomArrangeDao examineeRoomArrangeDao;
	@Autowired
	private ExamService examService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private ExamArrangeService examArrangeService;
	@Autowired
	private ExamRoomService examRoomService;
	
	
	
	
	@Override
	public void autoExamineeRoomArrange(int examId, int gradeId) {
		// TODO Auto-generated method stub
		
		int autoMode = 2;//2 默认按照考场容量排 1 按照考场平均排
		
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		//1 先将之前排的考场删除
		examineeRoomArrangeDao.deleteAll();
		//2 确定要排的考场，其中有组ID的一起排，没有组ID的单独排
		List<ExamVo> examVos = examService.findByYearAndTermOrderByExamDate(year, term); 
		ExamVo examVo = examVos.get(0);//当前考试
		List<ExamArrange> examArranges = examArrangeService.findByExam(examVo.getExamId());//所有要安排的考试
		List<ExamArrangeGroup> examArrangeGroups = examArrangeService.findByExamAndGroup(examId, gradeId);//所有处于同一组的考试 
		Map<Integer, String> groupMap = new HashMap<Integer, String>();
		for (ExamArrangeGroup examArrangeGroup : examArrangeGroups) {
			List<ExamArrange> eas = examArrangeGroup.getExamArranges();
			for (ExamArrange examArrange : eas) {
				if (groupMap.get(examArrangeGroup.getGroupId()) != null) {
					groupMap.put(examArrangeGroup.getGroupId(), groupMap.get(examArrangeGroup.getGroupId())+";"+examArrange.getArrangeId());
				}else {
					groupMap.put(examArrangeGroup.getGroupId(), examArrange.getArrangeId()+"");
				}
			}
		}
		
		//3排考场(排不在组里面的)
		for (ExamArrange examArrange : examArranges) {
			//根据examArrange分别去找考生以及考场
			List<Examinee> examinees = examArrange.getExaminees();//所有该门考试的考生
			List<ExamRoomAllocate> examRoomAllocates = examRoomService.findByExamArrange(examArrange);//所有该门考试的考场
			//同班同学不相临			
			if (autoMode == Constants.AUTO_MODE_ONE && examArrange.getGroup() == null) {
				//考生平均分配到考场
				int examineeCount = examinees.size();
				int examRoomCount = examRoomAllocates.size();
				int averageExaminee = examineeCount/examineeCount;//每个考场分配多少考生
				int remainExaminee = examineeCount%examineeCount;//剩余的考生
				//将所有考生排序
				Collections.sort(examinees);
				
				
				
			}
			if (autoMode == Constants.AUTO_MODE_TWO && examArrange.getGroup() == null) {
				//按考场容量分配
			}
			
			
			
		}
		
		
		
		
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		examineeRoomArrangeDao.deleteAll();
	}
	
	
	

	
	
	
	
	
	
	

	
	
	

	
	
}
