package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.WaybillStatementDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaybillStatementDao {
    //不用类的方式，传入xml mapping文件多个参数进去
    List<WaybillStatementDo> queryAllWaybillStatement(@Param("startTime") int sTime, @Param("endTime") int eTime);
}
