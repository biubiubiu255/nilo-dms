package com.nilo.dms.service.system.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.CustomerTypeEnum;
import com.nilo.dms.common.enums.LevelEnum;
import com.nilo.dms.common.enums.MerchantStatusEnum;
import com.nilo.dms.common.enums.NatureTypeEnum;
import com.nilo.dms.common.exception.BizErrorCode;
import com.nilo.dms.common.exception.SysErrorCode;
import com.nilo.dms.common.utils.AssertUtil;
import com.nilo.dms.common.utils.StringUtil;
import com.nilo.dms.dao.MerchantInfoDao;
import com.nilo.dms.dao.dataobject.MerchantInfoDO;
import com.nilo.dms.service.system.MerchantService;
import com.nilo.dms.service.system.model.MerchantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronny on 2017/8/25.
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantInfoDao merchantInfoDao;

    /**
     * 添加商户
     *
     * @param merchant
     */
    @Override
    public void addMerchant(MerchantInfo merchant) {
        AssertUtil.isNotNull(merchant, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotBlank(merchant.getMerchantName(), BizErrorCode.MERCHANT_NAME_EMPTY);
        merchant.setStatus(MerchantStatusEnum.NORMAL);
        MerchantInfoDO infoDO = convertToDO(merchant);
        merchantInfoDao.insert(infoDO);

        merchant.setId(""+infoDO.getId());
    }

    /**
     * 更新商户基本信息
     *
     * @param merchant
     */
    @Override
    public void updateMerchantInfo(MerchantInfo merchant) {
        AssertUtil.isNotNull(merchant, SysErrorCode.REQUEST_IS_NULL);
        AssertUtil.isNotNull(merchant.getId(), BizErrorCode.MERCHANT_ID_EMPTY);
        merchant.setStatus(null);
        MerchantInfoDO infoDO = convertToDO(merchant);
        merchantInfoDao.update(infoDO);
    }

    /**
     * 更新商户状态
     *
     * @param merchantId
     * @param status
     */
    @Override
    public void updateMerchantStatus(Long merchantId, MerchantStatusEnum status) {

        AssertUtil.isNotNull(merchantId, BizErrorCode.MERCHANT_ID_EMPTY);
        AssertUtil.isNotNull(status, BizErrorCode.MERCHANT_STATUS_EMPTY);

        MerchantInfoDO infoDO = new MerchantInfoDO();
        infoDO.setId(merchantId);
        infoDO.setStatus(status.getCode());
        merchantInfoDao.update(infoDO);
    }

    @Override
    public List<MerchantInfo> findBy(String merchantName, Pagination page) {
        List<MerchantInfo> list = new ArrayList<>();

        Integer count = merchantInfoDao.findCountBy(merchantName);
        if(count==null || count ==0) return list;

        page.setTotalCount(count);
        List<MerchantInfoDO> merchantInfoDOList = merchantInfoDao.findBy(merchantName,page.getOffset(),page.getLimit());
        if (merchantInfoDOList != null) {
            for (MerchantInfoDO d : merchantInfoDOList) {
                list.add(convertTo(d));
            }
        }
        return list;
    }

    /**
     * 所有商户信息
     *
     * @return
     */
    @Override
    public List<MerchantInfo> findAll() {
        List<MerchantInfo> list = new ArrayList<>();
        List<MerchantInfoDO> merchantInfoDOList = merchantInfoDao.findAll();
        if (merchantInfoDOList != null) {
            for (MerchantInfoDO d : merchantInfoDOList) {
                list.add(convertTo(d));
            }
        }
        return list;
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @Override
    public MerchantInfo findById(String id) {
        AssertUtil.isNotNull(id, BizErrorCode.MERCHANT_ID_EMPTY);
        MerchantInfoDO merchantInfoDO = merchantInfoDao.queryById(Long.parseLong(id));
        if (merchantInfoDO == null) {
            return null;
        }
        return convertTo(merchantInfoDO);
    }

    private MerchantInfoDO convertToDO(MerchantInfo merchantInfo) {
        MerchantInfoDO d = new MerchantInfoDO();
        if (StringUtil.isNotEmpty(merchantInfo.getId())) {
            d.setId(Long.parseLong(merchantInfo.getId()));
        }
        if (merchantInfo.getStatus() != null) {
            d.setStatus(merchantInfo.getStatus().getCode());
        }
        d.setMerchantName(merchantInfo.getMerchantName());
        d.setAddress(merchantInfo.getAddress());
        d.setContactEmail(merchantInfo.getContactEmail());
        d.setContactName(merchantInfo.getContactName());
        d.setContactPhone(merchantInfo.getContactPhone());

        d.setCountry(merchantInfo.getCountry());
        d.setIdCard(merchantInfo.getIdCard());

        d.setDescription(merchantInfo.getDescription());
        d.setAppLogo(merchantInfo.getAppLogo());
        d.setPcLogo(merchantInfo.getPcLogo());

        if (merchantInfo.getNature() != null) {
            d.setNature(merchantInfo.getNature().getCode());
        }
        if (merchantInfo.getType() != null) {
            d.setType(merchantInfo.getType().getCode());
        }
        if (merchantInfo.getLevel() != null) {
            d.setLevel(merchantInfo.getLevel().getCode());
        }
        d.setVip(merchantInfo.getVip());
        d.setWebsite(merchantInfo.getWebsite());
        d.setAddress(merchantInfo.getAddress());
        d.setLicence(merchantInfo.getLicence());

        return d;
    }

    private MerchantInfo convertTo(MerchantInfoDO d) {

        MerchantInfo info = new MerchantInfo();
        info.setId("" + d.getId());
        info.setMerchantName(d.getMerchantName());
        info.setStatus(MerchantStatusEnum.getEnum(d.getStatus()));
        info.setDescription(d.getDescription());
        info.setAppLogo(d.getAppLogo());
        info.setPcLogo(d.getPcLogo());

        info.setLicence(d.getLicence());
        info.setAddress(d.getAddress());
        info.setNature(NatureTypeEnum.getEnum(d.getNature()));
        info.setType(CustomerTypeEnum.getEnum(d.getType()));
        info.setContactEmail(d.getContactEmail());
        info.setContactName(d.getContactName());
        info.setContactPhone(d.getContactPhone());
        info.setCountry(d.getCountry());
        info.setIdCard(d.getIdCard());
        info.setLevel(LevelEnum.getEnum(d.getLevel()));
        info.setVip(d.getVip());
        info.setWebsite(d.getWebsite());


        info.setCreatedTime(d.getCreatedTime());
        info.setUpdatedTime(d.getUpdatedTime());
        return info;
    }
}
