package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.ArrangeCombine;
import com.zzy.pony.vo.CombineAndRotationVo;









public interface ArrangeCombineService {
	List<CombineAndRotationVo> findAllVo();
	ArrangeCombine get(int id);
}
