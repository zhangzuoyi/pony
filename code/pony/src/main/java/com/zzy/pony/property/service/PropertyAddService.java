package com.zzy.pony.property.service;


import com.zzy.pony.property.model.Property;







public interface PropertyAddService {	
	void add(Property property);
	
	/*** 
	* <p>Description:当天的最大值 </p>
	* @author  WANGCHAO262
	* @date  2017年5月10日 下午5:37:12
	*/
	int  maxCode(String date);
	
	
	
}
