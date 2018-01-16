package com.zzy.pony.ss.service;

import com.zzy.pony.model.Student;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.ss.mapper.SubjectSelectResultMapper;
import com.zzy.pony.ss.mapper.SubjectSelectStatisticsMapper;
import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.vo.StudentSubjectResultVo;
import com.zzy.pony.ss.vo.StudentSubjectSelectVo;
import com.zzy.pony.ss.vo.StudentSubjectStatisticsVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class SubjectSelectResultServiceImpl implements SubjectSelectResultService {

    @Autowired
    private SubjectSelectConfigService subjectSelectConfigService;
    @Autowired
    private SubjectSelectResultMapper subjectSelectResultMapper;
    @Autowired
    private StudentService studentService;

    @Override
    public List<StudentSubjectResultVo> findBySelected(ConditionVo cv) {
        SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
        cv.setConfigId(config.getConfigId());
        return subjectSelectResultMapper.findBySelected(cv);
    }

    @Override
    public List<StudentSubjectResultVo> findByUnselected(ConditionVo cv) {
        List<StudentSubjectResultVo> result = new ArrayList<StudentSubjectResultVo>();
        List<Student> allStudent = new ArrayList<Student>();
        SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
        Integer[] grades = config.getGradeIds();
        for (Integer gradeId : grades) {
            List<Student> students = studentService.findByGrade(gradeId);
            allStudent.addAll(students);
        }
        List<StudentSubjectResultVo> selectedList = findBySelected(cv);
        List<Integer> selected = new ArrayList<Integer>();
        for (StudentSubjectResultVo vo :
                selectedList) {
            selected.add(vo.getStudentId());
        }
        List<Student> removeStudents = new ArrayList<Student>();
        for (Student student:
                allStudent) {
            if (selected.contains(student.getStudentId())){
                removeStudents.add(student);
            }
        }

        allStudent.removeAll(removeStudents);

        for (Student student :
                allStudent) {
            StudentSubjectResultVo vo = new StudentSubjectResultVo();
            vo.setClassId(student.getSchoolClass().getClassId());
            vo.setClassName(student.getSchoolClass().getName());
            vo.setStudentId(student.getStudentId());
            vo.setStudentName(student.getName());
            result.add(vo);
        }

        return result;
    }
}
