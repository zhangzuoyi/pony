package com.zzy.pony.util;

import java.util.Collections;
import java.util.List;

public class CollectionsUtil {
	
	
	/*** 
	* <p>Description: 调换集合中两个指定位置的元素, 若两个元素位置中间还有其他元素，需要实现中间元素的前移或后移的操作</p>
	* @author  WANGCHAO262
	* @date  2017年8月2日 下午4:00:50
	*/
	public static <T> void swap(List<T> list, int oldPosition, int newPosition){
	    if(null == list){
	        throw new IllegalStateException("list不能为空");
	    }

	    // 向前移动，前面的元素需要向后移动
	    if(oldPosition < newPosition){
	        for(int i = oldPosition; i < newPosition; i++){
	            Collections.swap(list, i, i + 1);
	        }
	    }

	    // 向后移动，后面的元素需要向前移动
	    if(oldPosition > newPosition){
	        for(int i = oldPosition; i > newPosition; i--){
	            Collections.swap(list, i, i - 1);
	        }
	    }
	}
}
