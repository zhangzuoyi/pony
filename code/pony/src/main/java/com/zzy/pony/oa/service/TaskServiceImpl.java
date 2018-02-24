package com.zzy.pony.oa.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.mapper.TaskMapper;
import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.model.TaskAttach;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.ConditionVo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskMapper taskMapper;
    @Value("${oaTaskAttatch.baseUrl}")
    private String attatchPath;

    @Override
    public Page<TaskVo> list(ConditionVo cv) {
        List<TaskVo> list=taskMapper.findPage(cv);
        int count=taskMapper.findCount(cv);
        Pageable pageable = new PageRequest(cv.getCurrentPage(), cv.getPageSize());
        Page<TaskVo> result = new PageImpl<TaskVo>(list, pageable, count);
        return result;
    }

    @Override
    public Page<TaskVo> listMy(ConditionVo cv) {
        cv.setLoginName(ShiroUtil.getLoginName());
        List<TaskVo> list=taskMapper.findMyPage(cv);
        int count=taskMapper.findMyCount(cv);
        Pageable pageable = new PageRequest(cv.getCurrentPage(), cv.getPageSize());
        Page<TaskVo> result = new PageImpl<TaskVo>(list, pageable, count);
        return result;
    }

    @Override
    public Task get(long taskId) {
        return taskMapper.findOne(taskId);
    }

    @Override
    public long add(Task task) {
        task.setCreateTime(new Date());
        task.setCreateUser(ShiroUtil.getLoginName());
        task.setUpdateTime(new Date());
        task.setUpdateUser(ShiroUtil.getLoginName());
        task.setStatus(Constants.OA_STATUS_NEW);
        taskMapper.add(task);
        return task.getId();
    }

    @Override
    public void update(Task task) {
        Task old = taskMapper.findOne(task.getId());
        old.setUpdateTime(new Date());
        old.setUpdateUser(ShiroUtil.getLoginName());
        taskMapper.update(task);
    }

    @Override
    public void delete(long taskId) {
        taskMapper.delete(taskId);
    }

    @Override
    public void addFile(MultipartFile file, long taskId,int typeId) {
        String childPath = DateTimeUtil.dateToStr(new Date());//子路径
        File childDir = null;
        File localFile = null;
        String fileName ="";
        if (file!=null && !file.isEmpty()) {
            try {
                fileName = childPath+File.separator +file.getOriginalFilename();
                InputStream inputStream = file.getInputStream();
                childDir = new File(attatchPath, childPath);//根路径+子路径
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
        TaskAttach ta = new TaskAttach();
        ta.setCreateTime(new Date());
        ta.setCreateUser(ShiroUtil.getLoginName());
        ta.setTargetId(taskId);
        ta.setTargetType(typeId);
        ta.setFileName(fileName);
        ta.setOriginalName(file.getOriginalFilename());
        ta.setSavePath(childPath);
        taskMapper.addFile(ta);

    }
}
