package com.zzy.pony.evaluation.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.model.OutcomeAttach;



public interface OutcomeAttachDao extends JpaRepository<OutcomeAttach,Long>{
	List<OutcomeAttach> findByOutcome(Outcome outcome);
}
