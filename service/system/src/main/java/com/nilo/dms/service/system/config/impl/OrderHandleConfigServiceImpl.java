package com.nilo.dms.service.system.config.impl;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.OrderHandleConfigDao;
import com.nilo.dms.dao.dataobject.OrderHandleConfigDO;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.config.OrderHandleConfigService;
import com.nilo.dms.service.system.model.OrderHandleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/13.
 */
@Service
public class OrderHandleConfigServiceImpl implements OrderHandleConfigService {

    @Autowired
    private OrderHandleConfigDao orderHandleConfigDao;

    @Override
    public List<OrderHandleConfig> queryAll() {
        List<OrderHandleConfigDO> queryList = orderHandleConfigDao.findAll();
        List<OrderHandleConfig> list = new ArrayList<>();
        if (queryList == null) return list;

        for (OrderHandleConfigDO configDO : queryList) {
            list.add(convert(configDO));
        }
        return list;
    }

    @Override
    public List<OrderHandleConfig> queryAllBy(String merchantId) {
        List<OrderHandleConfigDO> queryList = orderHandleConfigDao.findAllBy(Long.parseLong(merchantId));
        List<OrderHandleConfig> list = new ArrayList<>();
        if (queryList == null) return list;

        for (OrderHandleConfigDO configDO : queryList) {
            list.add(convert(configDO));
        }
        return list;
    }

    @Override
    public void addConfig(OrderHandleConfig config) {
        AssertUtil.isNotNull(config, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotBlank(config.getOptType(), SysErrorCode.REQUEST_IS_NULL);

        OrderHandleConfigDO query = orderHandleConfigDao.findBy(Long.parseLong(config.getMerchantId()), config.getOptType());
        if (query != null) {
            throw new DMSException(BizErrorCode.HANDLE_TYPE_CONFIG_EXIST, config.getOptType());
        }

        OrderHandleConfigDO configDO = convert(config);
        orderHandleConfigDao.insert(configDO);
        //刷新缓存
        refreshCache(config);
    }

    @Override
    public void updateHandleConfig(OrderHandleConfig config) {
        AssertUtil.isNotNull(config, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotBlank(config.getOptType(), SysErrorCode.REQUEST_IS_NULL);

        OrderHandleConfigDO configDO = convert(config);
        orderHandleConfigDao.update(configDO);
        //刷新缓存
        refreshCache(config);
    }

    @Override
    public OrderHandleConfig queryBy(String merchantId, String optType) {
        OrderHandleConfigDO configDO = orderHandleConfigDao.findBy(Long.parseLong(merchantId), optType);
        if (configDO == null) return null;
        return convert(configDO);
    }

    private OrderHandleConfig convert(OrderHandleConfigDO configDO) {

        OrderHandleConfig config = new OrderHandleConfig();

        config.setOptType(configDO.getOptType());
        config.setMerchantId("" + configDO.getMerchantId());
        config.setUpdateStatus(configDO.getUpdateStatus());
        config.setClassName(configDO.getClassName());
        if (StringUtil.isNotEmpty(configDO.getAllowStatus())) {
            List<Integer> allowStatusList = new ArrayList<>();
            String[] allow = configDO.getAllowStatus().split(",");
            if (allow != null) {
                for (String s : allow) {
                    allowStatusList.add(Integer.parseInt(s));
                }
            }
            config.setAllowStatus(allowStatusList);
        }
        if (StringUtil.isNotEmpty(configDO.getNotAllowStatus())) {
            List<Integer> notAllowStatuslist = new ArrayList<>();
            String[] notAllow = configDO.getNotAllowStatus().split(",");
            if (notAllow != null) {
                for (String s : notAllow) {
                    notAllowStatuslist.add(Integer.parseInt(s));
                }
            }
            config.setNotAllowStatus(notAllowStatuslist);
        }

        return config;

    }

    private OrderHandleConfigDO convert(OrderHandleConfig config) {

        OrderHandleConfigDO configDO = new OrderHandleConfigDO();

        configDO.setOptType(config.getOptType());
        configDO.setMerchantId(Long.parseLong(config.getMerchantId()));
        configDO.setUpdateStatus(config.getUpdateStatus());
        configDO.setClassName(config.getClassName());
        if (config.getAllowStatus() != null && config.getAllowStatus().size() > 0) {
            String allowStatus = "";
            for (Integer s : config.getAllowStatus()) {
                allowStatus = allowStatus + s + ",";
            }
            configDO.setAllowStatus(allowStatus);
        }
        if (config.getNotAllowStatus() != null && config.getNotAllowStatus().size() > 0) {
            String notAllowStatus = "";
            for (Integer s : config.getNotAllowStatus()) {
                notAllowStatus = notAllowStatus + s + ",";
            }
            configDO.setNotAllowStatus(notAllowStatus);
        }

        return configDO;

    }

    private void refreshCache(OrderHandleConfig config) {
        RedisUtil.hset(Constant.ORDER_HANDLE_CONF + config.getMerchantId(), config.getOptType(), JSON.toJSONString(config));
    }
}
