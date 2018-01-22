package com.nilo.dms.service.system;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.MerchantStatusEnum;
import com.nilo.dms.service.system.model.MerchantInfo;

import java.util.List;

/**
 * Created by ronny on 2017/8/25.
 */
public interface MerchantService {

    void addMerchant(MerchantInfo merchant);

    void updateMerchantInfo(MerchantInfo merchant);

    void updateMerchantStatus(Long merchantId, MerchantStatusEnum status);

    List<MerchantInfo> findBy(String merchantName, Pagination page);

    List<MerchantInfo> findAll();

    MerchantInfo findById(String id);
}
