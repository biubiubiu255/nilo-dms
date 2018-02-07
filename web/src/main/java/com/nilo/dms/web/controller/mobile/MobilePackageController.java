package com.nilo.dms.web.controller.mobile;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nilo.dms.common.Principal;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.PackageController.NextStation;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/11/22.
 */
@Controller
@RequestMapping("/mobile/package")
public class MobilePackageController  extends BaseController {
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/packing.html")
    public String toPackage(Model model) {
    	Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));
        List<NextStation> list = new ArrayList<>();

        for(DistributionNetworkDO n:networkDOList) {
            NextStation s = new NextStation();
            s.setCode("" + n.getId());
            s.setName(n.getName());
            list.add(s);
        }
        
        model.addAttribute("nextStation",list);
        
        return "mobile/network/package/packing";
    }

    @RequestMapping(value = "/submit.html")
    @ResponseBody
    public String submit(String scaned_codes[],String nextStation,String weight,String length,String width,String high ) {

        for (int i=0;i<scaned_codes.length;i++){
            System.out.println(scaned_codes[i]);
        }
        System.out.println(nextStation);
        System.out.println(weight);
        System.out.println(length);
        System.out.println(width);
        System.out.println(high);

        return "true";
    }
}
