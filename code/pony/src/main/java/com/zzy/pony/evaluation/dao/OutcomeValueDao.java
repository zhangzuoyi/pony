package com.zzy.pony.evaluation.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.evaluation.model.OutcomeValue;



public interface OutcomeValueDao extends JpaRepository<OutcomeValue,Long>{
	@Query("select distinct category from OutcomeValue")
	List<String> findCategories();
	@Query("select distinct level1 from OutcomeValue where category=?1")
	List<String> findLevel1(String category);
	List<OutcomeValue> findByCategoryAndLevel1(String category, String level1);
	OutcomeValue findByCategoryAndLevel1AndLevel2(String category, String level1, String level2);
}
