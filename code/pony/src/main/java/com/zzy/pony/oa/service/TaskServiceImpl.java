package com.zzy.pony.oa.service;

import com.zzy.pony.oa.mapper.TaskMapper;
import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.vo.ConditionVo;
import com.zzy.pony.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Override
    public Page<TaskVo> list(ConditionVo cv) {
        List<TaskVo> list=taskMapper.findPage(cv);
        int count=taskMapper.findCount(cv);
        Pageable pageable = new PageRequest(cv.getCurrentPage(), cv.getPageSize());
        Page<TaskVo> result = new PageImpl<TaskVo>(list, pageable, count);
        return result;
    }

    @Override
    public Task get(long taskId) {
        return taskMapper.findOne(taskId);
    }

    @Override
    public void add(Task task) {
        taskMapper.add(task);
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
}
