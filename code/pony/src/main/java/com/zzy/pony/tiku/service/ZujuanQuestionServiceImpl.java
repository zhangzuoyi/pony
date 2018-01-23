package com.zzy.pony.tiku.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzy.pony.tiku.mapper.ZujuanQuestionMapper;
import com.zzy.pony.tiku.model.ZujuanQuestion;
import com.zzy.pony.tiku.vo.MyJson;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
@Service
@Transactional
public class ZujuanQuestionServiceImpl implements ZujuanQuestionService {

    @Autowired
    private ZujuanQuestionMapper zujuanQuestionMapper;

    @Override
    public ZujuanQuestion findById(long id) {
        return zujuanQuestionMapper.findById(id);
    }

    @Override
    public void add(ZujuanQuestion zujuanQuestion) {
        zujuanQuestionMapper.add(zujuanQuestion);
    }

    @Override
    public void update(ZujuanQuestion zujuanQuestion) {
        zujuanQuestionMapper.update(zujuanQuestion);
    }

    @Override
    public void deleteById(long id) {
        zujuanQuestionMapper.deleteById(id);
    }

    @Override
    public List<ZujuanQuestionVo> listQuestion(long zujuanId) {

        List<ZujuanQuestionVo>  questionVos =  zujuanQuestionMapper.findByZujuanId(zujuanId);
        for (ZujuanQuestionVo innerVo:
                questionVos) {
            try {
                Type listType = new TypeToken<ArrayList<MyJson>>(){}.getType();
                Gson gson = new Gson();
                ArrayList<MyJson> jsons = gson.fromJson(innerVo.getItems(), listType);
                innerVo.setItemArr(jsons.toArray());
            }finally {
                continue;
            }
        }
        return questionVos;
    }

    @Override
    public void deleteByZujuanId(long zujuanId) {
        zujuanQuestionMapper.deleteByZujuanId(zujuanId);
    }
}
