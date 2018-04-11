package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.*;
import com.nilo.dms.dto.order.Waybill;

import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public interface SendNextStationService {

    void insertSmallAll(Long merchantId, String handleNo, String[] smallOrders);

    void insertBig(SendNextStationDO sendNextStationDO);

    void insertBigAndSmall(Long merchantId, SendNextStationDO sendNextStationDO, String[] smallOrders);

    List<SendNextStationDO> queryBigs(Long merchantId, SendNextStationDO sendNextStationDO, Pagination page);

    List<SendNextStationDetailDO> querySmalls(String merchantId, SendNextStationDetailDO sendNextStationDetailDO, Pagination page);

    public List<Waybill> querySmallsPlus(String merchantId, String handleNo, Pagination page);

    void editBig(SendNextStationDO sendNextStationDO);

    void editSmall(Long merchantId, String handleNo, String[] smallOrders);

}
