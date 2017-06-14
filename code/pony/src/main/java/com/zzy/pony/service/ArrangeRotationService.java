package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.ArrangeRotation;
import com.zzy.pony.vo.CombineAndRotationVo;









public interface ArrangeRotationService {
	List<CombineAndRotationVo> findAllVo();
	ArrangeRotation get(int id);
}
