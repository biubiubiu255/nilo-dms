package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dto.order.DelayParam;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.WaybillOptService;
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
    	private WaybillOptService waybillOptService;
    	
        @RequestMapping(value = "/scan.html")
        public String customers() {
            return "mobile/rider/stranded/scan";
        }

        @RequestMapping(value = "/save.html", method = RequestMethod.POST)
        @ResponseBody
          public String save(DelayParam param) {
            try {
                waybillOptService.delay(param);
            } catch (Exception e) {
                return toJsonErrorMsg(e.getMessage());
            }
            return toJsonTrueMsg();
        }
    
}
