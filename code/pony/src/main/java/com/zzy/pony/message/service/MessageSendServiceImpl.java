package com.zzy.pony.message.service;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.message.dao.MessageDao;
import com.zzy.pony.message.model.Message;




@Service
@Transactional
public class MessageSendServiceImpl implements MessageSendService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public void save(Message message) {
		// TODO Auto-generated method stub
		messageDao.save(message);
	}

	@Override
	public Message get(int messageId) {
		// TODO Auto-generated method stub
		return messageDao.findOne(messageId);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
