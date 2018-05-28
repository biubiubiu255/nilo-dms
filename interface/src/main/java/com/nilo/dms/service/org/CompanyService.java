package com.nilo.dms.service.org;


import com.nilo.dms.dto.org.Company;

/**
 * Created by ronny on 2017/8/30.
 */
public interface CompanyService {

    void addCompany(Company company);

    void updateCompany(Company company);

    Company findByMerchantId(String merchantId);
}
