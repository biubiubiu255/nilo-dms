package com.nilo.dms.service.org.impl;

import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.CompanyDao;
import com.nilo.dms.dao.dataobject.CompanyDO;
import com.nilo.dms.dto.org.Company;
import com.nilo.dms.service.org.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ronny on 2017/8/30.
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public void addCompany(Company company) {

        CompanyDO companyDO = convertToDO(company);
        companyDao.insert(companyDO);
        company.setCompanyId("" + companyDO.getId());
    }

    @Override
    public void updateCompany(Company company) {
        CompanyDO companyDO = convertToDO(company);
        companyDao.update(companyDO);
    }

    @Override
    public Company findByMerchantId(String merchantId) {
        if (StringUtil.isEmpty(merchantId)) {
            return null;
        }
        CompanyDO companyDO = companyDao.findByMerchantId(Long.parseLong(merchantId));
        if (companyDO == null) {
            return null;
        }
        return convertTo(companyDO);
    }

    private CompanyDO convertToDO(Company company) {
        CompanyDO companyDO = new CompanyDO();
        if (StringUtil.isNotEmpty(company.getCompanyId())) {
            companyDO.setId(Long.parseLong(company.getCompanyId()));
        }
        if (StringUtil.isNotEmpty(company.getMerchantId())) {
            companyDO.setMerchantId(Long.parseLong(company.getMerchantId()));
        }
        companyDO.setAddress(company.getAddress());
        companyDO.setAppLogo(company.getAppLogo());
        companyDO.setCompanyName(company.getCompanyName());
        companyDO.setDescription(company.getDescription());
        companyDO.setEmail(company.getEmail());
        companyDO.setPcLogo(company.getPcLogo());
        companyDO.setPhone(company.getPhone());

        return companyDO;
    }

    private Company convertTo(CompanyDO d) {
        Company company = new Company();
        company.setCompanyId("" + d.getId());
        company.setPhone(d.getPhone());
        company.setPcLogo(d.getPcLogo());
        company.setEmail(d.getEmail());
        company.setDescription(d.getDescription());
        company.setAddress(d.getAddress());
        company.setAppLogo(d.getAppLogo());
        company.setCompanyName(d.getCompanyName());
        company.setMerchantId("" + d.getMerchantId());
        company.setCreatedTime(d.getCreatedTime());
        return company;
    }


}
