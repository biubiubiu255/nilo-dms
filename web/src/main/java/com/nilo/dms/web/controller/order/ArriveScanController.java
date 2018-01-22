package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.utils.DateUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.service.order.OrderOptLogService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.*;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/arriveScan")
public class ArriveScanController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderOptLogService orderOptLogService;

    @Autowired
    private OrderService orderService;


    @RequestMapping(value = "/arriveScanPage.html", method = RequestMethod.GET)
    public String arriveScanPage(Model model) {
        return "arrive_scan/arrive_scan";
    }


    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        return "arrive_scan/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(String orderNo, String fromTime, String toTime) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        Long fromTimeL = null;
        if (StringUtil.isNotEmpty(fromTime)) {
            fromTimeL = DateUtil.parse(fromTime, "yyyy-MM-dd");
        }
        Long toTimeL = null;
        if (StringUtil.isNotEmpty(toTime)) {
            toTimeL = DateUtil.parse(toTime, "yyyy-MM-dd") + 24 * 60 * 60 - 1;
        }
        List<DeliveryOrderOpt> list = orderOptLogService.queryBy(merchantId, OptTypeEnum.ARRIVE_SCAN.getCode(), orderNo, me.getUserId(), fromTimeL, toTimeL, page);
        return toPaginationLayUIData(page, list);
    }

    @ResponseBody
    @RequestMapping(value = "/arrive.html")
    public String arrive(@RequestParam(value = "orderNos[]", required = false) String[] orderNos) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            OrderOptRequest optRequest = new OrderOptRequest();
            optRequest.setMerchantId(merchantId);
            optRequest.setOptBy(me.getUserId());
            optRequest.setOptType(OptTypeEnum.ARRIVE_SCAN);
            //参数
            Map<String, String> args = new HashMap<>();
            args.put("0", "Test Narobi");
            args.put("1", me.getUserName());
            optRequest.setParams(args);
            optRequest.setOrderNo(Arrays.asList(orderNos));
            orderService.handleOpt(optRequest);

        } catch (Exception e) {
            log.error("arrive failed. orderNos:{}", orderNos, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();

    }
}
