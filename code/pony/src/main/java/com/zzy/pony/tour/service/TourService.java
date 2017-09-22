package com.zzy.pony.tour.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zzy.pony.tour.vo.TourCategoryVo;
import com.zzy.pony.tour.vo.TourConditionVo;
import com.zzy.pony.tour.vo.TourVo;

public interface TourService {
	List<TourCategoryVo> categories();
	void add(TourVo tour);
	Page<TourVo> findPage(TourConditionVo condition);
}
