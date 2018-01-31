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
import com.nilo.dms.dao.dataobject.ThirdDriverDO;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.LoginInfo;
import com.nilo.dms.service.model.Role;
import com.nilo.dms.service.model.User;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.model.test.Express;
import com.nilo.dms.service.org.ThirdDriverService;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

@Controller
@RequestMapping("/admin/driver")
public class ThirdDriverController extends BaseController {

    @Autowired
    private ThirdDriverService thirdDriverService;

    
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "driver/list";
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getList(ThirdDriverDO driver) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();  //主要的，主体
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<ThirdDriverDO> list  = new ArrayList<ThirdDriverDO>();
        //System.out.println(express.getExpressCode());
        if (driver.getDriverName()==null || driver.getDriverName().equals("")) {
        	list = thirdDriverService.findDriverAll(page, driver);
		}else {
			thirdDriverService.findByDriverCode(driver.getThirdExpressCode(), page);
		}
        return toPaginationLayUIData(page, list);  //Pagination 页码
    }
    
    /*    二级分割线        */
    
    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String addPage(Model model) {
    	
    	Pagination page = getPage();
    	
    	//ThirdDriverDO driver = new ThirdDriverDO();
    	
        List<ThirdExpressDO> list  = new ArrayList<ThirdExpressDO>();
        
        list = thirdDriverService.findExpressAll();
      
        //list = thirdDriverService.findDriverAll(page, driver);
        
        //System.out.println("========大小：" + list.size());
        
        //System.out.println(Arrays.toString(list.toArray()));
        
        model.addAttribute("expressList", list);
        
        return "driver/add";
    }
    
    
 
    
    //添加用户参数，以及写入
    @ResponseBody
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String add(ThirdDriverDO driver, String expressCode) {
  
    	driver.setThirdExpressCode(expressCode);
    	
    	thirdDriverService.add(driver);
 
        //System.out.println(express.getExpressName());
        
    	return toJsonTrueMsg();
    }

    
    //更新方法
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String editPage(ThirdDriverDO driver, Model model) {
    	
    	Pagination page = getPage();
    	
        List<ThirdExpressDO> list  = new ArrayList<ThirdExpressDO>();
        
        list = thirdDriverService.findExpressAll();
        
        model.addAttribute("expressList", list);
        
        //第二段返回数据
        
    	model.addAttribute("resultDate", driver);
    	
    	//driver.setThirdExpressCode(expressCode);
    	
    	//thirdDriverService.add(driver);
    	
    	//model.addAttribute("nowDate", list);
    	
    	return "driver/edit";
    }
  
    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String edit(ThirdDriverDO driver) {
    	
    	driver.setCreatedTime(Long.valueOf(System.currentTimeMillis()));
    	
    	thirdDriverService.update(driver);

    	//userService.updateExpress(express);
    	 
        //System.out.println(express.getExpressName());
    	
    	return toJsonTrueMsg();

    }
    

    
    //删除方法
    @ResponseBody
    @RequestMapping(value = "/dele.html", method = RequestMethod.POST)
    public String dele(ThirdDriverDO driver) {
    	
    	driver.setCreatedTime(Long.valueOf(System.currentTimeMillis()));
    	
    	thirdDriverService.delete(driver);
 
        //System.out.println(express.getExpressName());
    	
    	return toJsonTrueMsg();
    	
    }
 
}
