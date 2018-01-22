package com.nilo.dms.service.system.config.impl;

import com.nilo.dms.dao.LogConfigDao;
import com.nilo.dms.dao.dataobject.LogConfigDO;
import com.nilo.dms.service.system.config.LogConfigService;
import com.nilo.dms.service.system.model.LogConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/25.
 */
@Service
public class LogConfigServiceImpl implements LogConfigService {


    @Autowired
    private LogConfigDao logConfigDao;

    @Override
    public List<LogConfig> queryAll() {
        List<LogConfigDO> queryList = logConfigDao.queryAll();
        List<LogConfig> list = new ArrayList<>();
        if (queryList == null) return list;
        for (LogConfigDO c : queryList) {
            LogConfig config = convertToLogConfig(c);
            list.add(config);
        }
        return list;
    }

    private LogConfig convertToLogConfig(LogConfigDO logConfigDO) {
        LogConfig config = new LogConfig();
        config.setOperation(logConfigDO.getOperation());
        config.setUrl(logConfigDO.getUrl());
        return config;
    }
}
