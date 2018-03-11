package com.zzy.pony.oa.controller;

import com.zzy.pony.oa.model.*;
import com.zzy.pony.oa.service.WorkReportAttatchService;
import com.zzy.pony.oa.service.WorkReportService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
@Controller
@RequestMapping(value = "/oa/workReportAttach")
public class WorkReportAttatchController {

    @Autowired
    private WorkReportService workReportService;

    @Autowired
    private WorkReportAttatchService workReportAttatchService;
    @Value("${oaReportAttatch.baseUrl}")
    private String attatchPath;





    @RequestMapping(value = "getReportAttach", method = RequestMethod.GET)
    @ResponseBody
    public List<WorkReportAttach> getReportAttach(@RequestParam(value = "id") long id) {
        return workReportAttatchService.findByReportId(id);
    }

    @RequestMapping(value = "downloadAttach", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadAttach(@RequestParam(value = "id") long id, HttpServletRequest request, HttpServletResponse response) {

        WorkReportAttach ta = workReportAttatchService.findByAttachId(id);
        File localFile = new File(attatchPath, ta.getFileName());
        HttpHeaders headers = new HttpHeaders();
        byte[] content = new byte[0];

        try {
            headers.setContentDispositionFormData("attachment", new String(ta.getOriginalName().getBytes("utf-8"), "ISO8859-1"));
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


}
