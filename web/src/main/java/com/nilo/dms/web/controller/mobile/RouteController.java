package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dto.order.DeliveryRoute;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("mobile/basic/route")
public class RouteController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private LoadingService loadingService;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private DeliveryRouteService deliveryRouteService;


    @RequestMapping(value = "/route.html", method = RequestMethod.GET)
    public String homepage() {
        return "mobile/basic/route/route";
    }

    @RequestMapping(value = "/query.html")
    @ResponseBody
    public String query(String orderNo) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        List<DeliveryRoute> queryRoute = deliveryRouteService.queryRoute(merchantId, orderNo);

        return toJsonTrueData(queryRoute);

    }

}
