package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.model.Dict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */

public interface DictService {

    List<Dict> selectByType(String type);
}
