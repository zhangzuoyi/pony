package com.zzy.pony.exam.service;


import java.util.List;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import com.zzy.pony.exam.dao.ExamRoomDao;
import com.zzy.pony.exam.model.ExamRoom;



@Service
@Transactional
public class ExamRoomServiceImpl implements ExamRoomService {

	@Autowired
	private ExamRoomDao examRoomDao;
	
	@Override
	public List<ExamRoom> list() {
		// TODO Auto-generated method stub
		return examRoomDao.findAll();
	}

	
	
	
	
}
