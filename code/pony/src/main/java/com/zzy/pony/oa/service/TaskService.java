package com.zzy.pony.oa.service;

import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.data.domain.Page;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface TaskService {

    Page<TaskVo> list(ConditionVo cv);
    Task get(long taskId);
    void add(Task task);
    void update(Task task);
    void delete(long taskId);

}
