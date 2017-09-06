package com.zzy.pony.exam.service;






import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.exam.dao.ExamArrangeDao;
import com.zzy.pony.exam.mapper.ExamineeMapper;
import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.Examinee;






@Service
@Transactional
public class ExamineeArrangeServiceImpl implements ExamineeArrangeService {
	private static final Logger log=LoggerFactory.getLogger(ExamineeArrangeServiceImpl.class);
	@Autowired
	private ExamineeService examineeService;
	@Autowired
	private ExamArrangeDao examArrangeDao;
	@Autowired
	private ExamineeMapper mapper;
	
	@Override
	public void submitByClass(int examId, int[] classIds, int[] arrangeIds) {
//		List<Examinee> examinees = new ArrayList<Examinee>();
		/*for (int classId : classIds) {
			examinees.addAll(examineeService.findByExamIdAndClassId(examId, classId));
		}*/
//		examinees = examineeService.findByExamIdAndClassIds(examId, classIds);
//		List<ExamArrange> examArranges = new ArrayList<ExamArrange>();
//		for (int arrangeId : arrangeIds) {
//			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
//			examArrange.setExaminees(examinees);
//			examArranges.add(examArrange);
//		}
//		examArrangeDao.save(examArranges);
		for (int arrangeId : arrangeIds) {
			mapper.deleteExamineeArrangeByClass(examId, arrangeId, classIds);//先删除
			mapper.insertExamineeArrangeByClass(examId, arrangeId, classIds);//再新增
		}
	}

	@Override
	public void submitByStudent(int[] examineeIds, int[] arrangeIds) {
		// TODO Auto-generated method stub
//		List<Examinee> examinees = new ArrayList<Examinee>();
//		for (int examineeId : examineeIds) {
//			Examinee examinee = new Examinee();
//			examinee.setExamineeId(examineeId);
//			examinees.add(examinee);
//		}
//		List<ExamArrange> examArranges = new ArrayList<ExamArrange>();
//		for (int arrangeId : arrangeIds) {
//			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
//			examArrange.setExaminees(examinees);
//			examArranges.add(examArrange);
//		}
//		examArrangeDao.save(examArranges);
		for (int arrangeId : arrangeIds) {
			for(int examineeId : examineeIds){
				mapper.insertExamineeArrangeByExaminee(arrangeId, examineeId);
			}
		}
	}

	@Override
	public void delete(int[] arrangeIds) {
		for (int arrangeId : arrangeIds) {
//			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
//			examArrange.setExaminees(null);
//			examArrangeDao.save(examArrange);
			mapper.deleteExamineeArrangeByArrangeId(arrangeId);
		}				
	}
	
	

	
	
	

	
	
}
