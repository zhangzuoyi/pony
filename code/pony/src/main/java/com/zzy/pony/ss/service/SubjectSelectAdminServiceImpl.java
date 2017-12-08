package com.zzy.pony.ss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.model.Student;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.ss.dao.StudentSubjectSelectDao;
import com.zzy.pony.ss.mapper.SubjectSelectStatisticsMapper;
import com.zzy.pony.ss.model.StudentSubjectSelect;
import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.vo.StudentSubjectAdminVo;
import com.zzy.pony.ss.vo.StudentSubjectSelectVo;

@Service
@Transactional
public class SubjectSelectAdminServiceImpl implements SubjectSelectAdminService {

	@Autowired
	private SubjectSelectStatisticsMapper subjectSelectStatisticsMapper;
	@Autowired
	private SubjectSelectStatisticsService subjectSelectStatisticsService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SubjectSelectConfigService subjectSelectConfigService;
	@Autowired
	private StudentSubjectSelectDao studentSubjectSelectDao;

	@Override
	public List<StudentSubjectAdminVo> list(int configId, int studentId,String group) {
		// TODO Auto-generated method stub
		List<StudentSubjectAdminVo> result = new ArrayList<StudentSubjectAdminVo>();
		List<StudentSubjectSelectVo> list = subjectSelectStatisticsMapper.findAllByConfig(configId);
		Map<Integer, String> map = subjectSelectStatisticsService.studentSelect(list);				
		List<Student> students = studentService.findAll();
		Map<Integer, String> nameMap = new HashMap<Integer, String>();
		for (Student student : students) {
			nameMap.put(student.getStudentId(), student.getName());
		}
		for (Integer id : map.keySet()) {
			if (studentId > 0) {
				if ((StringUtils.isEmpty(group) && studentId == id) || (StringUtils.isNotEmpty(group) && studentId == id && group.equals(map.get(id)) )) {
					StudentSubjectAdminVo vo = new StudentSubjectAdminVo();
					vo.setStudentId(id);
					vo.setStudentName(nameMap.get(id));
					vo.setGroup(map.get(id));
					vo.setSelectSubjects(map.get(id).split(","));
					result.add(vo);
				}
			} else {
				if ( StringUtils.isEmpty(group) || (StringUtils.isNotEmpty(group) && group.equals(map.get(id)))) {
					StudentSubjectAdminVo vo = new StudentSubjectAdminVo();
					vo.setStudentId(id);
					vo.setStudentName(nameMap.get(id));
					vo.setGroup(map.get(id));
					vo.setSelectSubjects(map.get(id).split(","));
					result.add(vo);
				}			
			}
		}
		return result;
	}

	@Override
	public void update(List<String> subjects, int studentId, int configId) {
		// TODO Auto-generated method stub
		SubjectSelectConfig config = subjectSelectConfigService.get(configId);
		Student student = studentService.get(studentId);
		List<StudentSubjectSelect> list = studentSubjectSelectDao.findBySubjectSelectConfigAndStudent(config, student);
		// 老的选择不在新选择范围内的，需要删除
		for (StudentSubjectSelect ss : list) {
			boolean isFind = false;
			for (String subject : subjects) {
				if (ss.getSubject().equals(subject)) {
					isFind = true;
					break;
				}
			}
			if (!isFind) {
				studentSubjectSelectDao.delete(ss);
			}
		}
		// 新的选择不在老范围内的，需要新增
		for (String subject : subjects) {
			boolean isFind = false;
			for (StudentSubjectSelect ss : list) {
				if (subject.equals(ss.getSubject())) {
					isFind = true;
					break;
				}
			}
			if (!isFind) {
				StudentSubjectSelect ss = new StudentSubjectSelect();
				ss.setCreateTime(new Date());
				ss.setStudent(student);
				ss.setSubject(subject);
				ss.setSubjectSelectConfig(config);
				studentSubjectSelectDao.save(ss);
			}
		}

	}

}
