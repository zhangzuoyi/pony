package com.zzy.pony.tiku.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzy.pony.tiku.mapper.QuestionMapper;
import com.zzy.pony.tiku.vo.MyJson;
import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Page<QuestionVo> list(ConditionVo cv) {
        cv.setStartNum((cv.getCurrentPage()-1)*cv.getPageSize());
        List<QuestionVo> list=questionMapper.findPage(cv);
        //items str-->array
        for (QuestionVo vo:
        list) {
            try {
                Type listType = new TypeToken<ArrayList<MyJson>>(){}.getType();
                Gson gson = new Gson();
                ArrayList<MyJson> jsons = gson.fromJson(vo.getItems(), listType);
                vo.setItemArr(jsons.toArray());
            }finally {
                continue;
            }
        }

        int count=questionMapper.findCount(cv);
        Pageable pageable = new PageRequest(cv.getCurrentPage()-1, cv.getPageSize());
        Page<QuestionVo> result = new PageImpl<QuestionVo>(list, pageable, count);
        return result;

    }


}

