package com.nilo.dms.service.system.config.impl;

import com.nilo.dms.common.enums.SMSConfigStatusEnum;
import com.nilo.dms.dao.SMSConfigDao;
import com.nilo.dms.dao.dataobject.SMSConfigDO;
import com.nilo.dms.dto.system.SMSConfig;
import com.nilo.dms.service.system.config.SMSConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/12/1.
 */
@Service
public class SMSConfigServiceImpl implements SMSConfigService {

    @Autowired
    private SMSConfigDao smsConfigDao;

    @Override
    public SMSConfig queryBy(String merchantId, String smsType) {
        SMSConfigDO configDO = smsConfigDao.queryBy(Long.parseLong(merchantId), smsType);
        if (configDO == null) return null;
        return convert(configDO);
    }

    @Override
    public void add(SMSConfig smsConfig) {
        smsConfig.setStatus(SMSConfigStatusEnum.NORMAL);
        SMSConfigDO configDO = convert(smsConfig);
        smsConfigDao.insert(configDO);
    }

    @Override
    public void update(SMSConfig smsConfig) {
        SMSConfigDO configDO = convert(smsConfig);
        smsConfigDao.update(configDO);
    }

    @Override
    public List<SMSConfig> queryAllBy(String merchantId) {

        List<SMSConfigDO> configDOList = smsConfigDao.queryAllBy(Long.parseLong(merchantId));

        List<SMSConfig> configList = new ArrayList<>();
        if (configDOList == null || configDOList.size() == 0) return configList;

        for (SMSConfigDO configDO : configDOList) {
            configList.add(convert(configDO));
        }
        return configList;
    }

    @Override
    public List<SMSConfig> queryAll() {

        List<SMSConfigDO> configDOList = smsConfigDao.queryAll();

        List<SMSConfig> configList = new ArrayList<>();
        if (configDOList == null || configDOList.size() == 0) return configList;

        for (SMSConfigDO configDO : configDOList) {
            configList.add(convert(configDO));
        }
        return configList;
    }

    private SMSConfig convert(SMSConfigDO configDO) {

        SMSConfig config = new SMSConfig();
        config.setMerchantId("" + configDO.getMerchantId());
        config.setContent(configDO.getContent());
        config.setSmsType(configDO.getSmsType());
        config.setRemark(configDO.getRemark());
        config.setStatus(SMSConfigStatusEnum.getEnum(configDO.getStatus()));
        return config;
    }

    private SMSConfigDO convert(SMSConfig config) {

        SMSConfigDO configDO = new SMSConfigDO();
        configDO.setMerchantId(Long.parseLong(config.getMerchantId()));
        configDO.setRemark(config.getRemark());
        configDO.setSmsType(config.getSmsType());
        configDO.setContent(config.getContent());
        if (config.getStatus() != null) {
            configDO.setStatus(config.getStatus().getCode());
        }

        return configDO;

    }
}
