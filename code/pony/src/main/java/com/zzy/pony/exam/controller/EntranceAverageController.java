package com.zzy.pony.exam.controller;

import com.zzy.pony.exam.service.AverageService;
import com.zzy.pony.util.TemplateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.OldExcelFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/6 0006.
 */
@Controller
@RequestMapping(value = "/examAdmin/entranceAverage")
public class EntranceAverageController {

    @Autowired
    private AverageService service;
    @Value("${entranceAveragePath}")
    private String entranceAveragePath;


    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        return "examAdmin/entranceAverage/main";
    }


    @RequestMapping(value = "exportTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportAssignTemplate(Model model) {
        String fileName = "入学成绩模板.xlsx";
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(TemplateUtil.getContent(fileName), headers, HttpStatus.CREATED);
    }

    @SuppressWarnings("deprecation")
    @RequestMapping(value = "exportAverageFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportNewAverageFile(@RequestParam(value="name") String name, HttpServletRequest request, HttpServletResponse response) {
        File localFile = new File(entranceAveragePath, name);
        String fileName = "入学均量值.xls";
        HttpHeaders headers = new HttpHeaders();
        byte[] content = new byte[0];
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            content = IOUtils.toByteArray(new FileInputStream(localFile));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);

    }

    @RequestMapping(value = "exportAverage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> exportNewAverage(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        Map<String, String> result = new HashMap<String, String>();
        try {

        } catch (OldExcelFormatException e) {
            e.printStackTrace();
            result.put("error", "版本必须为2003及以上");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
