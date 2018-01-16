package com.zzy.pony.ss.service;

import com.zzy.pony.model.Student;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.ss.mapper.SubjectSelectResultMapper;
import com.zzy.pony.ss.mapper.SubjectSelectSingleMapper;
import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.vo.StudentSubjectResultVo;
import com.zzy.pony.ss.vo.StudentSubjectSingleVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SubjectSelectSingleServiceImpl implements SubjectSelectSingleService {

    @Autowired
    private SubjectSelectConfigService subjectSelectConfigService;
    @Autowired
    private SubjectSelectSingleMapper subjectSelectSingleMapper;

    @Override
    public List<StudentSubjectSingleVo> list() {
        SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
        return subjectSelectSingleMapper.list(config.getConfigId());
    }
}
