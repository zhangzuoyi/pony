package com.zzy.pony.service;



import java.util.List;











import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;











import com.zzy.pony.dao.ResourceDao;
import com.zzy.pony.model.Resource;


@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;
	
	@Override
	public List<Resource> findByResLevel(int resLevel) {
		// TODO Auto-generated method stub
		return resourceDao.findByResLevel(resLevel);
	}

	@Override
	public List<Resource> findByPresId(int presId) {
		// TODO Auto-generated method stub
		return resourceDao.findByPresId(presId);
	}

	@Override
	public void add(Resource resource) {
		// TODO Auto-generated method stub
		resourceDao.save(resource);
	}

	@Override
	public void delete(int resId) {
		// TODO Auto-generated method stub
		resourceDao.delete(resId);
	}

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub
		Resource old = resourceDao.findOne(resource.getResId());
		old.setComments(resource.getComments());
		old.setPresId(resource.getPresId());
		old.setResKey(resource.getResKey());
		old.setResLevel(resource.getResLevel());
		old.setResName(resource.getResName());
		resourceDao.save(old);
		
	}

	@Override
	public Resource get(int resId) {
		// TODO Auto-generated method stub
		return resourceDao.findOne(resId);
	}
	
	
	
	
	

	
	
	
	
	
		
		
		

	

}
