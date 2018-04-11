package com.nilo.dms.web.controller.report;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/11/20.
 */
@Controller
@RequestMapping("/report")
public class PickupReportController extends BaseController {


    @RequestMapping(value = "/pickupReport.html", method = RequestMethod.GET)
    public String list(Model model) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        model.addAttribute("list", getRiderList());
        return "report/pick_up";
    }

    @ResponseBody
    @RequestMapping(value = "/pickUpList.html")
    public String getOrderList(String rider, String fromTime, String toTime) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        if (StringUtil.isEmpty(fromTime) || StringUtil.isEmpty(fromTime)) {
            if (StringUtil.isEmpty(fromTime)) {
                fromTime = toTime;
            } else {
                toTime = fromTime;
            }
        }
        Long fromTimeL = 0l;
        if (StringUtil.isNotEmpty(fromTime)) {
            fromTimeL = DateUtil.parse(fromTime, "yyyy-MM-dd");
        }
        Long toTimeL = 0l;
        if (StringUtil.isNotEmpty(toTime)) {
            toTimeL = DateUtil.parse(toTime, "yyyy-MM-dd") + 24 * 60 * 60 - 1;
        }

        Pagination pagination = getPage();

        return toPaginationLayUIData(pagination, null);
    }
}
