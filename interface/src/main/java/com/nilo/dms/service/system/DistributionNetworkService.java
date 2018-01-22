package com.nilo.dms.service.system;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.service.system.model.DistributionNetwork;

import java.util.List;

/**
 * Created by admin on 2017/12/5.
 */
public interface DistributionNetworkService {

    void add(DistributionNetwork network);

    void update(DistributionNetwork network);

    List<DistributionNetwork> queryBy(String merchantId, String name, Pagination pagination);

    DistributionNetwork queryByCode(String merchantId, String code);

    void updateUserNetwork(String merchantId, String userId, Long[] distributions);
}
