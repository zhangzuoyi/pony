package com.zzy.pony.ss.service;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.ss.mapper.SubjectSelectStatisticsMapper;
import com.zzy.pony.ss.vo.StudentSubjectSelectVo;

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
	public Map<String, Integer> group(int configId) {
		// TODO Auto-generated method stub
		List<StudentSubjectSelectVo> vos = subjectSelectStatisticsMapper.findAllByConfig(configId);
		//每个学生的选课结果 
		Map<Integer, Set<String>> studentMap = studentSelect(vos);
		for (Integer studentId : studentMap.keySet()) {
			Set<String> subjectSet = studentMap.get(studentId);
			//@todo 如何比较 
		}
		
		
		return null;
	}

	@Override
	public Map<Integer, Set<String>> studentSelect(List<StudentSubjectSelectVo> list) {
		// TODO Auto-generated method stub
		Map<Integer, Set<String>> result = new HashMap<Integer, Set<String>>();
		for (StudentSubjectSelectVo vo : list) {
			if (result.containsKey(vo.getStudentId())) {
				result.get(vo.getStudentId()).add(vo.getSubject());
			}else {
				Set<String> subjectSet = new HashSet<String>();
				subjectSet.add(vo.getSubject());
				result.put(vo.getStudentId(), subjectSet);
			}
		}		
		return result;
	}
	
	
	
	
	
	
	

}
