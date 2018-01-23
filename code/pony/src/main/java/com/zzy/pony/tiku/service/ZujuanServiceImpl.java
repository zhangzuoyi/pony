package com.zzy.pony.tiku.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.tiku.mapper.QuestionMapper;
import com.zzy.pony.tiku.mapper.ZujuanMapper;
import com.zzy.pony.tiku.mapper.ZujuanQuestionMapper;
import com.zzy.pony.tiku.model.Zujuan;
import com.zzy.pony.tiku.model.ZujuanQuestion;
import com.zzy.pony.tiku.vo.MyJson;
import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;
import com.zzy.pony.tiku.vo.ZujuanVo;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
@Service
@Transactional
public class ZujuanServiceImpl implements  ZujuanService {

    @Autowired
    private ZujuanMapper zujuanMapper;
    @Autowired
    private ZujuanQuestionMapper zujuanQuestionMapper;
    @Autowired
    private QuestionMapper questionMapper;



    @Override
    public Page<ZujuanVo> list(ConditionVo cv) {

        cv.setStartNum((cv.getCurrentPage()-1)*cv.getPageSize());
        List<ZujuanVo> list=zujuanMapper.findPage(cv);
        //查找questionVo
        /*for (ZujuanVo vo:
        list) {
        List<ZujuanQuestionVo>  questionVos =  zujuanQuestionMapper.findByZujuanId(vo.getId());
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
            vo.setQuestions(questionVos);
        }*/
        int count=zujuanMapper.findCount(cv);
        Pageable pageable = new PageRequest(cv.getCurrentPage()-1, cv.getPageSize());
        Page<ZujuanVo> result = new PageImpl<ZujuanVo>(list, pageable, count);
        return result;
    }

    @Override
    public Zujuan findById(long id) {
        return zujuanMapper.findById(id);
    }

    @Override
    public void add(Zujuan zujuan) {
        zujuanMapper.add(zujuan);
    }

    @Override
    public void update(Zujuan zujuan) {
        zujuanMapper.update(zujuan);
    }

    @Override
    public void deleteById(long id) {
        zujuanMapper.deleteById(id);
    }

    @Override
    public void addBatch(String name,String gradeId,String subjectId, List<Long> questionIds) {

        Zujuan zujuan = new Zujuan();
        zujuan.setName(name);
        zujuan.setGradeCode(Integer.valueOf(gradeId));
        zujuan.setSubjectCode(Integer.valueOf(subjectId));
        zujuan.setCreateTime(new Date());
        zujuan.setUpdateTime(new Date());
        zujuan.setCreateUser(ShiroUtil.getLoginName());
        zujuan.setUpdateUser(ShiroUtil.getLoginName());

        zujuanMapper.add(zujuan);

        if (questionIds != null && questionIds.size()>0){
            List<QuestionVo> questionVos = questionMapper.findByIds(questionIds);
            Collections.sort(questionVos);
            List<ZujuanQuestion> list = new ArrayList<ZujuanQuestion>();
            int seq = 1;
            for (QuestionVo vo:
                    questionVos) {
                ZujuanQuestion zq = new ZujuanQuestion();
                zq.setQuestionId(vo.getId());
                zq.setZujuanId(zujuan.getId());
                zq.setSeq(seq);
                seq++;
                list.add(zq);
            }
            zujuanQuestionMapper.addBatch(list);
        }

    }
}
