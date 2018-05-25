package com.nilo.dms.service.order.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.dao.WaybillCodDao;
import com.nilo.dms.dao.dataobject.ReportCodDO;
import com.nilo.dms.service.order.ReportService;
import com.nilo.dms.dto.order.ReportCodQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wenzhuo-company on 2018/3/29.
 */

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private WaybillCodDao waybillCodDao;

    @Override
    public List<ReportCodDO> qeueryCodList(ReportCodQuery reportCodQuery, Pagination page) {

        reportCodQuery.setLimit(page.getLimit());
        reportCodQuery.setOffset(page.getOffset());
        if(reportCodQuery.getOut_warm()==null){
            reportCodQuery.setOut_warm(1);
            //设置默认的预警时间
        }
        List<ReportCodDO> list = waybillCodDao.queryReportCod(reportCodQuery);
        for (int i=0;i<list.size();i++){
            ReportCodDO reportCodDO = list.get(i);
            reportCodDO.setOrder_status(reportCodDO.getOrder_status_desc());
            list.set(i, reportCodDO);
        }
        page.setTotalCount(waybillCodDao.queryReportCodCount(reportCodQuery));
        System.out.println("查询总条数 = " + "========================================" + page.getTotalCount());
        return list;
    }
}
