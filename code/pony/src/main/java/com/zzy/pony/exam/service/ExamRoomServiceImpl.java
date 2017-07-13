package com.zzy.pony.exam.service;


import java.util.ArrayList;
import java.util.List;







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	public Page<ExamRoom> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return examRoomDao.findAll(pageable);
	}

	@Override
	public void add(ExamRoom examroom) {
		// TODO Auto-generated method stub
		examRoomDao.save(examroom);
	}

	@Override
	public void update(ExamRoom examroom) {
		// TODO Auto-generated method stub
		ExamRoom old = examRoomDao.findOne(examroom.getId());
		old.setName(examroom.getName());
		old.setCapacity(examroom.getCapacity());
		examRoomDao.save(old);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		examRoomDao.delete(id);
	}

	@Override
	public Boolean isExist(ExamRoom examroom) {
		// TODO Auto-generated method stub
		List<ExamRoom> examRooms = examRoomDao.findByName(examroom.getName());
		if (examRooms!= null && examRooms.size()>0) {
			return true;
		}
		
		return false;
	}

	@Override
	public List<ExamRoom> getExamRooms(int[] roomIds) {
		// TODO Auto-generated method stub
		List<ExamRoom> result = new ArrayList<ExamRoom>();
		for (int roomId : roomIds) {
			ExamRoom examRoom = examRoomDao.findOne(roomId);
			result.add(examRoom);
		}
		
		return result;
	}
	
	
	

	
	
	
	
}
