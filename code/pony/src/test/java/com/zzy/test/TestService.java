/**
 * @author WANGCHAO262
 * @version
 */

package com.zzy.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.config.Constants;
import com.zzy.pony.mapper.LessonArrangeMapper;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.AutoLessonArrangeService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ConditionVo;



/**
 * @author WANGCHAO262
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
	@Autowired
	private LessonArrangeMapper lessonArrangeMapper;
	@Autowired
	private SchoolClassService schoolClassService;
	
	 
	
	@Test
	public void testAutoLessonArrange(){

		//Grade grade = gradeService.findBySeq(1);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub	
				Grade grade = gradeService.get(2);
				SchoolYear year = schoolYearService.getCurrent();
				Term term = termService.getCurrent();	
				List<LessonArrange> autoList = lessonArrangeService.findBySchooleYearAndTermAndGradeIdAndSourceType(year, term,grade.getGradeId(), Constants.SOURCE_TYPE_AUTO);
				List<LessonArrange> changeList = lessonArrangeService.findBySchooleYearAndTermAndGradeIdAndSourceType(year, term,grade.getGradeId(), Constants.SOURCE_TYPE_CHANGE);
				autoList.addAll(changeList);
				lessonArrangeService.deleteList(autoList);
				return autoLessonArrangeService.autoLessonArrange(grade.getGradeId());
			}
			
		});
		executor.execute(future);
		try {
				boolean result = future.get(30, TimeUnit.MINUTES);
				System.out.println(result);
			}catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				future.cancel(true);
				executor.shutdown();
			}
		
	}
	
	@Test
	public void testAutoLessonArrangeTwo(){
		Grade grade = gradeService.get(3);
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<SchoolClass> schoolClassList = schoolClassService.findByGrade(grade.getGradeId());
		String[] schoolClasses = new String[schoolClassList.size()];
		for (int i = 0; i < schoolClasses.length; i++) {
			schoolClasses[i] = schoolClassList.get(i).getClassId()+"";
		}
		
		ConditionVo cv= new ConditionVo();
		cv.setYearId(year.getYearId());
		cv.setTermId(term.getTermId());
		cv.setSchoolClasses(schoolClasses);
		cv.setSourceType(Constants.SOURCE_TYPE_AUTO);
		List<ArrangeVo> autoVos = lessonArrangeMapper.findByCondition(cv);
		cv.setSourceType(Constants.SOURCE_TYPE_CHANGE);
		List<ArrangeVo> changeVos = lessonArrangeMapper.findByCondition(cv);
		List<ArrangeVo> arrangeVos = new ArrayList<ArrangeVo>();
		arrangeVos.addAll(autoVos);
		arrangeVos.addAll(changeVos);
		if (arrangeVos != null && arrangeVos.size()>0) {
			lessonArrangeMapper.deleteByArrangeVo(arrangeVos);
		}

		autoLessonArrangeService.autoLessonArrangeTwo(grade.getGradeId());
		
	}
	
	
}
