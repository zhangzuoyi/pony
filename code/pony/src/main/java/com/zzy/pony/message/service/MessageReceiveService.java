package com.zzy.pony.message.service;

import java.util.List;

import com.zzy.pony.message.model.MessageReceiveInfo;
import com.zzy.pony.message.vo.MessageVo;









public interface MessageReceiveService {
	List<MessageVo> list();
	MessageReceiveInfo get(int id);
	void save(MessageReceiveInfo messageReceiveInfo);
}
