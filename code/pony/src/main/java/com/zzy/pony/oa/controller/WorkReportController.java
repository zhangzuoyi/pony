package com.zzy.pony.oa.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.model.*;
import com.zzy.pony.oa.service.*;
import com.zzy.pony.oa.vo.WorkReportVo;
import com.zzy.pony.vo.ConditionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
@Controller
@RequestMapping(value="/oa/workReport")
public class WorkReportController {

    @Autowired
    private WorkReportService workReportService;

    @Autowired
    private WorkReportLogService workReportLogService;

    @Autowired
    private WorkReportAttatchService workReportAttatchService;


    @RequestMapping(value = "main")
    public String main(){
        return "oa/workReport/main";
    }



    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Page<WorkReportVo> list(@RequestBody ConditionVo cv ){
        cv.setStartNum(cv.getCurrentPage()*cv.getPageSize());
        return workReportService.list(cv);
    }

    @RequestMapping(value = "listMy",method = RequestMethod.POST)
    @ResponseBody
    public Page<WorkReportVo> listMy(@RequestBody ConditionVo cv ){
        cv.setStartNum(cv.getCurrentPage()*cv.getPageSize());
        return workReportService.listMy(cv);
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public long add(@RequestBody WorkReportVo vo){
        WorkReport wr = new WorkReport();
        wr.setName(vo.getName());
        wr.setAccess(vo.getAccess());
        wr.setPeriod(vo.getPeriod());
        wr.setReporter(StringUtils.join(vo.getReporter(),','));
        wr.setStartDate(vo.getStartDate() );
        wr.setEndDate(vo.getEndDate());
        wr.setContent(vo.getContent());
        wr.setAuditor(StringUtils.join(vo.getAuditor(),','));

        return  workReportService.add(wr);
    }

    @RequestMapping(value = "addFile", method = RequestMethod.POST)
    @ResponseBody
    public void addFile(MultipartFile fileUpload, HttpServletRequest request, @RequestParam(value = "id") long id) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("fileUpload");
        workReportAttatchService.addFile(file, id, Constants.OA_TARGETTYPE_ONE);
    }





    @RequestMapping(value = "finish",method = RequestMethod.GET)
    @ResponseBody
    public void finish(@RequestParam(value = "id") long id){

        WorkReport wr = workReportService.get(id);
        wr.setStatus(Constants.OA_STATUS_FINISH);
        workReportService.update(wr);

        WorkReportLog log = new WorkReportLog();
        log.setTypeCode(Constants.OA_STATUS_FINISH);
        log.setTypeName("完成");
        log.setReportId(wr.getId());
        workReportLogService.add(log);

    }

    @RequestMapping(value = "delete",method = RequestMethod.GET)
    @ResponseBody
    public void add(@RequestParam(value = "id") long id){

        workReportService.delete(id);
        WorkReportLog log = new WorkReportLog();
        log.setTypeCode(Constants.OA_STATUS_DELETE);
        log.setTypeName("删除");
        log.setReportId(id);
        workReportLogService.add(log);

    }










}
