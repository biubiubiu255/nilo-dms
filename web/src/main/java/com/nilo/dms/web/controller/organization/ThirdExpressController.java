package com.nilo.dms.web.controller.organization;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.RoleStatusEnum;
import com.nilo.dms.common.enums.UserStatusEnum;
import com.nilo.dms.common.enums.UserTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.LoginInfo;
import com.nilo.dms.service.model.Role;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.model.test.Express;
import com.nilo.dms.service.system.DistributionNetworkService;
import com.nilo.dms.service.system.model.DistributionNetwork;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

@Controller
@RequestMapping("/admin/express")
public class ThirdExpressController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserNetworkDao userNetworkDao;

    @Autowired
    private DistributionNetworkService distributionNetworkService;

    
    
    @ResponseBody
    @RequestMapping(value = "/expressList.html", method = RequestMethod.POST)
    public String expressList(ThirdExpressDO express) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();  //主要的，主体
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<ThirdExpressDO> list  = new ArrayList<ThirdExpressDO>();
        //System.out.println(express.getExpressCode());
        if (express.getExpressName()==null || express.getExpressName().equals("")) {
        	list = userService.findExpressesAll(page);
		}else {
			list = userService.findUserPageByExpresses(merchantId, express, page);
		}
        return toPaginationLayUIData(page, list);  //Pagination 页码
    }
     

    
    
    @RequestMapping(value = "/expressList.html", method = RequestMethod.GET)
    public String expressList(Model model) {
        return "express/expressList";
    }
    
    /*    二级分割线        */
    
    @RequestMapping(value = "/addExpressPage.html", method = RequestMethod.GET)
    public String addExpressPage(Model model) {

        return "express/addExpress";
    }
    
    
    //添加用户参数，以及写入
    @ResponseBody
    @RequestMapping(value = "/addExpressInfo.html", method = RequestMethod.POST)
    public String addExpressInfo(ThirdExpressDO express) {
    //public void Test_addExpressInfo(@RequestParam("express_name") String eName, @RequestParam("express_code") String eCode) {

        Pagination page = getPage();
        //page.setTotalCount(commonDao.lastFoundRows());
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        express.setMerchantId(Long.valueOf(me.getMerchantId()));
    	userService.addExpress(express);
 
        //System.out.println(express.getExpressName());
        
    	return toJsonTrueMsg();
    }
    
    
    //更新方法
    @RequestMapping(value = "/editExpressInfoPage.html", method = RequestMethod.GET)
    public String editExpressInfoPage(ThirdExpressDO express, Model model) {
    	
    	//express.setCreatedTime(Long.valueOf(System.currentTimeMillis()));

    	//Pagination page = getPage();
    	
    	//List<ThirdExpressDO> list = new ArrayList<ThirdExpressDO>();
    	
    	//list = userService.findUserPageByExpresses(null, express, page);
    	
    	model.addAttribute("resultDate", express);
    	
    	//model.addAttribute("nowDate", list);
    	
    	return "express/editExpressPage";
    }
    
    @ResponseBody
    @RequestMapping(value = "/editExpressInfo.html", method = RequestMethod.POST)
    public String editExpressInfo(ThirdExpressDO express) {
    	
    	express.setCreatedTime(Long.valueOf(System.currentTimeMillis()));

    	userService.updateExpress(express);
    	 
        //System.out.println(express.getExpressName());
    	
    	return toJsonTrueMsg();

    }

    
    //删除方法
    @ResponseBody
    @RequestMapping(value = "/deleExpressInfo.html", method = RequestMethod.POST)
    public String deleExpressInfo(ThirdExpressDO express) {
    	
    	express.setCreatedTime(Long.valueOf(System.currentTimeMillis()));

    	userService.deleteExpress(express);
 
        //System.out.println(express.getExpressName());
    	
    	return toJsonTrueMsg();
    	
    }
    
}
