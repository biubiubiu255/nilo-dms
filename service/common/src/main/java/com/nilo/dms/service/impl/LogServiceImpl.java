package com.nilo.dms.service.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.LogDao;
import com.nilo.dms.dao.dataobject.LogDO;
import com.nilo.dms.dto.common.Log;
import com.nilo.dms.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronny on 2017/9/18.
 */
@Service
public class LogServiceImpl implements LogService {


    @Autowired
    private LogDao logDao;

    @Override
    public void saveLog(Log log) {
        LogDO logDO = convertToLogDO(log);
        logDao.insert(logDO);
    }

    @Override
    public Log queryById(String logId) {
        LogDO logDO = logDao.queryById(Long.parseLong(logId));

        return logDO == null ? null : convertToLog(logDO);
    }

    @Override
    public List<Log> queryLogByPage(String merchantId, String operation, String operator, String parameter, Long beginTime, Long endTime, Pagination pagination) {

        List<LogDO> queryList = logDao.queryBy(Long.parseLong(merchantId), operation, operator, parameter, beginTime, endTime, pagination.getOffset(), pagination.getLimit());
        Long count = logDao.queryCountBy(Long.parseLong(merchantId), operation, operator, parameter, beginTime, endTime);
        pagination.setTotalCount(count == null ? 0 : count);
        List<Log> list = new ArrayList<>();
        if (queryList != null) {
            for (LogDO d : queryList) {
                list.add(convertToLog(d));
            }
        }
        return list;
    }


    private LogDO convertToLogDO(Log log) {
        LogDO logDO = new LogDO();
        logDO.setMerchantId(Long.parseLong(log.getMerchantId()));
        logDO.setOperation(log.getOperation());
        logDO.setContent(log.getContent());
        logDO.setException(log.getException());
        logDO.setOperator(log.getOperator());
        logDO.setParameter(log.getParameter());
        logDO.setIp(log.getIp());
        return logDO;
    }

    private Log convertToLog(LogDO l) {
        Log log = new Log();
        log.setId("" + l.getId());
        log.setMerchantId("" + l.getMerchantId());
        log.setOperation(l.getOperation());
        log.setContent(l.getContent());
        log.setException(l.getException());
        log.setOperator(l.getOperator());
        log.setParameter(l.getParameter());
        log.setIp(l.getIp());
        log.setCreatedTime(l.getCreatedTime());
        return log;
    }
}