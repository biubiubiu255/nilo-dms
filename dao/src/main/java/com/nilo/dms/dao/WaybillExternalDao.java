package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.WaybillExternalDo;
import com.nilo.dms.dao.dataobject.WaybillStatementDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WaybillExternalDao {
    //void addWaybillForeign(WaybillForeignDo);

    WaybillExternalDo findByID(int id);

    WaybillExternalDo findByOrder(String orderNo);

    List<WaybillExternalDo> queryExternalAll();

    List<WaybillExternalDo> queryExternalAllM(WaybillExternalDo external);

    List<WaybillExternalDo> queryExternalAllCONS(Map map);

    void addWaybillExternal(WaybillExternalDo external);

    void updateExternal(WaybillExternalDo external);

    void deleteExternal(WaybillExternalDo external);
}
