package com.zzy.pony.tour.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zzy.pony.tour.vo.TourItemVo;

public interface TourItemMapper {
	List<TourItemVo> findAll();
	List<String> findCategory();
	void insertItemData(long tourId, long itemId);
	List<TourItemVo> findDataByTours(@Param(value="tourIds")long[] tourIds);
}
