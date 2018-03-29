package com.nilo.dms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.RoleDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.UserInfoDao;
import com.nilo.dms.dao.UserLoginDao;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.org.ThirdDriverService;

@Service
public class ThridDriverServiceImpl implements ThirdDriverService {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ThirdExpressDao thirdExpressDao;

    @Autowired
    private ThirdDriverDao  thirdDriverDao;

    
	@Override
	public List<ThirdDriverDO> findByDriverCode(String thirdExpressCode,
			Pagination page) {
		
        List<ThirdDriverDO> list = new ArrayList<ThirdDriverDO>();
        
        list = thirdDriverDao.findByExpressCode(thirdExpressCode);
       
        page.setTotalCount(list.size());
        
		return list;
	}
    
	@Override
	public List<ThirdDriverDO> findDriverAll(Pagination page, ThirdDriverDO driver) {
		
        List<ThirdDriverDO> list = new ArrayList<ThirdDriverDO>();
        
        list = thirdDriverDao.findByExpressMultiple(driver);
        
        page.setTotalCount(list.size());
        
        //page.setTotalCount(totalCount);
		
		return list;
	}

	@Override
	public void add(ThirdDriverDO driver) {
		thirdDriverDao.add(driver);
		
	}

	@Override
	public void update(ThirdDriverDO driver) {
		thirdDriverDao.up(driver);
		
	}

	@Override
	public void delete(ThirdDriverDO driver) {
		thirdDriverDao.del(driver);
		
	}

	@Override
	public List<ThirdExpressDO> findExpressAll() {
		return thirdExpressDao.findByMerchantIdAll();
	}


}
