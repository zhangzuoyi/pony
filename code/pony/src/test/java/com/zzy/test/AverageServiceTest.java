package com.zzy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.exam.service.AverageService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class AverageServiceTest {

	@Autowired
	private AverageService averageService;

	
	@Test
	public void testRankExamResult(){			
		averageService.calculateAverage(26, 1);
	}
	
	
	
	
}
