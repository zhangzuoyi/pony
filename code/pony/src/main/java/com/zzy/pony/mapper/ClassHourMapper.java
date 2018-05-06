package com.zzy.pony.mapper;

import java.util.Date;
import java.util.List;

import com.zzy.pony.vo.ClassHourActualVo;
import com.zzy.pony.vo.ClassHourPlanVo;

public interface ClassHourMapper {
	List<ClassHourPlanVo> findByYearAndTerm(Integer yearId, Integer termId);
	List<String> businessDateList(Integer yearId, Integer termId);
	List<ClassHourActualVo> findActual(Integer yearId, Integer termId, Date businessDate);
}
