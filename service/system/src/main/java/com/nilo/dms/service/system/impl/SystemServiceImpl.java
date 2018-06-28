package com.nilo.dms.service.system.impl;


import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.InterfaceStatusEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.MerchantConfigDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.MerchantConfigDO;
import com.nilo.dms.dto.system.*;
import com.nilo.dms.service.system.DictionaryService;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.service.system.SendMessageService;
import com.nilo.dms.service.system.config.*;
import com.nilo.dms.service.system.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by ronny on 2017/8/30.
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private MerchantConfigDao merchantConfigDao;
    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private LogConfigService logConfigService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private InterfaceConfigService interfaceConfigService;

    @Autowired
    private OrderHandleConfigService orderHandleConfigService;

    @Autowired
    private SendMessageService smsConfigService;

    @Autowired
    private RouteConfigService routeConfigService;

    @Autowired
    private BizFeeConfigService bizFeeConfigService;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Override
    public void loadingAndRefreshCustomerConfig() {

        List<MerchantConfigDO> list = list = merchantConfigDao.findAll();

        if (list == null || list.size() == 0) return;

        String key = Constant.MERCHANT_CONF + "1*";
        Set<String> keys = RedisUtil.keys(key);
        if (keys != null) {
            for (String k : keys) {
                RedisUtil.del(k);
            }
        }
        for (MerchantConfigDO c : list) {
            RedisUtil.set(Constant.MERCHANT_CONF + c.getMerchantCode(), JSON.toJSONString(convertTo(c)));
            RedisUtil.set(Constant.MERCHANT_CONF + c.getMerchantId(), JSON.toJSONString(convertTo(c)));
            List<InterfaceConfig> configList = interfaceConfigService.queryAll("" + c.getMerchantId());
            if (configList == null) continue;
            for (InterfaceConfig config : configList) {
                if (config.getStatus().getCode().equals(InterfaceStatusEnum.DISABLED.getCode())) {
                    continue;
                }
                RedisUtil.hset(Constant.INTERFACE_CONF + c.getMerchantId(), config.getMethod(), JSON.toJSONString(config));
            }
        }

    }

    @Override
    public void loadingAndRefreshNetwork() {
        List<DistributionNetworkDO> list = distributionNetworkDao.findAll();

        for (DistributionNetworkDO r : list) {
            RedisUtil.hset(Constant.NETWORK_INFO + r.getMerchantId(), "" + r.getId(), JSON.toJSONString(r));
        }
    }

    @Override
    public void loadingAndRefreshSerialNumRule() {

        List<SerialNumberRule> list = serialNumberService.findAll();

        if (list == null || list.size() == 0) return;

        Set<String> keys = RedisUtil.keys(Constant.SERIAL_NUMBER_CONF + "1*");
        if (keys != null) {
            for (String k : keys) {
                RedisUtil.del(k);
            }
        }

        for (SerialNumberRule r : list) {
            RedisUtil.hset(Constant.SERIAL_NUMBER_CONF + r.getMerchantId(), r.getSerialType(), JSON.toJSONString(r));
        }
    }

    @Override
    public void loadingAndRefreshLogConfig() {

        Set<String> keys = RedisUtil.keys(Constant.LOG_CONF);
        if (keys != null) {
            for (String k : keys) {
                RedisUtil.del(k);
            }
        }
        List<LogConfig> list = logConfigService.queryAll();
        if (list == null || list.size() == 0) return;
        for (LogConfig c : list) {
            RedisUtil.sadd(Constant.LOG_CONF, JSON.toJSONString(c));
        }
    }

    @Override
    public void loadingAndRefreshSystemCode() {

        List<Dictionary> list = dictionaryService.queryAll();

        if (list == null || list.size() == 0) return;

        Set<String> keys = RedisUtil.keys("System_code"  + "1*");
        for (String k : keys) {
            RedisUtil.del(k);
        }

        for (Dictionary r : list) {
            RedisUtil.sadd("System_code" + r.getMerchantId() + r.getType(), JSON.toJSONString(r));
        }
    }

    @Override
    public void loadingAndRefreshOrderHandleConfig() {
        List<OrderHandleConfig> list = orderHandleConfigService.queryAll();
        if (list == null || list.size() == 0) return;

        Set<String> keys = RedisUtil.keys(Constant.ORDER_HANDLE_CONF + "1*");
        if (keys != null) {
            for (String k : keys) {
                RedisUtil.del(k);
            }
        }

        for (OrderHandleConfig r : list) {
            RedisUtil.hset(Constant.ORDER_HANDLE_CONF + r.getMerchantId(), r.getOptType(), JSON.toJSONString(r));
        }
    }

    @Override
    public void loadingAndRefreshSMSConfig() {

        List<SMSConfig> list = smsConfigService.listConfigBy(null);
        if (list == null || list.size() == 0) return;

        Set<String> keys = RedisUtil.keys(Constant.SMS_CONF + "1*");
        if (keys != null) {
            for (String k : keys) {
                RedisUtil.del(k);
            }
        }
        for (SMSConfig c : list) {
            RedisUtil.hset(Constant.SMS_CONF + c.getMerchantId(), c.getSmsType(), JSON.toJSONString(c));
        }
    }

    @Override
    public void loadingAndRefreshRouteConfig() {
        List<RouteConfig> list = routeConfigService.queryAll();

        if (list == null || list.size() == 0) return;

        Set<String> keys = RedisUtil.keys(Constant.ROUTE_CONF  + "1*");
        if (keys != null) {
            for (String k : keys) {
                RedisUtil.del(k);
            }
        }

        for (RouteConfig c : list) {
            RedisUtil.hset(Constant.ROUTE_CONF + c.getMerchantId(), c.getOptType(), JSON.toJSONString(c));
        }
    }

    @Override
    public void loadingAndRefreshBizFeeConfig() {
        List<BizFeeConfig> list =bizFeeConfigService.queryAll();

        if (list == null || list.size() == 0) return;

        Set<String> keys = RedisUtil.keys(Constant.BIZ_FEE_CONF +  "1*");
        if (keys != null) {
            for (String k : keys) {
                RedisUtil.del(k);
            }
        }

        for (BizFeeConfig c : list) {
            RedisUtil.hset(Constant.BIZ_FEE_CONF + c.getMerchantId(), c.getOptType(), JSON.toJSONString(c));
        }
    }

    @Override
    public void loadingAndRefreshAddressConfig() {

        String[] countrys = new String[]{"CN", "KE"};

        try {
            for (String c : countrys) {
                InputStream in = this.getClass().getResourceAsStream("/" + c + ".json");
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                String result = "";
                String line = null;
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                br.close();
                List<AddressNode> address = JSON.parseArray(result, AddressNode.class);
                for (AddressNode n : address) {
                    parsAddress(n, c);
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void parsAddress(AddressNode addressNode, String country) {
        if (addressNode.getChilds() != null) {
            for (AddressNode node : addressNode.getChilds()) {
                parsAddress(node, country);
            }
        }
        RedisUtil.hset(Constant.ADDRESS + country, addressNode.code, addressNode.getName());

    }


    private MerchantConfig convertTo(MerchantConfigDO configDO) {
        if (configDO == null) {
            return null;
        }
        MerchantConfig config = new MerchantConfig();
        config.setKey(configDO.getKey());
        config.setMerchantId("" + configDO.getMerchantId());
        config.setMerchantCode(configDO.getMerchantCode());
        return config;
    }


    private static class AddressNode {

        private List<AddressNode> childs;

        private String code;

        private String name;

        public List<AddressNode> getChilds() {
            return childs;
        }

        public void setChilds(List<AddressNode> childs) {
            this.childs = childs;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
