package com.nilo.dms.dao;

import com.nilo.dms.dao.dataobject.WaybillTaskDo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface WaybillPenalDao {

    Integer querySignMonthCount(@Param("network") String network, @Param("dataFormat") String monthFormat);

    Integer queryTimeOutSignCount(@Param("network") String network, @Param("dataFormat") String monthFormat, @Param("dayNum") Integer dayNum);

    @MapKey("day")
    Map<String, Integer> queryGroupSignMonthCount(@Param("network") String network, @Param("fromTime") Integer fromTime, @Param("toTime") Integer toTime);
}
