package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.order.Waybill;

import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public interface SendThirdService {

    void insertSmallAll(Long merchantId, String handleNo, String[] smallOrders);

    void insertBig(SendThirdHead sendThirdHead);

    void insertBigAndSmall(Long merchantId, SendThirdHead sendThirdHead, String[] smallOrders);

    List<SendThirdHead> queryHead(SendThirdHead head, Pagination page);

    SendThirdHead queryDetailsByHandleNo(String handleNo);

    List<Waybill> querySmallsPlus(String merchantId, String handleNo);

    SendThirdHead queryLoadingBySmallNo(String merchantId, String orderNo);

    void editSmalls(SendThirdHead sendThirdHead, String[] smallOrders);

    void ship(String handleNo);

    void deleteHandle(String handleNo);
}
