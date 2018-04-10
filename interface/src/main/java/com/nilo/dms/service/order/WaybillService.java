package com.nilo.dms.service.order;

import java.util.List;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.service.order.model.Waybill;

/**
 * Created by ronny on 2017/9/15.
 */
public interface WaybillService {

    String addCreateDeliveryOrderRequest(String merchantId, String data, String sign);

    String createDeliveryOrder(Waybill data);

    void updateWeight(String merchantId,String orderNo, Double weight);

    List<Waybill> queryDeliveryOrderBy(DeliveryOrderParameter parameter, Pagination pagination);

    Waybill queryByOrderNo(String merchantId, String orderNo);

    List<Waybill> queryByOrderNos(String merchantId, List<String> orderNos);

    void handleOpt(OrderOptRequest optRequest);

    void arrive(String merchantId, String scanNo, String networkId, String arriveBy);

    void print(String merchantId, List<String> orderNos);

    String addPackage(PackageRequest packageRequest);

    void unpack(UnpackRequest unpackRequest);

    List<Waybill> queryByPackageNo(String merchantNo, String packageNo);

    void waybillNoListArrive(List<String> waybillNos, String arriveBy, String merchantId, String netWork);

    long updatePaidType(DeliveryOrderDO deliveryOrderDO);

    void waybillArrvieWeighing(String waybillNo, Double weight, Double length, Double width, Double height,
                               String arriveBy, String merchantId, String networkId);

}
