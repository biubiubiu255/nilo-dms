package com.nilo.dms.service.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.WaybillExternalDao;
import com.nilo.dms.dao.WaybillExternalDao;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dao.dataobject.WaybillExternalDo;
import com.nilo.dms.dao.dataobject.WaybillExternalDo;
import com.nilo.dms.service.org.ExternalService;
import com.nilo.dms.service.org.ThirdDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalServiceImpl implements ExternalService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WaybillExternalDao waybillExternalDao;


	@Override
	public WaybillExternalDo findSingleByID(int id) {
		//System.out.println("测试：" + orderNo);
		return waybillExternalDao.findByID(id);
	}

	public WaybillExternalDo findSingleByOrder(String orderNo) {
		//System.out.println("测试：" + orderNo);
		return waybillExternalDao.findByOrder(orderNo);
	}


	@Override
	public List<WaybillExternalDo> findExternalAll(Pagination page) {
        List<WaybillExternalDo> list = new ArrayList<WaybillExternalDo>();
		list = waybillExternalDao.queryExternalAll();
        page.setTotalCount(list.size());
		return list;
	}

	@Override
	public List<WaybillExternalDo> findExternalAllFuzzy(WaybillExternalDo external, Pagination page) {
		if (external.getOrderNo()==null) external.setOrderNo("");
		List<WaybillExternalDo> list = new ArrayList<WaybillExternalDo>();
		list = waybillExternalDao.queryExternalAllM(external);
		page.setTotalCount(list.size());
		return list;
	}


	@Override
	public void addExternal(WaybillExternalDo external) {
		waybillExternalDao.addWaybillExternal(external);
		//System.out.println("本次插入位置ID：" + external.getId());
	}

	@Override
	public void updateExternal(WaybillExternalDo external) {
		waybillExternalDao.updateExternal(external);
	}

	@Override
	public void deleteExternal(WaybillExternalDo external) {
		waybillExternalDao.deleteExternal(external);
	}

}
