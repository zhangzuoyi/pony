package com.zzy.pony.tour.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zzy.pony.tour.mapper.TourItemMapper;
import com.zzy.pony.tour.mapper.TourMapper;
import com.zzy.pony.tour.vo.TourCategoryVo;
import com.zzy.pony.tour.vo.TourConditionVo;
import com.zzy.pony.tour.vo.TourItemVo;
import com.zzy.pony.tour.vo.TourVo;
@Service
@Transactional
public class TourServiceImpl implements TourService {
	@Autowired
	private TourItemMapper itemMapper;
	@Autowired
	private TourMapper mapper;

	@Override
	public List<TourCategoryVo> categories() {
		List<String> categories=itemMapper.findCategory();
		List<TourItemVo> items=itemMapper.findAll();
		List<TourCategoryVo> result=new ArrayList<TourCategoryVo>();
		for(String category: categories) {
			TourCategoryVo vo=new TourCategoryVo();
			vo.setCategory(category);
			for(TourItemVo item: items) {
				if(category.equals(item.getCategory())) {
					vo.getItems().add(item);
				}
			}
			result.add(vo);
		}
		return result;
	}

	@Override
	public void add(TourVo tour) {
		long tourId=mapper.add(tour);
		for(TourCategoryVo cvo: tour.getCategories()) {
			for(TourItemVo item: cvo.getItems()) {
				if(item.isCheck()) {
					itemMapper.insertItemData(tourId, item.getItemId());
				}
			}
		}
	}

	@Override
	public Page<TourVo> findPage(TourConditionVo condition) {
		condition.setFirstRow(condition.getCurrentPage()*condition.getPageSize());
		List<TourVo> list=mapper.find(condition);
		int total=mapper.count(condition);
		long[] tourIds=new long[list.size()];
		for(int i=0;i<tourIds.length;i++) {
			tourIds[i]=list.get(i).getTourId();
		}
		if(tourIds.length>0) {
			List<TourItemVo> items=itemMapper.findDataByTours(tourIds);
			for(TourVo vo: list) {
				setItemSummary(vo, items);
			}
		}
		
		Pageable pageable = new PageRequest(condition.getCurrentPage(), condition.getPageSize());
		Page<TourVo> result = new PageImpl<TourVo>(list, pageable, total);
		return result;
	}

	private void setItemSummary(TourVo vo, List<TourItemVo> items) {
		StringBuilder sb=new StringBuilder();
		for(TourItemVo item: items) {
			if(vo.getTourId() == item.getTourId()) {
				sb.append(item.getName()+";");
			}
		}
		vo.setItemSummary(sb.toString());
	}
}
