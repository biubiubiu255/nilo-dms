package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.RiderDeliveryDO;
import com.nilo.dms.dao.dataobject.RiderDeliverySmallDO;
import com.nilo.dms.dao.dataobject.SendNextStationDO;
import com.nilo.dms.dao.dataobject.SendNextStationDetailDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface  SendNextStationDao extends BaseDao<Long, SendNextStationDO> {

    List<SendNextStationDO> queryBig(@Param("ob") SendNextStationDO sendNextStationDO, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer queryBigCount(@Param("ob") SendNextStationDO sendNextStationDO, @Param("offset") Integer offset, @Param("limit") Integer limit);

    void insertBig(SendNextStationDO sendNextStationDO);

    void editBigBy(SendNextStationDO sendNextStationDO);

    //下面是子包

    List<SendNextStationDetailDO> querySmall(SendNextStationDetailDO sendNextStationDetailDO);

    void insertSmall(SendNextStationDetailDO sendNextStationDetailDO);

    void insertSmalls(List<SendNextStationDetailDO> list);

    void deleteSmallByHandleNo(SendNextStationDetailDO sendNextStationDetailDO);

}
