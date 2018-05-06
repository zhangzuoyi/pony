package com.zzy.pony.oa.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.model.WorkReportRemark;
import com.zzy.pony.oa.service.WorkReportAttatchService;
import com.zzy.pony.oa.service.WorkReportRemarkService;
import com.zzy.pony.security.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
@Controller
@RequestMapping(value = "/oa/workReportRemark")
public class WorkReportRemarkController {

    @Autowired
    private WorkReportRemarkService workReportRemarkService;
    @Autowired
    private WorkReportAttatchService workReportAttatchService;


    @RequestMapping(value = "main")
    public String main() {
        return "oa/workReportRemark/main";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    @ResponseBody
    public long add(@RequestParam(value = "reportId") long reportId, @RequestParam(value = "content") String content) {
        WorkReportRemark remark = new WorkReportRemark();
        remark.setContent(content);
        remark.setReportId(reportId);
        remark.setCreateTime(new Date());
        remark.setCreateUser(ShiroUtil.getLoginName());
        return workReportRemarkService.add(remark);
    }

    @RequestMapping(value = "addFile", method = RequestMethod.POST)
    @ResponseBody
    public void addFile(MultipartFile fileUpload, HttpServletRequest request, @RequestParam(value = "id") long id) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("fileUpload");
        workReportAttatchService.addFile(file, id, Constants.OA_TARGETTYPE_TWO);
    }


    @RequestMapping(value = "getRemark", method = RequestMethod.GET)
    @ResponseBody
    public List<WorkReportRemark> getRemark(@RequestParam(value = "id") long reportId) {
        return workReportRemarkService.findByReportId(reportId);
    }


}
