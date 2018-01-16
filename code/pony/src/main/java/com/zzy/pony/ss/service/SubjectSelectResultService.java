package com.zzy.pony.ss.service;

import com.zzy.pony.model.Student;
import com.zzy.pony.ss.vo.StudentSubjectResultVo;
import com.zzy.pony.ss.vo.StudentSubjectSelectVo;
import com.zzy.pony.ss.vo.StudentSubjectStatisticsVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;
import java.util.Map;

public interface SubjectSelectResultService {
    /*
     * Author: WANGCHAO262
     * Date  : 2018-01-16
     * Description: 已选
     */
    List<StudentSubjectResultVo> findBySelected(ConditionVo cv);

    /*
     * Author: WANGCHAO262
     * Date  : 2018-01-16
     * Description: 未选
     */
    List<StudentSubjectResultVo> findByUnselected(ConditionVo cv);


}
