package com.zzy.pony.tour.mapper;

import java.util.List;

import com.zzy.pony.tour.vo.TourConditionVo;
import com.zzy.pony.tour.vo.TourVo;

public interface TourMapper {
	List<TourVo> findAll();
	long add(TourVo tour);
	List<TourVo> find(TourConditionVo condition);
	int count(TourConditionVo condition);
}
