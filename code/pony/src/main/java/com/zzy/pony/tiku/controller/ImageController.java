package com.zzy.pony.tiku.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-19
 * @Description
 */
@Controller
public class ImageController {

    @Value("${tikuImage.baseUrl}")
    private  String baseUrl;


    @RequestMapping(value = "/getimg/**")
    public void findImg(HttpServletRequest request, HttpServletResponse response){
        //getimg/p343/93825457-0c9d-47ad-823c-c1a62137e21b.png
        try {
            String url = request.getRequestURI();
            String[] urlStr = url.split("/");
            InputStream imgStream = new FileInputStream(new File(baseUrl,urlStr[2]+'\\'+urlStr[3]));
            response.setContentType("image/png");
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(imgStream,outputStream);
            imgStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }




}
