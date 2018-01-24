package com.zzy.pony.tiku.mapper;

import com.zzy.pony.tiku.model.Answer;

import java.util.List;

public interface AnswerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Answer record);

    Answer selectByPrimaryKey(Long id);

    int update(Answer record);

    void addBatch(List<Answer> list);


}