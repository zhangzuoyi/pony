package com.zzy.pony.exam.service;






import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.exam.dao.ExamArrangeDao;
import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.Examinee;






@Service
@Transactional
public class ExamineeArrangeServiceImpl implements ExamineeArrangeService {

	@Autowired
	private ExamineeService examineeService;
	@Autowired
	private ExamArrangeDao examArrangeDao;
	
	
	
	@Override
	public void submitByClass(int examId, int[] classIds, int[] arrangeIds) {
		// TODO Auto-generated method stub
		List<Examinee> examinees = new ArrayList<Examinee>();
		/*for (int classId : classIds) {
			examinees.addAll(examineeService.findByExamIdAndClassId(examId, classId));
		}*/
		examinees = examineeService.findByExamIdAndClassIds(examId, classIds);
		List<ExamArrange> examArranges = new ArrayList<ExamArrange>();
		for (int arrangeId : arrangeIds) {
			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
			examArrange.setExaminees(examinees);
			examArranges.add(examArrange);
		}
		examArrangeDao.save(examArranges);
		
	}

	@Override
	public void submitByStudent(int[] examineeIds, int[] arrangeIds) {
		// TODO Auto-generated method stub
		List<Examinee> examinees = new ArrayList<Examinee>();
		for (int examineeId : examineeIds) {
			Examinee examinee = new Examinee();
			examinee.setExamineeId(examineeId);
			examinees.add(examinee);
		}
		List<ExamArrange> examArranges = new ArrayList<ExamArrange>();
		for (int arrangeId : arrangeIds) {
			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
			examArrange.setExaminees(examinees);
			examArranges.add(examArrange);
		}
		examArrangeDao.save(examArranges);
	}

	@Override
	public void delete(int[] arrangeIds) {
		// TODO Auto-generated method stub			
		for (int arrangeId : arrangeIds) {
			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
			examArrange.setExaminees(null);
			examArrangeDao.save(examArrange);
		}				
	}
	
	

	
	
	

	
	
}
