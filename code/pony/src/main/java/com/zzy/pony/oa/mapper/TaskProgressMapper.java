package com.zzy.pony.oa.mapper;

import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.model.TaskAttach;
import com.zzy.pony.oa.model.TaskProgress;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface TaskProgressMapper {


    TaskProgress findOne(long tpId);
    void update(TaskProgress tp);
    long add(TaskProgress tp);
    void delete(long tpId);

}
