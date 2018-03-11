package com.zzy.pony.service;

import java.util.Date;
import java.util.List;

import com.zzy.pony.model.ClassHourActual;
import com.zzy.pony.vo.ClassHourActualVo;
import com.zzy.pony.vo.ClassHourPlanVo;

/**
 * 课时服务
 * @author ZHANGZUOYI499
 *
 */
public interface ClassHourService {
	List<ClassHourPlanVo> findCurrentPlan();
	void save(List<ClassHourActual> list);
	/**
	 * 当前学期的业务日期列表
	 * @return
	 */
	List<String> businessDateList();
	List<ClassHourActualVo> findActual(Date businessDate);
}
