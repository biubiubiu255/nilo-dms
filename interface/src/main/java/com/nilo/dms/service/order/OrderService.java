package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.model.*;

import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
public interface OrderService {

    String addCreateDeliveryOrderRequest(String merchantId, String data, String sign);

    String createDeliveryOrder(DeliveryOrder data);

    DeliveryOrderStatusInfo queryStatus(String merchantId, String orderNo);

    List<DeliveryOrder> queryDeliveryOrderBy(DeliveryOrderParameter parameter, Pagination pagination);

    DeliveryOrder queryByOrderNo(String merchantId, String orderNo);

    List<DeliveryOrder> queryByOrderNos(String merchantId, List<String> orderNos);

    void handleOpt(OrderOptRequest optRequest);

    void arrive(String merchantId,String scanNo,String networkId,String arriveBy);

    void print(String merchantId, List<String> orderNos);

    String addPackage(PackageRequest packageRequest);

    List<DeliveryOrder> queryByPackageNo(String merchantNo,String packageNo);
    
    void waybillNoListArrive(List<String> waybillNos, String arriveBy, String merchantId);
    
    void updataMultiChildOrder(String merchantId, String networkId, String arriveBy, String scanNo, List<String> ChildorderNos);

}
