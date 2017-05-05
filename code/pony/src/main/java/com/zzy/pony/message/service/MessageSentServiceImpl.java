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
import com.zzy.pony.message.model.MessageReceiver;
import com.zzy.pony.message.vo.MessageVo;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroDbRealm.ShiroUser;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;
import com.zzy.pony.util.DateTimeUtil;




@Service
@Transactional
public class MessageSentServiceImpl implements MessageSentService {

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
		Sort sort = new Sort(Sort.Direction.DESC, "msgId");
		List<Message> messages = messageDao.findByUserAndIsValid(currentUser,Constants.IS_VALID_Y,sort);		
		for (Message message : messages) {
			MessageVo vo = new MessageVo();
			vo.setMessageId(message.getMsgId()+"");
			//收件人
			List<MessageReceiver> messageReceivers =    message.getMessageReceivers();
			StringBuilder sb  = new StringBuilder();
			for (MessageReceiver messageReceiver : messageReceivers) {
				sb.append(messageReceiver.getName()+";");
			}
			vo.setReceiveUser(sb.toString());			
			vo.setContent(message.getContent());
			vo.setSendUser(message.getUser().getLoginName());
			vo.setSendTime(DateTimeUtil.dateToStr(message.getSendTime(), DateTimeUtil.FORMAL_LONG_FORMAT));
			vo.setTitle(message.getTitle());			
			String[] attach = new String[message.getMessageAttaches().size()];
			for (int i = 0; i < attach.length; i++) {
				attach[i] = message.getMessageAttaches().get(i).getOriginalName();
			}
			vo.setAttachs(attach);			
			result.add(vo);
		}
		return result;
	}

	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
