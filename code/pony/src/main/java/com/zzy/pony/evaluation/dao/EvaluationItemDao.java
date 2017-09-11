package com.zzy.pony.evaluation.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.evaluation.model.EvaluationItem;
import com.zzy.pony.evaluation.model.EvaluationSubject;



public interface EvaluationItemDao extends JpaRepository<EvaluationItem,Long>{
	@Query("select t from EvaluationItem t where t.subject=?1 order by t.level,t.seq")
	List<EvaluationItem> findBySubject(EvaluationSubject subject);
	@Query("select count(t) from EvaluationItem t where t.parentItemId=?1")
	int findCountByParentItemId(Long parentItemId);
}
