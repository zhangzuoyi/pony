package com.zzy.pony.message.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.message.model.Message;
import com.zzy.pony.message.model.MessageAttach;



public interface MessageAttachDao extends JpaRepository<MessageAttach,Integer>{
	List<MessageAttach> findByMessageAndOriginalName(Message message , String originalName);
}
