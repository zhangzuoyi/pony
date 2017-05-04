package com.zzy.pony.message.service;



import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.message.dao.MessageDao;
import com.zzy.pony.message.model.Message;
import com.zzy.pony.message.model.MessageAttach;
import com.zzy.pony.message.vo.MessageVo;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroDbRealm.ShiroUser;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;
import com.zzy.pony.util.DateTimeUtil;




@Service
@Transactional
public class MessageReceiveServiceImpl implements MessageReceiveService {

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private UserService userService;

	@Override
	public List<MessageVo> list() {
		// TODO Auto-generated method stub
		List<MessageVo> result = new ArrayList<MessageVo>();
		ShiroUser shiroUser =  ShiroUtil.getLoginUser();
		User currentUser = userService.findById(shiroUser.getId());
		List<Message> messages = messageDao.findByUser(currentUser);		
		for (Message message : messages) {
			MessageVo vo = new MessageVo();
			vo.setContent(message.getContent());
			vo.setSendUser(message.getUser().getLoginName());
			vo.setSendTime(DateTimeUtil.dateToStr(message.getSendTime(), DateTimeUtil.FORMAL_LONG_FORMAT));
			vo.setTitle(message.getTitle());
			String[] attach = new String[message.getMessageAttaches().size()];
			for (int i = 0; i < attach.length; i++) {
				attach[i] = message.getMessageAttaches().get(i).getOriginalName()+" ";
			}
			vo.setAttach(attach);
			
			result.add(vo);
		}
		return result;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
