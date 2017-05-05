package com.zzy.pony.message.service;



import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.message.dao.MessageDao;
import com.zzy.pony.message.dao.MessageReceiveInfoDao;
import com.zzy.pony.message.model.Message;
import com.zzy.pony.message.model.MessageAttach;
import com.zzy.pony.message.model.MessageReceiveInfo;
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
	private MessageReceiveInfoDao messageReceiveInfoDao;
	@Autowired
	private UserService userService;

	@Override
	public List<MessageVo> list() {
		// TODO Auto-generated method stub
		List<MessageVo> result = new ArrayList<MessageVo>();
		ShiroUser shiroUser =  ShiroUtil.getLoginUser();
		User currentUser = userService.findById(shiroUser.getId());
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		List<MessageReceiveInfo> messageReceiveInfos = messageReceiveInfoDao.findByUserAndIsValid(currentUser,Constants.IS_VALID_Y,sort);		
		for (MessageReceiveInfo messageReceiveInfo : messageReceiveInfos) {
			MessageVo vo = new MessageVo();
			vo.setMessageId(messageReceiveInfo.getMessage().getMsgId()+"");
			vo.setMessageReceiveInfoId(messageReceiveInfo.getId()+"");
			vo.setContent(messageReceiveInfo.getMessage().getContent());
			vo.setSendUser(messageReceiveInfo.getUser().getLoginName());
			vo.setSendTime(DateTimeUtil.dateToStr(messageReceiveInfo.getMessage().getSendTime(), DateTimeUtil.FORMAL_LONG_FORMAT));
			vo.setTitle(messageReceiveInfo.getMessage().getTitle());
			vo.setReadStatus(messageReceiveInfo.getReadStatus()+"");
			String[] attach = new String[messageReceiveInfo.getMessage().getMessageAttaches().size()];
			for (int i = 0; i < attach.length; i++) {
				attach[i] = messageReceiveInfo.getMessage().getMessageAttaches().get(i).getOriginalName();
			}
			vo.setAttachs(attach);			
			result.add(vo);
		}
		return result;
	}

	@Override
	public MessageReceiveInfo get(int id) {
		// TODO Auto-generated method stub
		return messageReceiveInfoDao.findOne(id);
	}

	@Override
	public void save(MessageReceiveInfo messageReceiveInfo) {
		// TODO Auto-generated method stub
		messageReceiveInfoDao.save(messageReceiveInfo);
		
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
