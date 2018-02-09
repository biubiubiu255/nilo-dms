package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.StaffDao;
import com.nilo.dms.dao.ThirdDriverDao;
import com.nilo.dms.dao.ThirdExpressDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.StaffDO;
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.order.DeliveryRouteService;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.DeliveryRoute;
import com.nilo.dms.service.order.model.Loading;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.LoadingController;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



@Controller
@RequestMapping("mobile/basic/route")
public class RouteController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private LoadingService loadingService;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private DeliveryRouteService deliveryRouteService;
    
    
    
    
    @RequestMapping(value = "/homepage.html", method=RequestMethod.GET)
    public String homepage() {
        return "mobile/basic/route/route";
    }

    @RequestMapping(value = "/query.html")
    @ResponseBody
    public String query(String orderNo ) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();

        //DeliveryOrder queryByOrderNo = orderService.queryByOrderNo(merchantId, orderNo);
        
        List<DeliveryRoute> queryRoute = deliveryRouteService.queryRoute(merchantId, orderNo);

        return toJsonTrueData(queryRoute);

    }

}
