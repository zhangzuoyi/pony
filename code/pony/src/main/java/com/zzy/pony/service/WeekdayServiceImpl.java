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
		

		@Override
		public List<Weekday> findByhaveClassOrderBySeq(Integer haveClass) {
			// TODO Auto-generated method stub
			return weekdayDao.findByhaveClassOrderBySeq(haveClass);
		}


		@Override
		public Weekday findByName(String name) {
			// TODO Auto-generated method stub
		 List<Weekday> weekdays =	weekdayDao.findByName(name);
		 return weekdays.get(0);
		}

		@Override
		public Weekday get(int id) {
			// TODO Auto-generated method stub
			Weekday weekday = weekdayDao.findOne(id);
			return weekday;
		}
		
		
		
		
		
		
		
		

	

}
