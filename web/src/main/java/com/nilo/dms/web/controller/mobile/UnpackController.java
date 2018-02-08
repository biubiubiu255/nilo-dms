package com.nilo.dms.web.controller.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.WaybillScanDao;
import com.nilo.dms.dao.WaybillScanDetailsDao;
import com.nilo.dms.dao.dataobject.DeliveryOrderDO;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.dao.dataobject.WaybillScanDetailsDO;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.model.UserInfo;
import com.nilo.dms.service.order.OrderOptLogService;
import com.nilo.dms.service.order.OrderService;
import com.nilo.dms.service.order.model.DeliveryOrder;
import com.nilo.dms.service.order.model.OrderOptRequest;
import com.nilo.dms.service.system.RedisUtil;
import com.nilo.dms.web.controller.BaseController;
import com.nilo.dms.web.controller.order.PackageController.NextStation;

@Controller
@RequestMapping("/mobile/network/unpackage")
public class UnpackController extends BaseController  {
	
    @Autowired
    private DistributionNetworkDao distributionNetworkDao;
    
    @Autowired
    private OrderOptLogService orderOptLogService;
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private WaybillScanDao waybillScanDao;
    @Autowired
    private WaybillScanDetailsDao waybillScanDetailsDao;
    
    @Autowired
    private UserService userService;
    
    
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
    
    @ResponseBody
    @RequestMapping(value = "/scanList.html")
    public String scanList(String scanNo) {

        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<DeliveryOrder> list = new ArrayList<>();
        Pagination pagination = new Pagination(0, 100);

        if (StringUtil.isEmpty(scanNo)) return toPaginationLayUIData(pagination, list);

        List<WaybillScanDetailsDO> scanDetailsDOList = waybillScanDetailsDao.queryByScanNo( scanNo);
        if (scanDetailsDOList == null) return toPaginationLayUIData(pagination, list);

        List<String> orderNos = new ArrayList<>();
        for (WaybillScanDetailsDO details : scanDetailsDOList) {
            orderNos.add(details.getOrderNo());
        }
        list = orderService.queryByOrderNos(merchantId, orderNos);
        pagination.setTotalCount(list.size());
        return toPaginationLayUIData(pagination, list);
    }
    
    
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/save.html")
    public String save(String[] scanNos, @RequestParam("logisticsNo") String packNo) {
    	Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
    	String arriveByString = me.getUserId();
    	String merchantId = me.getMerchantId();
    	orderService.updataMultiChildOrder(merchantId, me.getNetworks().get(0)+"", arriveByString, packNo, Arrays.asList(scanNos));
    	return toJsonTrueMsg();
    }

}
