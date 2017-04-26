package com.zzy.pony.message.dao;




import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.message.model.Message;



public interface MessageDao extends JpaRepository<Message,Integer>{
	
}
