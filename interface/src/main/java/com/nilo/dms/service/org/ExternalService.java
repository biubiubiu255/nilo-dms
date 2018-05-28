package com.nilo.dms.service.org;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.WaybillExternalDo;

import java.util.List;
import java.util.Map;


public interface ExternalService {

	WaybillExternalDo findSingleByID(int id);

    WaybillExternalDo findSingleByOrder(String orderNo);

	List<WaybillExternalDo>  findExternalAll(Pagination page);

    List<WaybillExternalDo>  findExternalAllFuzzy(WaybillExternalDo external, Pagination page);

    List<WaybillExternalDo>  findExternalAllCons(Map map, Pagination page);

	void addExternal(WaybillExternalDo external);
	
    void updateExternal(WaybillExternalDo external);
    
    void deleteExternal(WaybillExternalDo external);

}
