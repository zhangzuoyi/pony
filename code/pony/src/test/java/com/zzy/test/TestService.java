/**
 * @author WANGCHAO262
 * @version
 */

package com.zzy.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.AutoLessonArrangeService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;



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
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private LessonArrangeService lessonArrangeService;
	@Autowired
	private GradeService gradeService;
	
	 
	
	@Test
	public void testAutoLessonArrange(){

		Grade grade = gradeService.findBySeq(2);
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<LessonArrange> autoList = lessonArrangeService.findBySchooleYearAndTermAndGradeIdAndSourceType(year, term,grade.getGradeId(), Constants.SOURCE_TYPE_AUTO);
		List<LessonArrange> changeList = lessonArrangeService.findBySchooleYearAndTermAndGradeIdAndSourceType(year, term,grade.getGradeId(), Constants.SOURCE_TYPE_CHANGE);
		autoList.addAll(changeList);
		lessonArrangeService.deleteList(autoList);

		autoLessonArrangeService.autoLessonArrange(grade.getGradeId());
		
	}
	
	
}
