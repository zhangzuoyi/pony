package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.dao.StudentDao;
import com.zzy.pony.mapper.StudentMapper;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Student;
import com.zzy.pony.vo.UpgradeVo;
@Service
public class UpgradeServiceImpl implements UpgradeService {
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private SchoolClassDao scDao;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private StudentMapper studentMapper;

	@Override
	public List<UpgradeVo> preview(Integer[] gradeIds) {
		List<Grade> allGrades=gradeService.findAll();
		List<Grade> grades=gradeService.findByGradeIdIn(gradeIds);
		SchoolYear currentYear=yearService.getCurrent();
		SchoolYear lastYear=yearService.findByStartYear(currentYear.getStartYear()-1);
		List<SchoolClass> scList=scDao.findByYearIdAndGradeIn(lastYear.getYearId(), grades);
		List<UpgradeVo> result=new ArrayList<UpgradeVo>();
		for(Grade grade: grades){
			result.add(getVo(grade, allGrades, scList));
		}
		return result;
	}
	
	private UpgradeVo getVo(Grade grade, List<Grade> allGrades, List<SchoolClass> scList){
		UpgradeVo vo=new UpgradeVo();
		vo.setSrcGrade(grade);
		for(Grade targetGrade: allGrades){
			if(targetGrade.getSeq() == grade.getSeq()+1){
				vo.setTargetGrade(targetGrade);
				break;
			}
		}
		List<SchoolClass> srcClasses=new ArrayList<SchoolClass>();
		for(SchoolClass sc: scList){
			if(sc.getGrade().getGradeId() == grade.getGradeId()){
				srcClasses.add(sc);
			}
		}
		vo.setSrcClasses(srcClasses);
		vo.setTargetClasses(srcClasses);//目标班级和源班级初始值是一样的
		
		return vo;
	}
	@Transactional
	@Override
	public void upgrade(UpgradeVo[] vos, String loginName) {
		SchoolYear currentYear=yearService.getCurrent();
		for(UpgradeVo vo: vos){
			if (vo.getTargetGrade() == null || vo.getTargetGrade().getGradeId() == null) {// 毕业
				for(SchoolClass old: vo.getSrcClasses()){
					// 不用将原班级学生的当前班级设为空
					/*List<Student> students = studentDao.findBySchoolClass(old);
					for (Student stu : students) {
						stu.setSchoolClass(null);
						//TODO 学生状态更改为已毕业

						studentDao.save(stu);
					}*/
					//TODO 增加StudentStatusChange记录
					studentMapper.updateStatusByClass(StudentService.STUDENT_STATUS_BY,StudentService.STUDENT_STATUS_ZD, old.getClassId());
				}
			} else {// 升级
				int len = vo.getTargetClasses().size();
				for (int i = 0; i < len; i++) {
					int seq = vo.getTargetClasses().get(i).getSeq();
					SchoolClass old = scDao.findOne(vo.getSrcClasses().get(i).getClassId());
					// 构造班级对象
					SchoolClass sc = new SchoolClass();
					sc.setCreateTime(new Date());
					sc.setCreateUser(loginName);
					sc.setGrade(vo.getTargetGrade());
					sc.setSeq(seq);
					sc.setTeacher(old.getTeacher());
					sc.setUpdateTime(new Date());
					sc.setUpdateUser(loginName);
					sc.setYearId(currentYear.getYearId());
					scDao.save(sc);

					// 将原班级学生的当前班级设为新班级
					/*List<Student> students = studentDao.findBySchoolClass(old);
					for (Student stu : students) {
						stu.setSchoolClass(sc);

						studentDao.save(stu);
					}*/
					//TODO 增加StudentStatusChange记录
					studentMapper.updateClassByClass(sc.getClassId(), old.getClassId());
				}
			}
			
		}
		
	}

}
