package com.nilo.dms.service.system.config.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.BizFeeConfigStatusEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.BizFeeConfigDao;
import com.nilo.dms.dao.dataobject.BizFeeConfigDO;
import com.nilo.dms.dto.system.BizFeeConfig;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.config.BizFeeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */
@Service
public class BizFeeConfigServiceImpl implements BizFeeConfigService {

    @Autowired
    private BizFeeConfigDao bizFeeConfigDao;

    @Override
    public List<BizFeeConfig> queryAll() {

        List<BizFeeConfigDO> configDOList = bizFeeConfigDao.findAll();

        List<BizFeeConfig> list = new ArrayList<>();
        if (configDOList == null || configDOList.size() == 0) return list;

        for (BizFeeConfigDO configDO : configDOList) {
            list.add(convert(configDO));
        }
        return list;

    }

    @Override
    public List<BizFeeConfig> queryAllBy(String merchantId) {
        List<BizFeeConfigDO> configDOList = bizFeeConfigDao.findAllBy(Long.parseLong(merchantId));

        List<BizFeeConfig> list = new ArrayList<>();
        if (configDOList == null || configDOList.size() == 0) return list;

        for (BizFeeConfigDO configDO : configDOList) {
            list.add(convert(configDO));
        }
        return list;
    }

    @Override
    public void addConfig(BizFeeConfig config) {

        //判断是否存在
        BizFeeConfigDO queryDo = bizFeeConfigDao.findByOptType(Long.parseLong(config.getMerchantId()), config.getOptType());
        if (queryDo != null) throw new DMSException(BizErrorCode.BIZ_FEE_CONFIG_EXIST, config.getOptType());
        config.setStatus(BizFeeConfigStatusEnum.NORMAL);
        BizFeeConfigDO configDO = convert(config);
        bizFeeConfigDao.insert(configDO);

        refreshCache(config);
    }

    @Override
    public void updateConfig(BizFeeConfig config) {

        //判断更新是否不为空
        if (config.getFee() != null || StringUtil.isNotEmpty(config.getRemark())) {
            //先设置为history再新增
            BizFeeConfigDO history = new BizFeeConfigDO();
            history.setStatus(BizFeeConfigStatusEnum.HISTORY.getCode());
            history.setMerchantId(Long.parseLong(config.getMerchantId()));
            history.setOptType(config.getOptType());
            bizFeeConfigDao.update(history);

            addConfig(config);
        } else {
            BizFeeConfigDO configDO = convert(config);
            bizFeeConfigDao.update(configDO);
            refreshCache(config);
        }
    }

    @Override
    public BizFeeConfig queryBy(String merchantId, String optType) {

        BizFeeConfigDO configDO = bizFeeConfigDao.findByOptType(Long.parseLong(merchantId), optType);
        if (configDO == null) return null;

        return convert(configDO);
    }

    private BizFeeConfig convert(BizFeeConfigDO configDO) {

        BizFeeConfig config = new BizFeeConfig();
        config.setRemark(configDO.getRemark());
        config.setMerchantId("" + configDO.getMerchantId());
        config.setOptType(configDO.getOptType());
        config.setFee(configDO.getFee());
        config.setStatus(BizFeeConfigStatusEnum.getEnum(configDO.getStatus()));

        return config;
    }

    private BizFeeConfigDO convert(BizFeeConfig config) {

        BizFeeConfigDO bizFeeConfigDO = new BizFeeConfigDO();
        if (config.getStatus() != null) {
            bizFeeConfigDO.setStatus(config.getStatus().getCode());
        }

        bizFeeConfigDO.setFee(config.getFee());
        bizFeeConfigDO.setOptType(config.getOptType());
        bizFeeConfigDO.setRemark(config.getRemark());
        bizFeeConfigDO.setMerchantId(Long.parseLong(config.getMerchantId()));

        return bizFeeConfigDO;
    }

    private void refreshCache(BizFeeConfig config) {
        RedisUtil.hset(Constant.BIZ_FEE_CONF + config.getMerchantId(), config.getOptType(), JSON.toJSONString(config));
    }

}
