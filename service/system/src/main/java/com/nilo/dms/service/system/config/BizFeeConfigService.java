package com.nilo.dms.service.system.config;

import com.nilo.dms.dto.system.BizFeeConfig;

import java.util.List;


/**
 * Created by admin on 2017/11/13.
 */


public interface BizFeeConfigService {

    List<BizFeeConfig> queryAll();

    List<BizFeeConfig> queryAllBy(String merchantId);

    void addConfig(BizFeeConfig config);

    void updateConfig(BizFeeConfig config);

    BizFeeConfig queryBy(String merchantId, String optType);

}
