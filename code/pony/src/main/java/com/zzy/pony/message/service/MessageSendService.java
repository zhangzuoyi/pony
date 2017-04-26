package com.zzy.pony.message.service;

import com.zzy.pony.message.model.Message;









public interface MessageSendService {
	void save(Message message);
	Message get(int messageId);
}
