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

@Controller
@RequestMapping("/mobile/network/unpackage")
public class UnpackController extends BaseController  {
	
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @RequestMapping(value = "/unpack.html")
    public String toPackage(Model model) {
    	Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DistributionNetworkDO> networkDOList = distributionNetworkDao.findAllBy(Long.parseLong(merchantId));
        List<NextStation> list = new ArrayList<>();

        for(DistributionNetworkDO n:networkDOList){
            NextStation s = new NextStation();
            s.setCode(""+n.getId());
            s.setName(n.getName());
            list.add(s);
        }
        
        model.addAttribute("nextStation",list);
        
        return "mobile/network/unpackage/unpack";
        
    }
    
}
