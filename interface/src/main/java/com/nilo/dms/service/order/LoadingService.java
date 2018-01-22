package com.nilo.dms.service.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.order.model.Loading;

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

    void ship(String merchantId, String loadingNo, String optBy);

    void deleteLoading(String merchantId, String loadingNo, String optBy);

}
