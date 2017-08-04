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


import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.service.SchoolYearService;



/**
 * @author WANGCHAO262
 * @date 2017年1月11日  上午10:36:08
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml","/shiroFilter.xml"})
public class SchoolYearServiceTest {
	
	@Autowired
	private SchoolYearService schoolYearService;
	 
	
	@Test
	public void testGetCurrent(){

		SchoolYear sy=schoolYearService.getCurrent();
		System.out.println(sy.getName());
		sy=schoolYearService.getCurrent();
		System.out.println(sy.getName());
	}
	
	
}
