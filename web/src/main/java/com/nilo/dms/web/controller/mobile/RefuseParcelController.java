package com.nilo.dms.web.controller.mobile;

import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dto.handle.HandleRefuse;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/mobile/rider/refuse")
public class RefuseParcelController extends BaseController {

    	@Autowired
    	private WaybillOptService waybillOptService;

        @Autowired
        private UserService userService;

        @RequestMapping(value = "/scan.html")
        public String customers(Model model) {
            List<ThirdExpressDO> expressList = userService.findExpressesAll(getPage());
            model.addAttribute("expressList", expressList);
            return "mobile/rider/refuse/scan";
        }

        @RequestMapping(value = "/save.html", method = RequestMethod.POST)
        @ResponseBody
          public String save(HandleRefuse handleRefuse) {
            Principal me = SessionLocal.getPrincipal();

            handleRefuse.setHandleName(me.getUserName());
            handleRefuse.setHandleBy(Long.parseLong(me.getUserId()));
            waybillOptService.refuse(handleRefuse);
            return toJsonTrueMsg();
        }
    
}
