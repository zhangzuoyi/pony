package com.zzy.pony.service;



import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.WeekdayDao;
import com.zzy.pony.model.Weekday;

@Service
@Transactional
public class WeekdayServiceImpl implements WeekdayService {
	
		@Autowired
		private WeekdayDao weekdayDao;

		@Override
		public List<Weekday> findByhaveClass(Integer haveClass) {
			// TODO Auto-generated method stub
			return weekdayDao.findByhaveClass(haveClass);
		}
		
		
		
		

	

}
