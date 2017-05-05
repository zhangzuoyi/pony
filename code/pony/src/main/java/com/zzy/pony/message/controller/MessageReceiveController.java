package com.zzy.pony.message.controller;




import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.config.Constants;
import com.zzy.pony.message.dao.MessageAttachDao;
import com.zzy.pony.message.dao.MessageDao;
import com.zzy.pony.message.model.Message;
import com.zzy.pony.message.model.MessageAttach;
import com.zzy.pony.message.model.MessageReceiveInfo;
import com.zzy.pony.message.service.MessageReceiveService;
import com.zzy.pony.message.vo.MessageVo;




@Controller
@RequestMapping(value = "/message/messageReceive")
public class MessageReceiveController {
	static Logger log=LoggerFactory.getLogger(MessageReceiveController.class);
	
	@Autowired
	private MessageReceiveService messageReceiveService;
	@Autowired
	private MessageAttachDao messageAttachDao;
	@Autowired
	private MessageDao messageDao;
	
	
	 
	
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
	
	@RequestMapping(value="read",method = RequestMethod.GET)
	@ResponseBody
	public void read(@RequestParam(value="selectMessage[]") int[] selectMessage  ){
		if (selectMessage == null || selectMessage.length ==0) {
			return;
		}
		for (int id : selectMessage) {
			MessageReceiveInfo messageReceiveInfo = messageReceiveService.get(id);
			messageReceiveInfo.setReadStatus(Constants.RECEIVER_READ_Y);
			messageReceiveService.save(messageReceiveInfo);
		}
	}
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="selectMessage[]") int[] selectMessage  ){
		if (selectMessage == null || selectMessage.length ==0) {
			return;
		}
		for (int id : selectMessage) {
			MessageReceiveInfo messageReceiveInfo = messageReceiveService.get(id);
			messageReceiveInfo.setIsValid(Constants.IS_VALID_N);
			messageReceiveService.save(messageReceiveInfo);
		}
	}
	
	@RequestMapping(value = "downloadAttach", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadAttach(Model model,
			@RequestParam(value = "messageId") int messageId,
			@RequestParam(value = "attachName") String attachName) {
		
		Message message = messageDao.findOne(messageId);
		List<MessageAttach> messageAttachs = messageAttachDao.findByMessageAndOriginalName(message, attachName);
		MessageAttach messageAttach = messageAttachs.get(0);
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(
					messageAttach.getOriginalName().getBytes("utf-8"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<byte[]>(
				getContent(messageAttach.getFileName()), headers,
				HttpStatus.CREATED);
	}

	@SuppressWarnings("resource")
	private byte[] getContent(String url) {
		InputStream inStream = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		try {
			inStream = new FileInputStream(new File(
					Constants.MESSAGE_ATTACH_UPLOADPATH, url));

			while ((rc = inStream.read(buff, 0, 100)) > 0) {
				out.write(buff, 0, rc);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out.toByteArray();

	}
	
	
	
}
