package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.Loading;
import com.nilo.dms.dto.order.ShipParameter;

import java.util.List;

/**
 * Created by admin on 2017/10/31.
 */
public interface LoadingService {

    String addLoading(Loading loading);

    List<Loading> queryBy(String merchantId, String loadingNo, Integer status, Pagination pagination);

    Loading queryByLoadingNo(String merchantId, String loadingNo);

    void loadingScan(String merchantId, String loadingNo, String orderNo, String optBy);

    void deleteLoadingDetails(String merchantId, String loadingNo, String orderNo, String optBy);

    void ship(ShipParameter parameter);

    void deleteLoading(String merchantId, String loadingNo, String optBy);

}
