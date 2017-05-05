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
@RequestMapping(value = "/message/messageSend")
public class MessageSendController {
	static Logger log=LoggerFactory.getLogger(MessageSendController.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private MessageSendService messageSendService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "message/messageSend/main";
	}
	@ResponseBody
	@RequestMapping(value="fileUpload",method = RequestMethod.POST)
	public void fileUpload(MultipartFile fileUpload,HttpServletRequest request,@RequestParam(value="messageId") int messageId
			){
		
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
		MultipartFile file = multipartRequest.getFile("fileUpload");
		//保存文件
		
		String childPath = DateTimeUtil.dateToStr(new Date());//子路径
		File childDir = null;
		File localFile = null;
		String fileName ="";
		if (file!=null && !file.isEmpty()) {
			try {				
				fileName = childPath+File.separator +file.getOriginalFilename();
				InputStream inputStream = file.getInputStream();
				childDir = new File(Constants.MESSAGE_ATTACH_UPLOADPATH, childPath);//根路径+子路径
				if (childDir.exists()) {
					//子文件夹存在
					localFile = new File(childDir,file.getOriginalFilename());
					FileOutputStream outputStream = new FileOutputStream(localFile);
					IOUtils.copy(inputStream, outputStream);
					inputStream.close();
					outputStream.close();		
				}else {
					childDir.mkdirs();
					localFile = new File(childDir,file.getOriginalFilename());
					FileOutputStream outputStream = new FileOutputStream(localFile);
					IOUtils.copy(inputStream, outputStream);
					inputStream.close();
					outputStream.close();
					
				}
						
			} catch (FileNotFoundException e) {
				// TODO: handle exception
			} catch (IOException e) {
				// TODO: handle exception
			}						
		}
		//数据存储
		Message message = messageSendService.get(messageId);
		List<MessageAttach> messageAttachs = new ArrayList<MessageAttach>();
		MessageAttach messageAttach = new MessageAttach();
		messageAttach.setMessage(message);
		messageAttach.setOriginalName(file.getOriginalFilename());
		messageAttach.setSavePath(childPath);
		messageAttach.setFileName(fileName);
		messageAttachs.add(messageAttach);
		message.setMessageAttaches(messageAttachs);		
		messageSendService.save(message);
	
	}
	//需要返回ID
	@ResponseBody
	@RequestMapping(value="send",method = RequestMethod.POST)
	public String send(@RequestBody MessageVo messageVo
			){
		Message message = new Message();
		ShiroUser shiroUser =   ShiroUtil.getLoginUser();
        User user = userService.findById(shiroUser.getId());  
		message.setContent(messageVo.getContent());
		message.setIsValid(Constants.IS_VALID_Y);
		message.setSendTime(new Date());
		message.setTitle(messageVo.getTitle());
		message.setUser(user);
		messageSendService.save(message);//先生成主表记录	
		List<MessageReceiver> messageReceivers = new ArrayList<MessageReceiver>();
		List<MessageReceiveInfo> messageReceiveInfos = new ArrayList<MessageReceiveInfo>();
		if ( messageVo.getUserGroup() != null &&  messageVo.getUserGroup().length>0) {
			for (int groupId : messageVo.getUserGroup()) {
				MessageReceiver messageReceiver = new MessageReceiver();
				Group group =  userGroupService.get(groupId);
				messageReceiver.setReceiverType(Constants.RECEIVER_TYPE_GROUP);
				messageReceiver.setReceiverId(groupId);
				messageReceiver.setMessage(message);
				messageReceiver.setName(group.getName());
				for (User receiveUser : group.getUsers()) {
					MessageReceiveInfo messageReceiveInfo = new MessageReceiveInfo();
					messageReceiveInfo.setIsValid(Constants.IS_VALID_Y);
					messageReceiveInfo.setUser(receiveUser);
					messageReceiveInfo.setMessage(message);
					messageReceiveInfos.add(messageReceiveInfo);
				}
				
				messageReceivers.add(messageReceiver);
			}
		}
		if ( messageVo.getUsers() != null &&  messageVo.getUsers().length>0) {
			for (int userId : messageVo.getUsers()) {
				
				MessageReceiver messageReceiver = new MessageReceiver();
				messageReceiver.setReceiverType(Constants.RECEIVER_TYPE_USER);
				messageReceiver.setReceiverId(userId);
				messageReceiver.setMessage(message);
				String userName = userService.findUserNameById(userId);
				messageReceiver.setName(userName);
				messageReceivers.add(messageReceiver);
				
				if (!userGroupService.isExist(userId, messageVo.getUserGroup())) {
					MessageReceiveInfo messageReceiveInfo = new MessageReceiveInfo();
					messageReceiveInfo.setIsValid(Constants.IS_VALID_Y);
					messageReceiveInfo.setUser(userService.findById(userId));
					messageReceiveInfo.setMessage(message);
					messageReceiveInfos.add(messageReceiveInfo);
				}
			
			}
		}
		
		message.setMessageReceiveInfos(messageReceiveInfos);
		message.setMessageReceivers(messageReceivers);
		messageSendService.save(message);	
		return message.getMsgId()+"";
	}
	
}
