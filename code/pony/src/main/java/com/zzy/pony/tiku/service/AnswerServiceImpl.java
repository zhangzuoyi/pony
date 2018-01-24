package com.zzy.pony.tiku.service;

import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.tiku.mapper.AnswerMapper;
import com.zzy.pony.tiku.mapper.RecordMapper;
import com.zzy.pony.tiku.model.Answer;
import com.zzy.pony.tiku.model.Record;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-24
 * @Description
 */
@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public void deleteById(Long id) {
        answerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insert(Answer record) {
        answerMapper.insert(record);
    }

    @Override
    public Answer selectById(Long id) {
        return answerMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Answer record) {
        answerMapper.update(record);
    }

    @Override
    public void addBatch(List<ZujuanQuestionVo> list) {

        if (list == null || list.size() <= 0) {
            return;
        }

        Record record = new Record();
        record.setCreateTime(new Date());
        record.setCreateUser(ShiroUtil.getLoginName());
        record.setZujuanId((long) list.get(0).getZujuanId());
        recordMapper.insert(record);
        List<Answer> answers = new ArrayList<Answer>();
        for (ZujuanQuestionVo vo :
                list) {
            Answer answer = new Answer();
            if (vo.getAnswer() != null){
                answer.setAnswer(vo.getAnswer().toString());//数组或者string
            }
            answer.setQuestionId((long) vo.getQuestionId());
            answer.setRecordId(record.getId());
            answers.add(answer);

        }

        answerMapper.addBatch(answers);


    }
}
