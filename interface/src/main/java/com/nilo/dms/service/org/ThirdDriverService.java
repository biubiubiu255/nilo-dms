package com.nilo.dms.service.org;

import java.util.List;
import java.util.ArrayList;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;


public interface ThirdDriverService {

	List<ThirdDriverDO>  findByDriverCode(String thirdExpressCode, Pagination pagination);
	
	List<ThirdDriverDO>  findDriverAll(Pagination page, ThirdDriverDO driver);
	
	List<ThirdExpressDO> findExpressAll();
	
	void add(ThirdDriverDO express);
	
    void update(ThirdDriverDO express);
    
    void delete(ThirdDriverDO express);

}
