package com.zzy.pony.evaluation.dao;




import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.evaluation.model.EvaluationItemData;



public interface EvaluationItemDataDao extends JpaRepository<EvaluationItemData,Long>{
	@Modifying
	@Query("update EvaluationItemData set score=?2,according=?3 where id=?1")
	void updateScore(Long id,float score,String according);
	@Modifying
	@Query("update EvaluationItemData set checkScore=?2,checkTime=?3,checkUser=?4 where id=?1")
	void updateCheckScore(Long id,float checkScore,Date checkTime,String checkUser);
}
