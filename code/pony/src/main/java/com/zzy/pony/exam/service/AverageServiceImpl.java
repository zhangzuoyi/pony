package com.zzy.pony.exam.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.exam.dao.AverageIndexDao;
import com.zzy.pony.exam.mapper.AverageIndexMapper;
import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.vo.AverageIndexRowVo;
import com.zzy.pony.exam.vo.AverageIndexVo;
import com.zzy.pony.mapper.ExamResultMapper;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.vo.ExamResultVo;

@Service
@Transactional
public class AverageServiceImpl implements AverageService {
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private AverageIndexDao indexDao;
	@Autowired
	private AverageIndexMapper averageIndexMapper;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private ExamResultMapper examResultMapper;

	@Override
	public List<AverageIndexRowVo> findIndexRowVo(Integer examId, Integer gradeId) {
		List<Subject> subjects = subjectService.findByExam(examId);
		List<AverageIndex> indexList = indexDao.findByExamIdAndGradeId(examId, gradeId);
		List<AverageIndexRowVo> result = new ArrayList<AverageIndexRowVo>();
		for (int i = 1; i <= SECTION_COUNT; i++) {
			String section = "A" + i;
			AverageIndexRowVo vo = new AverageIndexRowVo();
			vo.setSection(section);
			vo.setExamId(examId);
			vo.setGradeId(gradeId);
			addIndex(vo, subjects, indexList);
			result.add(vo);
		}
		return result;
	}

	private void addIndex(AverageIndexRowVo vo, List<Subject> subjects, List<AverageIndex> indexList) {
		for (Subject subject : subjects) {
			AverageIndex ai = null;
			for (AverageIndex tmp : indexList) {
				if (tmp.getSection().equals(vo.getSection()) && tmp.getSubjectId() == subject.getSubjectId()) {
					ai = tmp;
					break;
				}
			}
			if (ai == null) {
				ai = new AverageIndex();
				ai.setSection(vo.getSection());
				ai.setSubjectId(subject.getSubjectId());
			}
			vo.getIndexList().add(ai);
		}
	}

	@Override
	public void saveIndexList(Integer examId, Integer gradeId, List<AverageIndex> indexList) {
		for (AverageIndex ai : indexList) {
			if (ai.getId() != 0) {
				AverageIndex old = indexDao.findOne(ai.getId());
				old.setIndexValue(ai.getIndexValue());
				indexDao.save(old);
			} else {
				ai.setExamId(examId);
				ai.setGradeId(gradeId);
				indexDao.save(ai);
			}

		}

	}

	@Override
	public void uploadIndexList(Integer examId, Integer gradeId, List<String> subjectList,
			List<List<Float>> valueList) {
		List<Subject> subjects = subjectService.findByExam(examId);
		List<AverageIndex> indexList = indexDao.findByExamIdAndGradeId(examId, gradeId);
		List<Integer> subjectIds = new ArrayList<Integer>();
		for (String name : subjectList) {
			Integer subjectId = 0;
			for (Subject subject : subjects) {
				if (name.equals(subject.getName())) {
					subjectId = subject.getSubjectId();
					break;
				}
			}
			subjectIds.add(subjectId);
		}
		int subjectLen = subjectIds.size();
		for (int j = 0; j < subjectLen; j++) {
			Integer subjectId = subjectIds.get(j);
			if (subjectId > 0) {
				for (int i = 0; i < valueList.size(); i++) {
					List<Float> values = valueList.get(i);
					String section = "A" + (i + 1);
					AverageIndex ai = new AverageIndex();
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

	private void saveUploadAverageIndex(List<AverageIndex> indexList, AverageIndex ai) {
		boolean isFind = false;
		for (AverageIndex old : indexList) {
			if (old.getSection().equals(ai.getSection()) && old.getSubjectId() == ai.getSubjectId()) {
				old.setIndexValue(ai.getIndexValue());
				indexDao.save(old);
				isFind = true;
				break;
			}
		}
		if (!isFind) {
			indexDao.save(ai);
		}
	}

	@Override
	public void calculateAverage(int examId, int gradeId) {
		// TODO Auto-generated method stub
		// 获取考试科目
		SchoolYear schoolYear = schoolYearService.getCurrent();
		List<Subject> subjects = subjectService.findByExam(examId);
		List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(),gradeId);
		for (Subject subject : subjects) {
			//key section(A1) value(key seq)
			Map<String, Map<String, Float>> map = new HashMap<String, Map<String,Float>>();
			List<Float> indexValues = new ArrayList<Float>();			
			List<AverageIndexVo> averageIndexVos = averageIndexMapper.findByExamAndGradeAndSubject(examId, gradeId,subject.getSubjectId());
			List<ExamResultVo> examResultVos = examResultMapper.findByExamAndGradeAndSubject(examId, gradeId, subject.getSubjectId());
			for (AverageIndexVo averageIndexVo : averageIndexVos) {
				indexValues.add(averageIndexVo.getIndexValue());				
			}
			int j=0;//段位控制
			for(int i=0;i<examResultVos.size();i++) {
				int classSeq = examResultVos.get(i).getClassSeq();
				float indexValue = indexValues.get(j);
				int indexValueFloor = (int) Math.floor(indexValue);
				int indexValueCeil  = (int) Math.ceil(indexValue);
				//6.82   在6后面出现相等  
				if (examResultVos.get(indexValueFloor).getGradeRank() != examResultVos.get(indexValueCeil).getGradeRank()) {
					//小于档位
					if (i<=indexValueFloor) {						
						if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
							//档数
							map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+1);								
						}else {
							map.get("A"+(j+1)).put("level"+classSeq, 1f);								
						}						
					}
					//等于档位
					if (i==indexValueCeil) {
						int count = 1;//相同排名数
						while(examResultVos.get(i).getGradeRank() == examResultVos.get(i+1).getGradeRank()) {
							i++;
							count++;
						}
						BigDecimal total = new BigDecimal(String.valueOf(indexValue-indexValueFloor));
						BigDecimal average =  total.divide(new BigDecimal(count));
						for (int m=indexValueCeil;m<i;m++) {
							//@todo 除不尽的默认加在第一个
							if (map.get("A"+(j+1)) != null && map.get("A"+(j+1)).get("level"+classSeq) != null) {
								//档数
								map.get("A"+(j+1)).put("level"+classSeq, map.get("A"+(j+1)).get("level"+classSeq)+average.floatValue());								
							}else {
								map.get("A"+(j+1)).put("level"+classSeq, average.floatValue());								
							}
						}
						j++;						
					}
				}
				//6.82   在6前后面出现相等  
				else {
						int gradeRank = examResultVos.get(indexValueFloor).getGradeRank();
					
					
				}
				
				
			}	
			
			
		}
	}

}
