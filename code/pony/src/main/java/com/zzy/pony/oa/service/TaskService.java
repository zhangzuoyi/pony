package com.zzy.pony.oa.service;

import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.model.TaskAttach;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface TaskService {



    Page<TaskVo> list(ConditionVo cv);
    Page<TaskVo> listMy(ConditionVo cv);
    Task get(long taskId);
    long add(Task task);
    void update(Task task);
    void delete(long taskId);
    void addFile(MultipartFile file, long taskId,int typeId);

    List<TaskAttach> findByTaskId(long taskId);
    List<TaskAttach> findByTypeAndTaskId(int type,long taskId);
    TaskAttach findByAttachId(long taId);

}
