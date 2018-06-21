package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Constant;
import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.dao.HandleDelayDao;
import com.nilo.dms.dao.HandleRefuseDao;
import com.nilo.dms.dto.handle.HandleRefuse;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.AbnormalOrderService;
import com.nilo.dms.service.order.WaybillOptService;
import com.nilo.dms.service.system.SystemCodeUtil;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by admin on 2017/11/2.
 */
@Controller
@RequestMapping("/order/refuse")
public class RefuseOrderController extends BaseController {
    @Autowired
    private WaybillOptService waybillOptService;
    @Autowired
    private AbnormalOrderService abnormalOrderService;
    @Autowired
    private HandleDelayDao handleDelayDao;

    @Autowired
    private HandleRefuseDao handleRefuseDao;

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "refuse_order/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getOrderList(HandleRefuse handleRefuse, Integer fromTime, Integer toTime) {
        //获取merchantId
        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());
        handleRefuse.setMerchantId(merchantId);
        Pagination page = getPage();
        List<HandleRefuse> handleRefuseList = handleRefuseDao.queryBy(handleRefuse, fromTime, toTime, page.getOffset(), page.getLimit());
        Long queryCount = handleRefuseDao.queryCountBy(handleRefuse, fromTime, toTime);
        if(queryCount!=null){
            page.setTotalCount(queryCount);
        }
        for(int i=0;i<handleRefuseList.size();i++){
            HandleRefuse tempHandleRefuse = handleRefuseList.get(i);
            tempHandleRefuse.setReason(SystemCodeUtil.getCodeVal(merchantId.toString(), Constant.REFUSE_REASON, handleRefuseList.get(i).getReason()));
            handleRefuseList.set(i, tempHandleRefuse);
        }
        return toPaginationLayUIData(page, handleRefuseList);
    }

    @RequestMapping(value = "/addPage.html")
    public String addPage(Model model, String orderNo) {
        //查询上一次dispatch task
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        //查询rider列表
        //model.addAttribute("delayDO", delayDO);
        return "refuse_order/add";
    }

    @ResponseBody
    @RequestMapping(value = "/add.html")
    public String add(HandleRefuse handleRefuse) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        Long merchantId = Long.parseLong(SessionLocal.getPrincipal().getMerchantId());

        handleRefuse.setMerchantId(merchantId);
        handleRefuse.setHandleBy(Long.parseLong(me.getUserId()));
        handleRefuse.setHandleName(me.getUserName());

        waybillOptService.refuse(handleRefuse);


        return toJsonTrueMsg();
    }

}
