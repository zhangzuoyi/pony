package com.zzy.pony.message.dao;




import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.message.model.Message;
import com.zzy.pony.model.User;



public interface MessageDao extends JpaRepository<Message,Integer>{
	List<Message> findByUserAndIsValid(User user,int isValid,Sort sort);
	
}
