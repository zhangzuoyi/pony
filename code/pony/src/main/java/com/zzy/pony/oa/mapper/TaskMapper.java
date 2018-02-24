package com.zzy.pony.oa.mapper;

import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.model.TaskAttach;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface TaskMapper {


    List<TaskVo> findPage(ConditionVo cv);
    int findCount(ConditionVo cv);
    List<TaskVo> findMyPage(ConditionVo cv);
    int findMyCount(ConditionVo cv);
    Task findOne(long taskId);
    void add(Task task);
    void update(Task task);
    void delete(long taskId);
    void addFile(TaskAttach taskAttach);

}
