package com.zzy.pony.evaluation.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.model.OutcomeAttach;
import com.zzy.pony.evaluation.vo.OutcomeVo;

public interface OutcomeService {
	List<OutcomeVo> findByTeacher(Integer teacherId);
	void add(Outcome outcome);
	void update(Outcome outcome);
	void saveAttach(MultipartFile file, Long outcomeId);
	List<OutcomeAttach> findAttach(Long outcomeId);
	byte[] getAttachContent(OutcomeAttach attach);
	void deleteAttach(Long attachId);
}
