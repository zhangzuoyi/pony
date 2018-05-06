package com.zzy.pony.oa.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.model.TaskAttach;
import com.zzy.pony.oa.model.TaskLog;
import com.zzy.pony.oa.model.TaskProgress;
import com.zzy.pony.oa.service.TaskLogService;
import com.zzy.pony.oa.service.TaskService;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.ConditionVo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
@Controller
@RequestMapping(value="/oa/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Value("${oaTaskAttatch.baseUrl}")
    private String attatchPath;

    @Autowired
    private TaskLogService taskLogService;


    @RequestMapping(value = "main")
    public String main(){
        return "oa/task/main";
    }



    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Page<TaskVo> list(@RequestBody ConditionVo cv ){
        cv.setStartNum(cv.getCurrentPage()*cv.getPageSize());
        return taskService.list(cv);
    }

    @RequestMapping(value = "listMy",method = RequestMethod.POST)
    @ResponseBody
    public Page<TaskVo> listMy(@RequestBody ConditionVo cv ){
        cv.setStartNum(cv.getCurrentPage()*cv.getPageSize());
        return taskService.listMy(cv);
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public long add(@RequestBody TaskVo taskVo){
        Task task = new Task();
        task.setName(taskVo.getName());
        task.setAccess(taskVo.getAccess());
        task.setAssignee(StringUtils.join(taskVo.getAssignee(),','));
        task.setCc(StringUtils.join(taskVo.getCc(),','));
        task.setDescription(taskVo.getDescription());
        task.setStartTime(taskVo.getStartTime() );
        task.setEndTime(taskVo.getEndTime());
        task.setMembers(StringUtils.join(taskVo.getMembers(),','));
        task.setTags(taskVo.getTags());
        return  taskService.add(task);
    }

    @RequestMapping(value = "addFile",method = RequestMethod.POST)
    @ResponseBody
    public void addFile(MultipartFile fileUpload, HttpServletRequest request,@RequestParam(value = "id") long id){
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("fileUpload");
        taskService.addFile(file,id, Constants.OA_TARGETTYPE_ONE);
    }

    @RequestMapping(value = "pending",method = RequestMethod.GET)
    @ResponseBody
    public void pending(@RequestParam(value = "id") long id){

        Task task = taskService.get(id);
        task.setStatus(Constants.OA_STATUS_PENDING);
        taskService.update(task);

        TaskLog tl = new TaskLog();
        tl.setTypeCode(Constants.OA_STATUS_PENDING);
        tl.setTypeName("暂停");
        tl.setTaskId(task.getId());
        taskLogService.add(tl);
    }

    @RequestMapping(value = "finish",method = RequestMethod.GET)
    @ResponseBody
    public void finish(@RequestParam(value = "id") long id){

        Task task = taskService.get(id);
        task.setStatus(Constants.OA_STATUS_FINISH);
        taskService.update(task);

        TaskLog tl = new TaskLog();
        tl.setTypeCode(Constants.OA_STATUS_FINISH);
        tl.setTypeName("完成");
        tl.setTaskId(task.getId());
        taskLogService.add(tl);

    }

    @RequestMapping(value = "delete",method = RequestMethod.GET)
    @ResponseBody
    public void add(@RequestParam(value = "id") long id){

        taskService.delete(id);
        TaskLog tl = new TaskLog();
        tl.setTypeCode(Constants.OA_STATUS_DELETE);
        tl.setTypeName("删除");
        tl.setTaskId(id);
        taskLogService.add(tl);

    }

    @RequestMapping(value = "getTaskAttach",method = RequestMethod.GET)
    @ResponseBody
    public List<TaskAttach> getTaskAttach(@RequestParam(value = "id") long id){
        return taskService.findByTaskId(id);
    }

    @RequestMapping(value = "downloadAttach", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadAttach(@RequestParam(value = "id") long  id, HttpServletRequest request, HttpServletResponse response) {

        TaskAttach ta = taskService.findByAttachId(id);
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
