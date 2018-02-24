package com.zzy.pony.oa.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.mapper.TaskProgressMapper;
import com.zzy.pony.oa.model.TaskAttach;
import com.zzy.pony.oa.model.TaskProgress;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.util.DateTimeUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-24
 * @Description
 */
public class TaskProgressServiceImpl implements TaskProgressService {

    @Autowired
    private TaskProgressMapper taskProgressMapper;
    @Value("${oaTaskAttatch.baseUrl}")
    private String attatchPath;

    @Override
    public long add(TaskProgress taskProgress) {
        return taskProgressMapper.add(taskProgress);
    }

    @Override
    public void update(TaskProgress taskProgress) {
        taskProgressMapper.update(taskProgress);
    }

    @Override
    public void delete(long tpId) {
        taskProgressMapper.delete(tpId);
    }


}
