package com.zzy.pony.ss.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.ss.mapper.SubjectSelectStatisticsMapper;
import com.zzy.pony.ss.vo.StudentSubjectSelectVo;
import com.zzy.pony.ss.vo.StudentSubjectStatisticsVo;

@Service
@Transactional
public class SubjectSelectStatisticsServiceImpl implements SubjectSelectStatisticsService {

	@Autowired
	private SubjectSelectStatisticsMapper subjectSelectStatisticsMapper;

	@Override
	public int findTotalSelectByConfig(int configId) {
		// TODO Auto-generated method stub
		return subjectSelectStatisticsMapper.findTotalSelectByConfig(configId);
	}

	@Override
	public List<StudentSubjectStatisticsVo> group(int configId) {
		// TODO Auto-generated method stub
		List<StudentSubjectStatisticsVo> result = new ArrayList<StudentSubjectStatisticsVo>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<StudentSubjectSelectVo> vos = subjectSelectStatisticsMapper.findAllByConfig(configId);
		// 每个学生的选课结果
		Map<Integer, String> studentMap = studentSelect(vos);
		for (Integer studentId : studentMap.keySet()) {
			String subjects = studentMap.get(studentId);
			if (map.containsKey(subjects)) {
				map.put(subjects, map.get(subjects).intValue() + 1);
			} else {
				map.put(subjects, 1);
			}
		}
		for (String group : map.keySet()) {
			StudentSubjectStatisticsVo vo = new StudentSubjectStatisticsVo();
			vo.setGroup(group);
			vo.setCount(map.get(group));
			result.add(vo);
		}		
		Collections.sort(result);		
		return result;
	}

	@Override
	public Map<Integer, String> studentSelect(List<StudentSubjectSelectVo> list) {
		// TODO Auto-generated method stub
		Map<Integer, String> result = new HashMap<Integer, String>();
		Map<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();
		for (StudentSubjectSelectVo vo : list) {
			if (map.containsKey(vo.getStudentId())) {
				map.get(vo.getStudentId()).add(vo.getSubject());
			} else {
				Set<String> subjectSet = new TreeSet<String>();
				subjectSet.add(vo.getSubject());
				map.put(vo.getStudentId(), subjectSet);
			}
		}
		for (Integer studentId : map.keySet()) {
			Set<String> set = map.get(studentId);
			StringBuilder sb = new StringBuilder();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				sb.append(it.next() + ",");
			}
			result.put(studentId, sb.deleteCharAt(sb.length() - 1).toString());
		}

		return result;
	}

	
}
