package com.nilo.dms.service.order.impl;
import java.util.*;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.enums.DeliveryOrderStatusEnum;
import com.nilo.dms.dao.*;
import com.nilo.dms.dao.dataobject.QO.SendReportQO;
import com.nilo.dms.dto.order.SendOrderParameter;
import com.nilo.dms.service.order.*;
import com.nilo.dms.service.system.SystemCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.dto.order.SendReport;
import com.nilo.dms.dao.dataobject.SendReportDO;

/**
 * Created by ronny on 2017/9/15.
 */
@Service
public class SendReportServiceImpl implements SendReportService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SendReportDao SendReportDao;


    @Override
    public List<SendReportDO> querySendReport(SendReportQO sendReportQO, Pagination page) {

        sendReportQO.setLimit(page.getLimit());
        sendReportQO.setOffset(page.getOffset());

        // 查询记录
        List<SendReportDO> queryList = SendReportDao.querySendReport(sendReportQO);

        Long count = SendReportDao.queryCountBy(sendReportQO);
        page.setTotalCount(count == null ? 0 : count);
        return queryList;
    }

}
