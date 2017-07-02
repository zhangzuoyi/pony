package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.ArrangeRotation;
import com.zzy.pony.vo.CombineAndRotationVo;









public interface ArrangeRotationService {
	List<CombineAndRotationVo> findAllVo();
	//查找同时出现在rotation以及combine中的
	ArrangeRotation get(int id);
}
