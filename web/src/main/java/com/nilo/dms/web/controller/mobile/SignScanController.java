package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.SignForOrderParam;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/SignScanController")
public class SignScanController extends BaseController {
    @Autowired
    private RiderOptService riderOptService;

    @RequestMapping(value = "/test.html")
    @ResponseBody
    public String test(String logisticsNo, String signer, String remark) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            SignForOrderParam param = new SignForOrderParam();
            param.setMerchantId(merchantId);
            param.setOptBy(me.getUserId());
//            param.setOrderNo(orderNo);
//            param.setRemark(remark);
//            param.setSignBy(signBy);
            param.setOrderNo(logisticsNo);
            param.setSignBy(signer);
            param.setRemark(remark);
            riderOptService.signForOrder(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }

        return "true";
    }
}
