package com.nilo.dms.web.controller.log;


import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.order.OrderOptLogService;
import com.nilo.dms.service.order.model.DeliveryOrderOpt;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/order/log")
public class OrderLogController extends BaseController {

    @Autowired
    private OrderOptLogService orderOptLogService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "log/order/list";
    }

    @RequestMapping("/view.html")
    public String edit(Model model, String id) {
        if (StringUtil.isNotEmpty(id)) {
        }
        return "log/order/view";
    }

    @ResponseBody
    @RequestMapping("/getLogList.html")
    public String getUserList(String orderNo, String fromTime, String toTime) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Long fromTimeLong = null, toTimeLong = null;
        if (StringUtil.isNotEmpty(fromTime)) {
            fromTimeLong = DateUtil.parse(fromTime, "yyyy-MM-dd");
        }
        if (StringUtil.isNotEmpty(toTime)) {
            toTimeLong = DateUtil.parse(toTime, "yyyy-MM-dd") + 24 * 60 * 60 - 1;
        }
        Pagination page = getPage();
        List<DeliveryOrderOpt> list = orderOptLogService.queryBy(merchantId, null, orderNo, null, fromTimeLong, toTimeLong, page);
        return toPaginationLayUIData(page, list);
    }

}
