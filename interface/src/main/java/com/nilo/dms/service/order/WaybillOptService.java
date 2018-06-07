package com.nilo.dms.service.order;

import com.nilo.dms.dto.handle.HandleRefuse;
import com.nilo.dms.dto.order.AbnormalParam;
import com.nilo.dms.dto.order.DelayParam;
import com.nilo.dms.dto.order.SignForOrderParam;

import java.util.List;

/**
 * Created by admin on 2017/10/30.
 */
public interface WaybillOptService {

    /**
     * 签收
     * @param orderNo
     * @param signer
     * @param remark
     */
    void sign(String orderNo, String signer, String remark);

    /**
     * 拒收
     * @param param
     */
    void refuse(HandleRefuse handleRefuse);

    /**
     * 运单标记为延迟件
     * @param param
     */
    void delay(DelayParam param);

    /**
     * 处理完成延迟件
     * @param orderNo
     */
    void completeDelay(String orderNo);

    /**
     * 分配快递员上门揽件
     * @param orderNos
     * @param riderId
     * @param remark
     */
    void allocate(List<String> orderNos,String riderId,String remark);
}
