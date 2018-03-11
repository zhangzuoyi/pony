package com.zzy.pony.oa.service;

import com.zzy.pony.oa.mapper.TaskLogMapper;
import com.zzy.pony.oa.model.TaskLog;
import com.zzy.pony.security.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-26
 * @Description
 */
@Service
@Transactional
public class TaskLogServiceImpl implements TaskLogService {

    @Autowired
    private TaskLogMapper taskLogMapper;

    @Override
    public void add(TaskLog tl) {
        tl.setCreateTime(new Date());
        tl.setCreateUser(ShiroUtil.getLoginName());
        taskLogMapper.add(tl);
    }
}
