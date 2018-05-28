package com.nilo.dms.service.system.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.dao.SerialNumberRuleDao;
import com.nilo.dms.dao.dataobject.SerialNumberRuleDO;
import com.nilo.dms.dto.system.SerialNumberRule;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/19.
 */
@Service
public class SerialNumberServiceImpl implements SerialNumberService {


    @Autowired
    private SerialNumberRuleDao serialNumberRuleDao;

    private static final Long expireTime = 24 * 60 * 60l;

    @Override
    public List<SerialNumberRule> findAll() {
        List<SerialNumberRuleDO> queryList = serialNumberRuleDao.findAll();
        List<SerialNumberRule> list = new ArrayList<>();
        if (queryList == null) return list;
        for (SerialNumberRuleDO d : queryList) {
            list.add(convert(d));
        }
        return list;
    }

    @Override
    public List<SerialNumberRule> findAllBy(String merchantId) {
        List<SerialNumberRuleDO> queryList = serialNumberRuleDao.findAllBy(merchantId == null ? null : Long.parseLong(merchantId));
        List<SerialNumberRule> list = new ArrayList<>();
        if (queryList == null) return list;
        for (SerialNumberRuleDO d : queryList) {
            list.add(convert(d));
        }
        return list;
    }

    @Override
    public List<SerialNumberRule> findBy(String merchantId, List<String> serialType) {

        List<SerialNumberRule> list = new ArrayList<>();
        List<SerialNumberRuleDO> serialNumberRuleList = serialNumberRuleDao.findBy(Long.parseLong(merchantId), serialType);
        if (serialNumberRuleList == null) return list;
        for (SerialNumberRuleDO d : serialNumberRuleList) {
            list.add(convert(d));
        }
        return list;
    }

    @Override
    public void updateSerialNumberRule(SerialNumberRule rule) {
        AssertUtil.isNotNull(rule, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotBlank(rule.getSerialType(), BizErrorCode.SERIAL_TYPE_EMPTY);
        AssertUtil.isNotBlank(rule.getSerialType(), BizErrorCode.SERIAL_LENGTH_EMPTY);
        AssertUtil.isNotBlank(rule.getMerchantId(), BizErrorCode.MERCHANT_ID_EMPTY);

        SerialNumberRuleDO serialNumberRuleDO = new SerialNumberRuleDO();
        serialNumberRuleDO.setName(rule.getName());
        serialNumberRuleDO.setFormat(rule.getFormat());
        serialNumberRuleDO.setMerchantId(Long.parseLong(rule.getMerchantId()));
        serialNumberRuleDO.setPrefix(rule.getPrefix());
        serialNumberRuleDO.setSerialLength(rule.getSerialLength());
        serialNumberRuleDO.setSerialType(rule.getSerialType());
        serialNumberRuleDao.update(serialNumberRuleDO);

        //更新缓存
        RedisUtil.hset(Constant.SERIAL_NUMBER_CONF + rule.getMerchantId(), rule.getSerialType(), JSON.toJSONString(rule));
    }

    private SerialNumberRule convert(SerialNumberRuleDO serialNumberRuleDO) {

        if (serialNumberRuleDO == null) {
            return null;
        }
        SerialNumberRule rule = new SerialNumberRule();
        rule.setId("" + serialNumberRuleDO.getId());
        rule.setFormat(serialNumberRuleDO.getFormat());
        rule.setMerchantId("" + serialNumberRuleDO.getMerchantId());
        rule.setName(serialNumberRuleDO.getName());
        rule.setPrefix(serialNumberRuleDO.getPrefix());
        rule.setSerialLength(serialNumberRuleDO.getSerialLength());
        rule.setSerialType(serialNumberRuleDO.getSerialType());
        return rule;
    }

}
