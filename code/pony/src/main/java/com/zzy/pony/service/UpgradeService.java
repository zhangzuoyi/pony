package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.vo.UpgradeVo;

public interface UpgradeService {
	List<UpgradeVo> preview(Integer[] gradeIds);
	void upgrade(UpgradeVo[] vos, String loginName);
}
