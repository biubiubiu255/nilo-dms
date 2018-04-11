package com.nilo.dms.web.controller.order;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.OptTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.DMSException;
import com.nilo.dms.dto.order.OrderOptRequest;
import com.nilo.dms.dto.order.Waybill;
import com.nilo.dms.dto.order.WaybillParameter;
import com.nilo.dms.service.UserService;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.order.WaybillService;
import com.nilo.dms.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ronny on 2017/9/15.
 */
@Controller
@RequestMapping("/order/doorToDoor")
public class DoorToDoorController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private WaybillService waybillService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/listPage.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "door_to_door/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html")
    public String getOrderList(WaybillParameter parameter) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        parameter.setMerchantId(merchantId);
        parameter.setOrderType(Arrays.asList(new String[]{"DS"}));

        Pagination page = getPage();
        List<Waybill> list = waybillService.queryWaybillBy(parameter, page);

        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/allocatePage.html")
    public String allocatePage(Model model, HttpServletRequest request, @RequestParam(value = "orderNos[]") String[] orderNos) {
        request.getSession().setAttribute("orderNos", orderNos);
        model.addAttribute("list", getRiderList());
        return "door_to_door/allocatePage";
    }

    @ResponseBody
    @RequestMapping(value = "/allocate.html")
    public String allocate(String userId, String remark, HttpServletRequest request) {

        String[] orderNos = (String[]) request.getSession().getAttribute("orderNos");
        Principal me = SessionLocal.getPrincipal();
        String merchantId = me.getMerchantId();
        try {
            if (orderNos == null) {
                throw new DMSException(BizErrorCode.ORDER_NO_EMPTY);
            }
            //操作订单
            OrderOptRequest optRequest = new OrderOptRequest();
            optRequest.setOptType(OptTypeEnum.ALLOCATE);
            optRequest.setOrderNo(Arrays.asList(orderNos));
            waybillService.handleOpt(optRequest);

        } catch (Exception e) {
            log.error("allocate failed. orderNo:{}", orderNos, e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();

    }

    @RequestMapping(value = "/print.html")
    public String print(Model model, HttpServletRequest request, String orderNos) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<Waybill> list = waybillService.queryByOrderNos(merchantId, Arrays.asList(orderNos.split(",")));
        for (Waybill o : list) {
            if (o.isPrinted()) {
                throw new IllegalArgumentException("Delivery Order :" + o.getOrderNo() + " is already printed.");
            }
        }
        waybillService.print(merchantId, Arrays.asList(orderNos.split(",")));
        model.addAttribute("list", list);
        return "door_to_door/print";
    }

    @RequestMapping(value = "/printAgain.html")
    public String printAgain(Model model, HttpServletRequest request, String orderNos) {
        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<Waybill> list = waybillService.queryByOrderNos(merchantId, Arrays.asList(orderNos.split(",")));

        waybillService.print(merchantId, Arrays.asList(orderNos.split(",")));

        model.addAttribute("list", list);
        return "door_to_door/print";
    }

}
