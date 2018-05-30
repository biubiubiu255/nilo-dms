package com.nilo.dms.dao;

import com.nilo.dms.common.BaseDao;
import com.nilo.dms.dao.dataobject.WaybillDO;
import com.nilo.dms.dto.handle.SendThirdHead;
import com.nilo.dms.dto.handle.SendThirdDetail;
import com.nilo.dms.dto.order.Waybill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/4/2.
 */
@Repository
public interface HandleThirdDao extends BaseDao<Long, SendThirdHead> {

    SendThirdHead queryBigByHandleNo(@Param("merchantId") Long merchantId, @Param("handleNo") String handleNo);

    List<SendThirdHead> queryBig(@Param("ob") SendThirdHead sendThirdHead, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer queryBigCount(@Param("ob") SendThirdHead sendThirdHead);

    SendThirdHead queryHandleBySmallNo(@Param("merchantId") Long merchantId, @Param("orderNo") String orderNo);

    void insertBig(SendThirdHead sendThirdHead);

    void editBigBy(SendThirdHead sendThirdHead);

    //下面是子包
    List<SendThirdDetail> querySmall(@Param("merchantId") Long merchantId, @Param("handleNo") String handleNo);

    void insertSmalls(List<SendThirdDetail> list);

    void deleteSmallByHandleNo(@Param("merchantId") Long merchantId, @Param("handleNo") String handleNo);

}
