package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dto.order.DelayParam;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.RiderOptService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
/*        public String save(@RequestParam("logisticsNo") String orderNo, 
        			@RequestParam("reason") String abnormalType, @RequestParam("memo") String remark) {*/
          public String save(DelayParam param) {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            try {
                param.setOptBy(me.getUserId());
                param.setMerchantId(merchantId);
                riderOptService.delay(param);
            } catch (Exception e) {
                return toJsonErrorMsg(e.getMessage());
            }
            return toJsonTrueMsg();
        }
    
}
