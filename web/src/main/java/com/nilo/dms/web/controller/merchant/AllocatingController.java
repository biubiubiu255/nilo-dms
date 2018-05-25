package com.nilo.dms.web.controller.merchant;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.Principal;
import com.nilo.dms.common.enums.DistributionStatusEnum;
import com.nilo.dms.common.enums.DistributionTypeEnum;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dto.system.DistributionNetwork;
import com.nilo.dms.dto.system.MerchantInfo;
import com.nilo.dms.service.impl.SessionLocal;
import com.nilo.dms.service.system.DistributionNetworkService;
import com.nilo.dms.service.system.MerchantService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
@Controller
@RequestMapping("/admin/allocating")
public class AllocatingController extends BaseController {

    @Autowired
    private DistributionNetworkService distributionNetworkService;

    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/list.html")
    public String list(Model model) {
        return "merchant/allocating/list";
    }

    @ResponseBody
    @RequestMapping("/getList.html")
    public String getList(String name) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Pagination page = getPage();
        List<DistributionNetwork> list = distributionNetworkService.queryBy(merchantId, name, page);
        return toPaginationLayUIData(page, list);
    }


    @RequestMapping(value = "/editPage.html", method = RequestMethod.GET)
    public String updateRulePage(String code, Model model) {

        Principal me = SessionLocal.getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        DistributionNetwork network = distributionNetworkService.queryByCode(merchantId, code);
        model.addAttribute("network", network);
        return "merchant/allocating/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    public String updateConfig(DistributionNetwork network) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            network.setMerchantId(merchantId);
            distributionNetworkService.update(network);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @RequestMapping(value = "/addPage.html", method = RequestMethod.GET)
    public String addPage(Model model) {

        List<MerchantInfo> list = merchantService.findAll();
        model.addAttribute("merchantList", list);
        return "merchant/allocating/add";
    }


    @ResponseBody
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String addConfig(DistributionNetwork network, String isSelfCollect) {
        try {
            if (StringUtil.isEmpty(isSelfCollect)) {
                isSelfCollect = DistributionTypeEnum.NETWORK.getCode();
            }
            network.setType(DistributionTypeEnum.getEnum(isSelfCollect));
            distributionNetworkService.add(network);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/active.html", method = RequestMethod.POST)
    public String active(String code) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            DistributionNetwork network = new DistributionNetwork();
            network.setMerchantId(merchantId);
            network.setCode(code);
            network.setStatus(DistributionStatusEnum.NORMAL);
            distributionNetworkService.update(network);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/delete.html", method = RequestMethod.POST)
    public String delete(String code) {
        try {
            Principal me = SessionLocal.getPrincipal();
            //获取merchantId
            String merchantId = me.getMerchantId();
            DistributionNetwork network = new DistributionNetwork();
            network.setMerchantId(merchantId);
            network.setCode(code);
            network.setStatus(DistributionStatusEnum.DISABLE);
            distributionNetworkService.update(network);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }
}
