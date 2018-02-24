package com.zzy.pony.oa.service;

import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.model.TaskProgress;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface TaskProgressService {

    long add(TaskProgress taskProgress);
    void update(TaskProgress taskProgress);
    void delete(long tpId);

}
