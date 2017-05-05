package com.zzy.pony.message.dao;




import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.message.model.MessageReceiveInfo;
import com.zzy.pony.model.User;



public interface MessageReceiveInfoDao extends JpaRepository<MessageReceiveInfo,Integer>{
	List<MessageReceiveInfo> findByUserAndIsValid(User user,int isValid,Sort sort);
}
