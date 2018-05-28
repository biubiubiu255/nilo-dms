package com.nilo.dms.service.system;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dto.system.LogConfig;
import com.nilo.dms.dto.system.MerchantConfig;
import com.nilo.dms.dto.system.OrderHandleConfig;
import com.nilo.dms.dto.system.SerialNumberRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ronny on 2017/8/30.
 */
public class SystemConfig {

    /**
     * 客户配置
     */
    public static MerchantConfig getMerchantConfig(String merchantId) {
        MerchantConfig config = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + merchantId), MerchantConfig.class);
        return config;
    }
    public static MerchantConfig getMerchantConfigByCode(String code) {
        MerchantConfig config = JSON.parseObject(RedisUtil.get(Constant.MERCHANT_CONF + code), MerchantConfig.class);
        return config;
    }
    /**
     * 日志配置
     */
    public static List<LogConfig> getLogConfig() {
        Set<String> set = RedisUtil.smembers(Constant.LOG_CONF);
        List<LogConfig> list = new ArrayList<>();
        for (String s : set) {
            list.add(JSON.parseObject(s, LogConfig.class));
        }

        return list;
    }

    /**
     * SerialNumberRule配置
     */
    public static String getNextSerialNo(String merchantId, String serialType) {

        SerialNumberRule rule = JSON.parseObject(RedisUtil.hget(Constant.SERIAL_NUMBER_CONF + merchantId, serialType), SerialNumberRule.class);
        if (rule == null) {
            throw new IllegalArgumentException("MerchantId :" + merchantId + " ,serialType:" + serialType + " is not right.");
        }
        String formatDate = "";
        if (StringUtil.isNotBlank(rule.getFormat())) {
            formatDate = DateUtil.formatCurrent(rule.getFormat());
        }
        // serial number key
        String key = merchantId + "_" + serialType + formatDate;
        Long serialNumber = RedisUtil.increment(key);
        return rule.getPrefix() + formatDate + String.format("%0" + rule.getSerialLength() + "d", serialNumber);
    }

    public static OrderHandleConfig getOrderHandleConfig(String merchantId, String optType) {
        OrderHandleConfig orderHandleConfig = JSON.parseObject(RedisUtil.hget(Constant.ORDER_HANDLE_CONF + merchantId, optType), OrderHandleConfig.class);
        return orderHandleConfig;
    }

}
