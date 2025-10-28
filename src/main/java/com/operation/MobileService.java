package com.operation;

import java.util.List;

public interface MobileService {
	
	List<Mobile> findAllMobile();
		
	
	
	List<Mobile> findMobiles(String keyword);
	List<Mobile> findproductsByKey(String keyword);

	

}
