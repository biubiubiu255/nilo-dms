package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DeliveryFeeDetailsDao;
import com.nilo.dms.dao.dataobject.DeliveryFeeDetailsDO;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/20.
 */
@Controller
@RequestMapping("/report")
public class DeliveryFeeReportController extends BaseController {

    @Autowired
    private DeliveryFeeDetailsDao deliveryFeeDetailsDao;


    @RequestMapping(value = "/deliveryFeeReport.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "report/delivery_fee";
    }

    @ResponseBody
    @RequestMapping(value = "/deliveryFeeList.html")
    public String getOrderList(String orderNo, String bizType, String fromTime, String toTime) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        if (StringUtil.isEmpty(fromTime) || StringUtil.isEmpty(fromTime)) {
            if (StringUtil.isEmpty(fromTime)) {
                fromTime = toTime;
            } else {
                toTime = fromTime;
            }
        }
        Long fromTimeL = null;
        if (StringUtil.isNotEmpty(fromTime)) {
            fromTimeL = DateUtil.parse(fromTime, "yyyy-MM-dd");
        }
        Long toTimeL = null;
        if (StringUtil.isNotEmpty(toTime)) {
            toTimeL = DateUtil.parse(toTime, "yyyy-MM-dd") + 24 * 60 * 60 - 1;
        }

        Pagination pagination = getPage();
        List<DeliveryFeeDetailsDO> list = new ArrayList<>();Long count = deliveryFeeDetailsDao.queryCountBy(Long.parseLong(merchantId), bizType, orderNo, fromTimeL, toTimeL);
        if (count == 0) return toPaginationLayUIData(pagination, list);

        list = deliveryFeeDetailsDao.queryBy(Long.parseLong(merchantId), bizType, orderNo, fromTimeL, toTimeL, pagination.getOffset(), pagination.getLimit());
        return toPaginationLayUIData(pagination, list);
    }
}
