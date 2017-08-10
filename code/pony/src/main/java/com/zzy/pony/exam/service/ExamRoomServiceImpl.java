package com.zzy.pony.exam.service;


import java.util.ArrayList;
import java.util.List;


import com.zzy.pony.exam.dao.ExamRoomAllocateDao;
import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamRoomAllocate;
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
	@Autowired
	private ExamArrangeService examArrangeService;
	@Autowired
	private ExamRoomAllocateDao examRoomAllocateDao;
	
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

	@Override
	public void save(int[] subjectIds, int[] roomIds, int examId, int gradeId) {
		//增加删除逻辑
		
		
		for (int subjectId:
			 subjectIds) {
			ExamArrange examArrange = examArrangeService.findByExamAndGradeAndSubject(examId,gradeId,subjectId);
			List<ExamRoomAllocate> examRoomAllocates = examRoomAllocateDao.findByExamArrangeOrderByRoomSeq(examArrange);
			examRoomAllocateDao.delete(examRoomAllocates);
			int i = 1;
			for (int roomId:
				 roomIds) {
			 ExamRoom examRoom = examRoomDao.findOne(roomId);
			 ExamRoomAllocate examRoomAllocate = new ExamRoomAllocate();
			 examRoomAllocate.setCapacity(examRoom.getCapacity());
			 examRoomAllocate.setExamArrange(examArrange);
			 examRoomAllocate.setRoomSeq(i);
			 examRoomAllocate.setRoomName(examRoom.getName());
			 examRoomAllocateDao.save(examRoomAllocate);
			 i++;
			}
		}
	}

	@Override
	public List<ExamRoomAllocate> findByExamArrangeOrderByRoomSeq(ExamArrange examArrange) {
		// TODO Auto-generated method stub
		return examRoomAllocateDao.findByExamArrangeOrderByRoomSeq(examArrange);
	}

	@Override
	public ExamRoom get(int roomId) {
		// TODO Auto-generated method stub
		return examRoomDao.findOne(roomId);
	}
	
	

	
	
	
	
}
