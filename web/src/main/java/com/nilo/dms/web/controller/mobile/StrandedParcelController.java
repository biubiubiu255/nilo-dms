package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.service.order.model.AbnormalOrder;
import com.nilo.dms.service.order.model.AbnormalParam;
import com.nilo.dms.web.controller.BaseController;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mobile/rider/stranded")
public class StrandedParcelController extends BaseController {

    	@Autowired
    	private RiderOptService riderOptService;
    	
        @RequestMapping(value = "/scan.html")
        public String customers() {
            return "mobile/rider/stranded/scan";
        }
    	
    	
        
        @RequestMapping(value = "/save.html", method = RequestMethod.POST)
        @ResponseBody
        public String save(@RequestParam("logisticsNo") String orderNo, 
        			@RequestParam("reason") String abnormalType, @RequestParam("memo") String remark) {
        	
            AbnormalOrder abnormalOrder = new AbnormalOrder();
            abnormalOrder.setOrderNo(orderNo);
            abnormalOrder.setAbnormalType(abnormalType);
            abnormalOrder.setRemark(remark);
            
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            try {
                AbnormalParam param = new AbnormalParam();
                abnormalOrder.setCreatedBy(me.getUserId());
                abnormalOrder.setMerchantId(merchantId);
                param.setAbnormalOrder(abnormalOrder);
                param.setOptBy(me.getUserId());
                riderOptService.abnormal(param);

            } catch (Exception e) {
                return toJsonErrorMsg(e.getMessage());
            }
            return toJsonTrueMsg();
        }
    
    
    
}
