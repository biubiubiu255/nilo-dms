package com.nilo.dms.service.system.config.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.InterfaceStatusEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.InterfaceConfigDao;
import com.nilo.dms.dao.dataobject.InterfaceConfigDO;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.config.InterfaceConfigService;
import com.nilo.dms.service.system.model.InterfaceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/9.
 */
@Service
public class InterfaceConfigServiceImpl implements InterfaceConfigService {

    @Autowired
    private InterfaceConfigDao interfaceConfigDao;

    @Override
    public List<InterfaceConfig> queryAll(String merchantId) {

        List<InterfaceConfigDO> configDOList = interfaceConfigDao.queryByMerchantId(Long.parseLong(merchantId));
        List<InterfaceConfig> list = new ArrayList<>();
        if (configDOList != null) {
            for (InterfaceConfigDO configDO : configDOList) {
                list.add(convertTo(configDO));
            }
        }
        return list;

    }

    @Override
    public void update(InterfaceConfig interfaceConfig) {

        //比较是否有更新
        InterfaceConfigDO query = interfaceConfigDao.queryByMethod(Long.parseLong(interfaceConfig.getMerchantId()), interfaceConfig.getMethod());
        if (StringUtil.equals(query.getOp(), interfaceConfig.getOp())
                && StringUtil.equals(query.getUrl(), interfaceConfig.getUrl())
                && StringUtil.equals(query.getRequestMethod(), interfaceConfig.getRequestMethod())) {
            return;
        }
        InterfaceConfigDO configDO = convertTo(interfaceConfig);
        long update = interfaceConfigDao.update(configDO);
        if (update > 0) {
            //refresh cache
            RedisUtil.hset(Constant.INTERFACE_CONF + interfaceConfig.getMerchantId(), interfaceConfig.getMethod(), JSON.toJSONString(interfaceConfig));
        }
    }

    private InterfaceConfig convertTo(InterfaceConfigDO configDO) {
        if (configDO == null) {
            return null;
        }
        InterfaceConfig config = new InterfaceConfig();
        config.setMerchantId("" + configDO.getMerchantId());
        config.setOp(configDO.getOp());
        config.setUrl(configDO.getUrl());
        config.setRequestMethod(configDO.getRequestMethod());
        config.setMethod(configDO.getMethod());
        config.setStatus(InterfaceStatusEnum.getEnum(configDO.getStatus()));
        return config;
    }

    private InterfaceConfigDO convertTo(InterfaceConfig config) {
        if (config == null) {
            return null;
        }
        InterfaceConfigDO configDO = new InterfaceConfigDO();
        configDO.setMerchantId(Long.parseLong(config.getMerchantId()));
        configDO.setOp(config.getOp());
        configDO.setUrl(config.getUrl());
        configDO.setRequestMethod(config.getRequestMethod());
        configDO.setMethod(config.getMethod());
        if (config.getStatus() != null) {
            configDO.setStatus(config.getStatus().getCode());
        }
        return configDO;
    }

}
