package com.zzy.pony.evaluation.dao;




import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.evaluation.model.EvaluationRecord;
import com.zzy.pony.evaluation.model.EvaluationSubject;
import com.zzy.pony.model.Teacher;



public interface EvaluationRecordDao extends JpaRepository<EvaluationRecord,Long>{
	EvaluationRecord findBySubjectAndTeacherAndEvlTime(EvaluationSubject subject,Teacher teacher,String evlTime);
}
