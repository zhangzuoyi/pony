package com.zzy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.service.ComprehensiveRankService;
import com.zzy.pony.vo.ConditionVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class ComprehensiveRankServiceTest {

	@Autowired
	private ComprehensiveRankService comprehensiveRankService;
	
	
	@Test
	public void testRankExamResult(){
			
		ConditionVo cv = new ConditionVo();
		cv.setExamId(26);
		cv.setGradeId(1);
		comprehensiveRankService.rankExamResult(cv);
	}
	
	@Test
	public void testRankExaminee(){
			
		ConditionVo cv = new ConditionVo();
		cv.setExamId(26);
		cv.setGradeId(1);
		comprehensiveRankService.rankExaminee(cv);
	}
	
	
}
