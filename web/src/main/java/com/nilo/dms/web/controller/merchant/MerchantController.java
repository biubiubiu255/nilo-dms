package com.nilo.dms.web.controller.merchant;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.CustomerTypeEnum;
import com.nilo.dms.common.enums.LevelEnum;
import com.nilo.dms.common.enums.MerchantStatusEnum;
import com.nilo.dms.common.enums.NatureTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dto.system.MerchantInfo;
import com.nilo.dms.service.system.MerchantService;
import com.nilo.dms.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/merchant")
public class MerchantController extends BaseController {

    @Autowired
    private MerchantService merchantService;


    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String list(Model model) {
        return "merchant/list";
    }

    @ResponseBody
    @RequestMapping(value = "/list.html", method = RequestMethod.POST)
    public String getUserList(String merchantName) {

        Pagination page = getPage();

        List<MerchantInfo> list = merchantService.findBy(merchantName,page);

        return toPaginationLayUIData(page, list);
    }

    @RequestMapping(value = "/editPage.html")
    public String editPage(Model model, String id) {

        MerchantInfo merchantInfo = merchantService.findById(id);
        model.addAttribute("merchantInfo", merchantInfo);
        if(merchantInfo.getType() == CustomerTypeEnum.EB) {
            return "merchant/editEnterprise";
        }else{
            return "merchant/editPerson";
        }
    }

    @RequestMapping(value = "/addPage.html")
    public String addPage(Model model, String type) {
        if (StringUtil.equals(type, "enterprise")) {
            return "merchant/addEnterprise";
        } else {
            return "merchant/addPerson";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/addMerchant.html", method = RequestMethod.POST)
    public String addMerchant(MerchantInfo info, String natureCode, String typeCode, String levelCode) {

        try {
            if(StringUtil.isNotEmpty(natureCode)){
                info.setNature(NatureTypeEnum.getEnum(natureCode));
            }
            if(StringUtil.isNotEmpty(typeCode)){
                info.setType(CustomerTypeEnum.getEnum(typeCode));
            }
            if(StringUtil.isNotEmpty(levelCode)){
                info.setLevel(LevelEnum.getEnum(levelCode));
            }
            AssertUtil.isNotNull(info, SysErrorCode.REQUEST_IS_NULL);
            merchantService.addMerchant(info);
        } catch (Exception e) {
            logger.error("Add Failed.",e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueData(info.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/updateMerchant.html", method = RequestMethod.POST)
    public String updateMerchant(MerchantInfo info) {

        try {
            AssertUtil.isNotNull(info, SysErrorCode.REQUEST_IS_NULL);
            //更新基本信息
            merchantService.updateMerchantInfo(info);
        } catch (Exception e) {
            logger.error("Update Failed.",e);
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }


    @ResponseBody
    @RequestMapping("/delMerchant.html")
    public String delMerchant(Long id) {
        try {
            AssertUtil.isNotNull(id, BizErrorCode.MERCHANT_ID_EMPTY);
            MerchantStatusEnum status = MerchantStatusEnum.DISABLED;
            merchantService.updateMerchantStatus(id, status);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping("/activeMerchant.html")
    public String activeMerchant(Long id) {
        try {
            AssertUtil.isNotNull(id, BizErrorCode.MERCHANT_ID_EMPTY);
            MerchantStatusEnum status = MerchantStatusEnum.NORMAL;
            merchantService.updateMerchantStatus(id, status);
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();
    }

    @ResponseBody
    @RequestMapping("/{userId}.html")
    public MerchantInfo getUser(@PathVariable String id) {
        return merchantService.findById(id);
    }

}
