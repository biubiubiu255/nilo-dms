package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.WaybillLogDao;
import com.nilo.dms.dto.order.Loading;
import com.nilo.dms.dto.order.ShipParameter;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.LoadingService;
import com.nilo.dms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mobile/deliver")
public class DeliverScanController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private LoadingService loadingService;
    @Autowired
    private WaybillLogDao waybillLogDao;

    @RequestMapping(value = "/scan.html")
    public String toPage(Model model, HttpServletRequest request) {
        model.addAttribute("riderList", getRiderList());
        return "mobile/network/deliver_scan/deliverScan";
    }

    @ResponseBody
    @RequestMapping(value = "/check.html")
    public String check(String code) {

        Long a = waybillLogDao.getStateByOrderNo(code);
        if (a == null) {
            return toJsonErrorMsg("There is no OrderNo");
        }
        if (!(a == 20)) {
            return toJsonErrorMsg(BizErrorCode.ORDER_NO_ARRIVE.getDescription());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/submit.html")
    @ResponseBody
    public String submit(String scaned_codes[], String rider, String logisticsNo) {
        Loading loading = new Loading();
        loading.setRider(rider);

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        String loadingNo = "";
        try {
            if (StringUtil.isEmpty(rider)) {
                throw new IllegalArgumentException("PdaRider or Driver is empty.");
            }
            loading.setMerchantId(merchantId);
            loading.setLoadingBy(me.getUserId());
            if (StringUtil.isNotEmpty(rider)) {
                loading.setRider(rider);
            }
            loadingNo = loadingService.addLoading(loading);
        } catch (Exception e) {
            log.error("addLoading failed. loading:{}", loading, e);
            return toJsonErrorMsg(e.getMessage());
        }

        Waybill order = null;
        for (int i = 0; i < scaned_codes.length; i++) {
            try {
                loadingService.loadingScan(merchantId, loadingNo, scaned_codes[i], me.getUserId());
                //order = orderService.queryByOrderNo(merchantId, scaned_codes);

            } catch (Exception e) {
                log.error("loadingScan failed. orderNo:{}", scaned_codes[i], e);
                return toJsonErrorMsg(e.getMessage());
            }

        }

        ShipParameter parameter = new ShipParameter();
        parameter.setMerchantId(merchantId);
        parameter.setOptBy(me.getUserId());
        parameter.setLoadingNo(loadingNo);
        parameter.setNetworkId("" + me.getNetworks().get(0));
        loadingService.ship(parameter);
        return toJsonTrueMsg();

    }

}
