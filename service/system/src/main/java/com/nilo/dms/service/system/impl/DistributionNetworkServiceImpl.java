package com.nilo.dms.service.system.impl;

import com.nilo.dms.common.Pagination;
import com.nilo.dms.common.enums.DistributionStatusEnum;
import com.nilo.dms.common.enums.DistributionTypeEnum;
import com.nilo.dms.dao.DistributionNetworkDao;
import com.nilo.dms.dao.dataobject.DistributionNetworkDO;
import com.nilo.dms.service.system.MerchantService;
import com.nilo.dms.service.system.DistributionNetworkService;
import com.nilo.dms.service.system.model.DistributionNetwork;
import com.nilo.dms.service.system.model.MerchantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/12/5.
 */
@Service
public class DistributionNetworkServiceImpl implements DistributionNetworkService {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private DistributionNetworkDao distributionNetworkDao;

    @Override
    public void add(DistributionNetwork network) {

        network.setStatus(DistributionStatusEnum.NORMAL);
        DistributionNetworkDO networkDO = convert(network);

        distributionNetworkDao.insert(networkDO);

    }

    @Override
    public void update(DistributionNetwork network) {

        DistributionNetworkDO networkDO = convert(network);
        distributionNetworkDao.update(networkDO);

    }

    @Override
    public List<DistributionNetwork> queryBy(String merchantId,String name, Pagination pagination) {

        List<DistributionNetwork> list = new ArrayList<>();

        Long count = distributionNetworkDao.findCountBy(Long.parseLong(merchantId),name);
        if (count == null || count == 0) return list;

        pagination.setTotalCount(count);
        List<DistributionNetworkDO> queryList = distributionNetworkDao.findBy(Long.parseLong(merchantId),name, pagination.getOffset(), pagination.getLimit());
        for (DistributionNetworkDO network : queryList) {

            DistributionNetwork n = convert(network);
            MerchantInfo merchantInfo = merchantService.findById(n.getMerchantId());
            n.setMerchantName(merchantInfo.getMerchantName());
            list.add(n);
        }

        return list;
    }

    @Override
    public DistributionNetwork queryByCode(String merchantId, String code) {

        DistributionNetworkDO networkDO = distributionNetworkDao.findByCode(Long.parseLong(merchantId), code);
        if (networkDO == null) return null;
        return convert(networkDO);
    }

    @Override
    public void updateUserNetwork(String merchantId, String userId, Long[] distributions) {

    }

    private DistributionNetwork convert(DistributionNetworkDO distributionNetworkDO) {

        DistributionNetwork network = new DistributionNetwork();

        network.setId(""+distributionNetworkDO.getId());
        network.setAddress(distributionNetworkDO.getAddress());
        network.setName(distributionNetworkDO.getName());
        network.setCity(distributionNetworkDO.getCity());
        network.setArea(distributionNetworkDO.getArea());
        network.setProvince(distributionNetworkDO.getProvince());
        network.setCode(distributionNetworkDO.getCode());
        network.setStatus(DistributionStatusEnum.getEnum(distributionNetworkDO.getStatus()));
        network.setMerchantId("" + distributionNetworkDO.getMerchantId());
        network.setCountry(distributionNetworkDO.getCountry());
        network.setRemark(distributionNetworkDO.getRemark());
        network.setType(DistributionTypeEnum.getEnum(distributionNetworkDO.getType()));

        return network;

    }

    private DistributionNetworkDO convert(DistributionNetwork network) {

        DistributionNetworkDO networkDO = new DistributionNetworkDO();

        networkDO.setMerchantId(Long.parseLong(network.getMerchantId()));
        networkDO.setRemark(network.getRemark());
        if (network.getType() != null) {
            networkDO.setType(network.getType().getCode());
        }
        networkDO.setCountry(network.getCountry());
        networkDO.setArea(network.getArea());
        networkDO.setCity(network.getCity());
        networkDO.setProvince(network.getProvince());
        networkDO.setAddress(network.getAddress());
        networkDO.setName(network.getName());
        networkDO.setCode(network.getCode());
        if (network.getStatus() != null) {
            networkDO.setStatus(network.getStatus().getCode());
        }

        return networkDO;

    }
}
