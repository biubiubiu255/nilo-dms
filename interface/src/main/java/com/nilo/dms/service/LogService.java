package com.nilo.dms.service;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.common.Log;

import java.util.List;

/**
 * Created by ronny on 2017/9/18.
 */
public interface LogService {

    void saveLog(Log log);

    Log queryById(String logId);

    List<Log> queryLogByPage(String merchantId, String operation, String operator, String parameter, Long beginTime, Long endTime, Pagination pagination);
}
