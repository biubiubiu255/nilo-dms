package com.nilo.dms.service.order;

import java.util.List;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.*;

/**
 * Created by ronny on 2017/9/15.
 */
public interface WaybillService {

    String createWaybillRequest(String merchantId, String data, String sign);

    void updateWaybill(WaybillHeader header);

    List<Waybill> queryWaybillBy(WaybillParameter parameter, Pagination pagination);

    Waybill queryByOrderNo(String merchantId, String orderNo);

    List<Waybill> queryByOrderNos(String merchantId, List<String> orderNos);

    boolean checkHandleOpt(OrderOptRequest optRequest);

    void handleOpt(OrderOptRequest optRequest);

    void arrive(List<String> waybillNos);

    void print(String merchantId, List<String> orderNos);

    String addPackage(PackageRequest packageRequest);

    void unpack(UnpackRequest unpackRequest);

    List<Waybill> queryByPackageNo(String merchantNo, String packageNo);

}
