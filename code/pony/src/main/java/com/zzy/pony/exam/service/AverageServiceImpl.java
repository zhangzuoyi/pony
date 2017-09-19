package com.zzy.pony.exam.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.exam.dao.AverageIndexDao;
import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.AverageIndexRowVo;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.SubjectService;
@Service
@Transactional
public class AverageServiceImpl implements AverageService {
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private AverageIndexDao indexDao;

	@Override
	public List<AverageIndexRowVo> findIndexRowVo(Integer examId, Integer gradeId) {
		List<Subject> subjects=subjectService.findByExam(examId);
		List<AverageIndex> indexList=indexDao.findByExamIdAndGradeId(examId, gradeId);
		List<AverageIndexRowVo> result=new ArrayList<AverageIndexRowVo>();
		for(int i=1;i<=SECTION_COUNT;i++) {
			String section="A"+i;
			AverageIndexRowVo vo=new AverageIndexRowVo();
			vo.setSection(section);
			vo.setExamId(examId);
			vo.setGradeId(gradeId);
			addIndex(vo,subjects,indexList);
			result.add(vo);
		}
		return result;
	}
	private void addIndex(AverageIndexRowVo vo,List<Subject> subjects,List<AverageIndex> indexList) {
		for(Subject subject: subjects) {
			AverageIndex ai=null;
			for(AverageIndex tmp: indexList) {
				if(tmp.getSection().equals(vo.getSection()) && tmp.getSubjectId() == subject.getSubjectId()) {
					ai=tmp;
					break;
				}
			}
			if(ai == null) {
				ai=new AverageIndex();
				ai.setSection(vo.getSection());
				ai.setSubjectId(subject.getSubjectId());
			}
			vo.getIndexList().add(ai);
		}
	}
	@Override
	public void saveIndexList(Integer examId, Integer gradeId, List<AverageIndex> indexList) {
		for(AverageIndex ai : indexList) {
			if(ai.getId() != 0) {
				AverageIndex old=indexDao.findOne(ai.getId());
				old.setIndexValue(ai.getIndexValue());
				indexDao.save(old);
			}else {
				ai.setExamId(examId);
				ai.setGradeId(gradeId);
				indexDao.save(ai);
			}
			
		}
		
	}
	@Override
	public void uploadIndexList(Integer examId, Integer gradeId, List<String> subjectList,
			List<List<Float>> valueList) {
		List<Subject> subjects=subjectService.findByExam(examId);
		List<AverageIndex> indexList=indexDao.findByExamIdAndGradeId(examId, gradeId);
		List<Integer> subjectIds=new ArrayList<Integer>();
		for(String name : subjectList) {
			Integer subjectId=0;
			for(Subject subject : subjects) {
				if(name.equals(subject.getName())) {
					subjectId=subject.getSubjectId();
					break;
				}
			}
			subjectIds.add(subjectId);
		}
		int subjectLen=subjectIds.size();
		for(int j=0;j<subjectLen;j++) {
			Integer subjectId=subjectIds.get(j);
			if(subjectId>0) {
				for(int i=0;i<valueList.size();i++) {
					List<Float> values=valueList.get(i);
					String section="A"+(i+1);
					AverageIndex ai=new AverageIndex();
					ai.setExamId(examId);
					ai.setGradeId(gradeId);
					ai.setIndexValue(values.get(j));
					ai.setSection(section);
					ai.setSubjectId(subjectId);
					
					saveUploadAverageIndex(indexList, ai);
				}
			}
		}
		
	}
	private void saveUploadAverageIndex(List<AverageIndex> indexList,AverageIndex ai) {
		boolean isFind=false;
		for(AverageIndex old : indexList) {
			if(old.getSection().equals(ai.getSection()) && old.getSubjectId() == ai.getSubjectId()) {
				old.setIndexValue(ai.getIndexValue());
				indexDao.save(old);
				isFind=true;
				break;
			}
		}
		if( ! isFind) {
			indexDao.save(ai);
		}
	}
}
