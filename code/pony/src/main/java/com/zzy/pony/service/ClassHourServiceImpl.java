package com.zzy.pony.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ClassHourActualDao;
import com.zzy.pony.mapper.ClassHourMapper;
import com.zzy.pony.model.ClassHourActual;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.ClassHourActualVo;
import com.zzy.pony.vo.ClassHourPlanVo;
@Service
@Transactional
public class ClassHourServiceImpl implements ClassHourService {
	@Autowired
	private ClassHourMapper mapper;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private ClassHourActualDao actualDao;

	@Override
	public List<ClassHourPlanVo> findCurrentPlan() {
		SchoolYear currentYear=yearService.getCurrent();
		Term currentTerm=termService.getCurrent();
		
		return mapper.findByYearAndTerm(currentYear.getYearId(), currentTerm.getTermId());
	}

	@Override
	public void save(List<ClassHourActual> list) {
		actualDao.save(list);
	}

	@Override
	public List<String> businessDateList() {
		SchoolYear currentYear=yearService.getCurrent();
		Term currentTerm=termService.getCurrent();
		
		return mapper.businessDateList(currentYear.getYearId(), currentTerm.getTermId());
	}

	@Override
	public List<ClassHourActualVo> findActual(Date businessDate) {
		SchoolYear currentYear=yearService.getCurrent();
		Term currentTerm=termService.getCurrent();
		
		return mapper.findActual(currentYear.getYearId(), currentTerm.getTermId(), businessDate);
	}

}
