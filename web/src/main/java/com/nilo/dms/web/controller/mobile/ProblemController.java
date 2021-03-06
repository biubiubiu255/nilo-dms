package com.nilo.dms.web.controller.mobile;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.AbnormalOrder;
import com.nilo.dms.service.order.model.AbnormalParam;
import com.nilo.dms.web.controller.BaseController;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;




@Controller
@RequestMapping("/mobile/rider/problem")
public class ProblemController extends BaseController {
	
	@Autowired
	private RiderOptService riderOptService;
	
    @RequestMapping(value = "/scan.html")
    public String customers() {
        return "mobile/rider/problem/scan";
    }
	
	
    
    @RequestMapping(value = "/save.html", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam("logisticsNo") String orderNo, 
    			@RequestParam("reason") String reason, @RequestParam("memo") String remark) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        try {
            AbnormalParam param = new AbnormalParam();
            param.setMerchantId(merchantId);
            param.setOptBy(me.getUserId());
            param.setRemark(remark);
            param.setOrderNo(orderNo);
            param.setReason(reason);
            riderOptService.refuse(param);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
    
    
}
