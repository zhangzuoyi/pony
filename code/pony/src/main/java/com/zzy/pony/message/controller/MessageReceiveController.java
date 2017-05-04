package com.zzy.pony.message.controller;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zzy.pony.config.Constants;
import com.zzy.pony.message.model.Message;
import com.zzy.pony.message.model.MessageAttach;
import com.zzy.pony.message.model.MessageReceiveInfo;
import com.zzy.pony.message.model.MessageReceiver;
import com.zzy.pony.message.service.MessageReceiveService;
import com.zzy.pony.message.service.MessageSendService;
import com.zzy.pony.message.vo.MessageVo;
import com.zzy.pony.model.Group;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroDbRealm.ShiroUser;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserGroupService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.util.DateTimeUtil;



@Controller
@RequestMapping(value = "/message/messageReceive")
public class MessageReceiveController {
	static Logger log=LoggerFactory.getLogger(MessageReceiveController.class);
	
	@Autowired
	private MessageReceiveService messageReceiveService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "message/messageReceive/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<MessageVo> list(){
		List<MessageVo> result = messageReceiveService.list();
		return result;
	}
	
	
}
