package com.nilo.dms.web.controller.organization;

import com.nilo.dms.service.org.CompanyService;
import com.nilo.dms.service.org.model.Company;
import com.nilo.dms.common.Principal;
import com.nilo.dms.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/details.html", method = RequestMethod.GET)
    public String details(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Company company = companyService.findByMerchantId(merchantId);
        // 查询公司组织架构
        model.addAttribute("company", company);
        return "company/details";
    }

    @RequestMapping(value = "/editPage.html")
    public String editPage(Model model) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Company company = companyService.findByMerchantId(merchantId);

        model.addAttribute("company", company);
        return "company/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/updateCompany.html", method = RequestMethod.POST)
    public String updateCompany(Company company) {
        Principal me = (Principal) SecurityUtils.getSubject().getPrincipal();
        //获取merchantId
        String merchantId = me.getMerchantId();
        Company query = companyService.findByMerchantId(merchantId);
        try {
            if (query == null) {
                companyService.addCompany(company);
            } else {
                companyService.updateCompany(company);
            }
        } catch (Exception e) {
            return toJsonErrorMsg(e.getMessage());
        }
        return toJsonTrueMsg();


    }

}
