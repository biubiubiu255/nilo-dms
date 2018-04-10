package com.nilo.dms.web.controller.config;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.config.OrderHandleConfigService;
import com.nilo.dms.service.system.model.OrderHandleConfig;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
@Controller
@RequestMapping("/systemConfig/orderHandle")
public class OrderHandleConfigController extends BaseController {

    @Autowired
    private OrderHandleConfigService orderHandleConfigService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "system_config/order_handle/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getList() {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        List<OrderHandleConfig> list = orderHandleConfigService.queryAllBy(merchantId);
        Pagination page = new Pagination(0, list.size());
        page.setTotalCount(list.size());
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(String optType, Model model) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        OrderHandleConfig orderHandleConfig = orderHandleConfigService.queryBy(merchantId, optType);
        model.addAttribute("orderHandleConfig", orderHandleConfig);
        return "system_config/order_handle/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String updateConfig(String optTypeEdit, Integer updateStatus, @RequestParam(value = "allowStatus[]", required = false) Integer[] allowStatus, @RequestParam(value = "notAllowStatus[]", required = false) Integer[] notAllowStatus, String className) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            OrderHandleConfig config = new OrderHandleConfig();
            config.setMerchantId(merchantId);
            config.setOptType(optTypeEdit);
            config.setUpdateStatus(updateStatus);
            config.setClassName(className);
            if (allowStatus != null) {
                config.setAllowStatus(Arrays.asList(allowStatus));
            }
            if (notAllowStatus != null) {
                config.setNotAllowStatus(Arrays.asList(notAllowStatus));
            }
            orderHandleConfigService.updateHandleConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addPage(String optType, Model model) {

        return "system_config/order_handle/add";
    }


    @ResponseBody
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String addConfig(String optTypeAdd, Integer updateStatus, @RequestParam(value = "allowStatus[]", required = false) Integer[] allowStatus, @RequestParam(value = "notAllowStatus[]", required = false) Integer[] notAllowStatus, String className) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            OrderHandleConfig config = new OrderHandleConfig();
            config.setMerchantId(merchantId);
            config.setOptType(optTypeAdd);
            config.setUpdateStatus(updateStatus);
            config.setClassName(className);
            if (allowStatus != null) {
                config.setAllowStatus(Arrays.asList(allowStatus));
            }
            if (notAllowStatus != null) {
                config.setNotAllowStatus(Arrays.asList(notAllowStatus));
            }
            orderHandleConfigService.addConfig(config);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

}
