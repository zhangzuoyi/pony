package com.zzy.pony.message.controller;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping(value = "/message/messageSend")
public class MessageSendController {
	static Logger log=LoggerFactory.getLogger(MessageSendController.class);
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "message/messageSend/main";
	}
	
}
