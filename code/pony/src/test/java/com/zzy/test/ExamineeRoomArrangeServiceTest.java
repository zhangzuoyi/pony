package com.zzy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.exam.service.ExamineeRoomArrangeService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class ExamineeRoomArrangeServiceTest {

	@Autowired
	private ExamineeRoomArrangeService examineeRoomArrangeService;
	
	@Test
	public void testAutoExamineeRoomArrange(){
			
		examineeRoomArrangeService.autoExamineeRoomArrange(2, 1);
	}
	
	
}
