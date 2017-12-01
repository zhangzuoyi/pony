package com.zzy.test.arrange;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.service.LessonPeriodAnalysisService;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.TeacherAnalysisVo;

/**
 * @author WANGCHAO262
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class LPAnalysis {
	
	@Autowired
	private LessonPeriodAnalysisService lessonPeriodAnalysisService;
	
	@Test
	public void analysis() {
		
		List<ArrangeVo> arrangeVos = lessonPeriodAnalysisService.findAllLessonArrange(5, 1, 3);
		List<TeacherAnalysisVo> vos = lessonPeriodAnalysisService.findAllTeacher(arrangeVos);
		lessonPeriodAnalysisService.analysisXW(vos);
		lessonPeriodAnalysisService.analysisSW(vos);
		lessonPeriodAnalysisService.analysisPQ(vos);
		for (TeacherAnalysisVo vo : vos) {
			if (!vo.isPQ()) {
				System.out.println(vo.getTeacherName()+":"+vo.getSubjectName()+"不平齐");
			}
			if ("语文".equalsIgnoreCase(vo.getSubjectName())||"数学".equalsIgnoreCase(vo.getSubjectName())||"英语".equalsIgnoreCase(vo.getSubjectName())) {
				if (vo.getPmRatio()>1) {
					System.out.println(vo.getTeacherName()+":"+vo.getSubjectName()+"下午课程过多"+vo.getPmRatio());
				}
			}else {
				if (vo.getPmRatio()>2) {
					System.out.println(vo.getTeacherName()+":"+vo.getSubjectName()+"下午课程过多"+vo.getPmRatio());
				}
			}
			System.out.println("上午"+vo.getTeacherName()+":"+vo.getSubjectName()+vo.getAmRatio());
			
		}
		
	}

}
