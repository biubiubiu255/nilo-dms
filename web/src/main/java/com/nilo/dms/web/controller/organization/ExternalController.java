package com.nilo.dms.web.controller.organization;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.UserNetworkDao;
import com.nilo.dms.dao.dataobject.ThirdExpressDO;
import com.nilo.dms.dao.dataobject.WaybillExternalDo;
import com.nilo.dms.service.RoleService;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.org.ExternalService;
import com.nilo.dms.service.system.DistributionNetworkService;
import com.nilo.dms.web.controller.BaseController;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/waybill/external")
public class ExternalController extends BaseController {

    @Autowired
    private ExternalService externalService;

    @ResponseBody
    @RequestMapping(value = "/externalList.html", method = RequestMethod.POST)
    public String expressList(WaybillExternalDo external) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();  //主要的，主体
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<WaybillExternalDo> list  = new ArrayList<WaybillExternalDo>();

        System.out.println("【参数输出！】" + external.toString());

        if (external.getOrderNo()==null || external.getOrderNo().equals("")) {
            external.setOrderNo("");
		}
        list = externalService.findExternalAllFuzzy(external, page);
        return toPaginationLayUIData(page, list);  //Pagination 页码
    }
     

    @RequestMapping(value = "/externalPage.html", method = RequestMethod.GET)
    public String externalList(Model model) {
        return "external/externalList";
    }
    
    /*    二级分割线        */
    
    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addExternalPage(Model model) {
        return "external/addPage";
    }
    
    
    //添加用户参数，以及写入
    @ResponseBody
    @RequestMapping(value = "/addInfo.html", method = RequestMethod.POST)
    public String addExternalInfo(WaybillExternalDo external, @RequestParam("receive_time") String time) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(time);
            external.setReceiveTime((int)(date.getTime()/1000));
            Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();  //主要的，主体
            if (external.getCreator()==null || external.getCreator().equals("")) external.setCreator(me.getUserName());
            externalService.addExternal(external);
            return toJsonTrueMsg();
        } catch (ParseException e) {
            e.printStackTrace();
            return toJsonErrorMsg("pleace input date");
        }

    }

    //更新方法
    @RequestMapping(value = "/editInfoPage.html", method = RequestMethod.GET)
    public String editExpressInfoPage(WaybillExternalDo external, Model model) {
    	model.addAttribute("resultDate", external);
        external = externalService.findSingleByID(external.getId().intValue());
        //System.out.println(external.toString());
        model.addAttribute("resultData", external);
    	return "external/editPage";
    }
    
    @ResponseBody
    @RequestMapping(value = "/editInfo.html", method = RequestMethod.POST)
    public String editExpressInfo(WaybillExternalDo external) {
        externalService.updateExternal(external);
        //System.out.println(express.getExpressName());
    	return toJsonTrueMsg();
    }

    
    //删除方法
    @ResponseBody
    @RequestMapping(value = "/deleExternalInfo.html", method = RequestMethod.POST)
    public String deleExpressInfo(WaybillExternalDo external) {
        externalService.deleteExternal(external);
        //System.out.println(express.getExpressName());
    	return toJsonTrueMsg();
    }
    
}
