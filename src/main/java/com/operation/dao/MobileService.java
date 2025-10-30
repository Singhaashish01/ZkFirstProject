package com.operation.dao;

import java.util.List;

import com.operation.model.Mobile;

public interface MobileService {
	
	List<Mobile> findAllMobile();
		
	
	
	List<Mobile> findMobiles(String keyword);
	List<Mobile> findproductsByKey(String keyword);

	

}
