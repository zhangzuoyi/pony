/**
 * @author WANGCHAO262
 * @version
 */

package com.zzy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.service.AutoLessonArrangeService;



/**
 * @author WANGCHAO262
 * @date 2017年1月11日  上午10:36:08
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestService {

	
	
	@Autowired
	private AutoLessonArrangeService autoLessonArrangeService;
	
	@Test
	public void testAutoLessonArrange(){
		autoLessonArrangeService.autoLessonArrange();
		
	}
	
}
