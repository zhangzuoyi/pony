package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.mapper.LessonArrangeMapper;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ConditionVo;
import com.zzy.pony.vo.TeacherAnalysisVo;

@Transactional
@Service
public class LessonPeriodAnalysisServiceImpl implements LessonPeriodAnalysisService {

	@Autowired
	private LessonArrangeMapper lessonArrangeMapper;

	@Override
	public List<ArrangeVo> findAllLessonArrange(int yearId, int termId, int gradeId) {
		// TODO Auto-generated method stub
		ConditionVo cv = new ConditionVo();
		cv.setYearId(yearId);
		cv.setTermId(termId);
		cv.setGradeId(gradeId);
		return lessonArrangeMapper.findByCondition(cv);
	}

	@Override
	public List<TeacherAnalysisVo> findAllTeacher(List<ArrangeVo> arrangeVos) {
		// TODO Auto-generated method stub
		List<TeacherAnalysisVo> result = new ArrayList<TeacherAnalysisVo>();
		Map<Integer, TeacherAnalysisVo> map = new HashMap<Integer, TeacherAnalysisVo>();
		Set<Integer> teacherIds = new HashSet<Integer>();
		for (ArrangeVo vo : arrangeVos) {
			if (!teacherIds.contains(vo.getTeacherId())) {
				TeacherAnalysisVo teacherAnalysisVo = new TeacherAnalysisVo(true);// 默认平齐
				teacherAnalysisVo.setTeacherId(vo.getTeacherId());
				teacherAnalysisVo.setTeacherName(vo.getTeacherName());
				teacherAnalysisVo.setSubjectId(vo.getSubjectId());
				teacherAnalysisVo.setSubjectName(vo.getSubjectName());
				Set<Integer> classIds = new HashSet<Integer>();
				classIds.add(vo.getClassId());
				teacherAnalysisVo.setClassIds(classIds);
				// 早上
				if (vo.getPeriodSeq() <= 5) {
					List<Integer> periodAm = new ArrayList<Integer>();
					periodAm.add(vo.getPeriodId());
					teacherAnalysisVo.setPeriodAm(periodAm);
				} else {
					List<Integer> periodPm = new ArrayList<Integer>();
					periodPm.add(vo.getPeriodId());
					teacherAnalysisVo.setPeriodPm(periodPm);
				}
				// weekPeriod
				Map<Integer, List<Integer>> weekPeriod = new HashMap<Integer, List<Integer>>();
				List<Integer> innerList = new ArrayList<Integer>();
				innerList.add(vo.getPeriodSeq());
				weekPeriod.put(vo.getWeekdayId(), innerList);
				teacherAnalysisVo.setWeekPeriod(weekPeriod);
				// weekClass
				Map<Integer, Set<Integer>> weekClass = new HashMap<Integer, Set<Integer>>();
				Set<Integer> innerSet = new HashSet<Integer>();
				innerSet.add(vo.getClassId());
				weekClass.put(vo.getWeekdayId(), innerSet);
				teacherAnalysisVo.setWeekClass(weekClass);

				map.put(vo.getTeacherId(), teacherAnalysisVo);
			} else {
				TeacherAnalysisVo teacherAnalysisVo = map.get(vo.getTeacherId());
				teacherAnalysisVo.getClassIds().add(vo.getClassId());
				if (vo.getPeriodSeq() <= 5) {
					if (teacherAnalysisVo.getPeriodAm() == null) {
						List<Integer> periodAm = new ArrayList<Integer>();
						periodAm.add(vo.getPeriodId());
						teacherAnalysisVo.setPeriodAm(periodAm);
					} else {
						teacherAnalysisVo.getPeriodAm().add(vo.getPeriodId());
					}
				} else {
					if (teacherAnalysisVo.getPeriodPm() == null) {
						List<Integer> periodPm = new ArrayList<Integer>();
						periodPm.add(vo.getPeriodId());
						teacherAnalysisVo.setPeriodPm(periodPm);
					} else {
						teacherAnalysisVo.getPeriodPm().add(vo.getPeriodId());
					}
				}
				if (teacherAnalysisVo.getWeekPeriod().containsKey(vo.getWeekdayId())) {
					teacherAnalysisVo.getWeekPeriod().get(vo.getWeekdayId()).add(vo.getPeriodSeq());
				} else {
					List<Integer> innerList = new ArrayList<Integer>();
					innerList.add(vo.getPeriodSeq());
					teacherAnalysisVo.getWeekPeriod().put(vo.getWeekdayId(), innerList);
				}
				if (teacherAnalysisVo.getWeekClass().containsKey(vo.getWeekdayId())) {
					teacherAnalysisVo.getWeekClass().get(vo.getWeekdayId()).add(vo.getClassId());
				} else {
					Set<Integer> innerSet = new HashSet<Integer>();
					innerSet.add(vo.getClassId());
					teacherAnalysisVo.getWeekClass().put(vo.getWeekdayId(), innerSet);
				}
			}
			teacherIds.add(vo.getTeacherId());
		}
		for (Integer teacherId : map.keySet()) {
			result.add(map.get(teacherId));
		}
		return result;
	}

	@Override
	public void analysisXW(List<TeacherAnalysisVo> teacherAnalysisVos) {
		// TODO Auto-generated method stub
		for (TeacherAnalysisVo vo : teacherAnalysisVos) {
			int classNum = vo.getClassIds().size();
			if (vo.getPeriodPm()!= null) {
				int pmNum = vo.getPeriodPm().size();
				vo.setPmRatio(pmNum / classNum);
			}else {
				vo.setPmRatio(0);
			}
			
		}
	}

	@Override
	public void analysisPQ(List<TeacherAnalysisVo> teacherAnalysisVos) {
		// TODO Auto-generated method stub
		// weekPeriod每天的上课数目应该与教的班级数目一致(或者为0) 且每天应该每个班级上一次
		for (TeacherAnalysisVo vo : teacherAnalysisVos) {
			if ("音乐".equalsIgnoreCase(vo.getSubjectName())||"体育".equalsIgnoreCase(vo.getSubjectName())) {
				continue;
			}
			boolean even = false;
			// 上偶数天
			if (vo.getWeekClass().size() % 2 == 0) {
				even = true;
			}
			Set<Integer> set = new HashSet<Integer>();
			for (Integer week : vo.getWeekClass().keySet()) {
				// 教的班级超过4
				if (vo.getClassIds().size() > 4) {
					set.add(vo.getWeekClass().get(week).size());				
				} else {
					if (vo.getWeekClass().get(week).size() != 0
							&& vo.getWeekClass().get(week).size() != vo.getClassIds().size()) {
						vo.setPQ(false);
						break;
					}
				}
			}		
			if (even && set.size()%2 != 0) {
				vo.setPQ(false);
			}

		}
	}

	@Override
	public void adjustPQ(List<TeacherAnalysisVo> teacherAnalysisVos) {
		// TODO Auto-generated method stub
		List<TeacherAnalysisVo> vos = new ArrayList<TeacherAnalysisVo>();
		for (TeacherAnalysisVo vo : teacherAnalysisVos) {
			if (!vo.isPQ()) {
				vos.add(vo);
			}
		}		
		for (TeacherAnalysisVo vo : vos) {
			
		}
		
		
		
	}

}
